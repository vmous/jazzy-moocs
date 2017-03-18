package lecturecode

import org.apache.spark.{ SparkConf, SparkContext }

object CFFData {

  val sConf: SparkConf = new SparkConf()
    .setAppName("cff-data")
    .setMaster("local[*]")
  val sc: SparkContext = SparkContext.getOrCreate(sConf)

  val as = List((101, ("Ruelti", "AG")), (102, ("Bralaz", "DemiTarif")), (103, ("Gress", "DemiTarifVisa")), (104, ("Schatten", "DemiTarif")))
  val abos = sc.parallelize(as)

  val ls = List((101, "Bern"), (101,"Thun"), (102, "Lausanne"), (102, "Geneve"), (102, "Nyon"), (103, "Zurich"), (103, "St-Gallen"), (103, "Chur"))
  val locations = sc.parallelize(ls)

  def main(args: Array[String]): Unit = {
    // CFF wants to know for which customers we have subscription info and
    // location info (use the CFF smartphone app so we can keep track of which
    // cities they regularly travel to.
    abos.join(locations).collect().foreach(println)

    // CFF wants to know for which subscribers it has collected location
    // information. For example, we know it's possible that somebody has a
    // subscription like a DemiTarif. But doesn't use the mobile app and will
    // always pay for tickets with cash at a machine.
    abos.leftOuterJoin(locations).collect().foreach(println)

    // CFF wants to know for which of its smartphone app users it has
    // subscriptions for. That way CFF could offer a discount on a subscription
    // to one of these users. So maybe the CFF would like to identify the people
    // who use the mobile app, but don't yet have a DemiTarif subscription.
    abos.rightOuterJoin(locations).collect().foreach(println)

    sc.stop()
  }
}
