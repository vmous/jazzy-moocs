package lecturecode

import org.apache.spark.{ RangePartitioner, SparkConf, SparkContext }

object CFFPurchasesData {

  case class CFFPurchase(customerId: Int, destination: String, price: Double)

  val sConf: SparkConf = new SparkConf()
    .setAppName("cff-purchases-data")
    .setMaster("local[*]")
  val sc: SparkContext = SparkContext.getOrCreate(sConf)

  // Yocto data-set of one month's period.
  val purchases = List(
    CFFPurchase(100, "Geneva", 22.25),
    CFFPurchase(300, "Zurich", 42.10),
    CFFPurchase(100, "Fribourg", 12.40),
    CFFPurchase(200, "St. Gallen", 8.20),
    CFFPurchase(100, "Lucerne", 31.60),
    CFFPurchase(300, "Basel", 16.20)
  )
  val purchasesRdd = sc.parallelize(purchases)

  def main(args: Array[String]): Unit = {
    // CFF wants to knxow how many trips and how much money was spent by each
    // individual customer over that last one month period.
    // Caution!: This shuffles data across the network (groupByKey) before our
    //           reduction operation.
    purchasesRdd.map(p => (p.customerId, p.price)) // Pair RDD
      .groupByKey() // groupByKey returns RDD[K, Iterable[V]]
      .mapValues(amounts => (amounts.size, amounts.sum))
      .collect()
      .foreach(println)

    // The same as above but 3x MORE EFFICIENT than the first one;)
    purchasesRdd.map(p => (p.customerId, (1, p.price)))
      .reduceByKey { case (one, other) => (one._1 + other._1, one._2 + other._2) }
      .collect()
      .foreach(println)

    // Gimme 9x MORE EFFICIENCY compared to the first one with partitioning!
    val pairs = purchasesRdd.map(p => (p.customerId, p.price))
    val tunedPartitioner = new RangePartitioner(8, pairs)
    val partitioned = pairs.partitionBy(tunedPartitioner)
      .persist() // DO NOT FORGET TO persist AFTER RE-PARTITIONING
    val purchasesPerCust = partitioned.map(p => (p._1, (1, p._2)))
    val purchasesPerMonth = purchasesPerCust
      .reduceByKey { case (one, other) => (one._1 + other._1, one._2 + other._2) }
      .collect()
      .foreach(println)

    sc.stop()
  }
}
