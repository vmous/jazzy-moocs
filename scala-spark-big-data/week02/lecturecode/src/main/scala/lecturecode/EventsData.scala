package lecturecode

import java.io.File

import org.apache.spark.{ SparkConf, SparkContext }

case class Event(organizer: String, name: String, budget: Int)

object EventsData {

  val sConf: SparkConf = new SparkConf()
    .setAppName("events-data")
    .setMaster("local[*]")
  val sc: SparkContext = SparkContext.getOrCreate(sConf)

  val filePath = new File(this.getClass.getClassLoader.getResource("events.dat").toURI).getPath

  def parse(line: String): Event = {
    val raw = line.split(";")
    Event(raw(0), raw(1), Integer.parseInt(raw(2)))
  }

  val eventsRdd = sc.textFile(filePath, 1)
    .map(EventsData.parse)
    .map(event => (event.organizer, event.budget))
    .cache()

  def main(args: Array[String]): Unit = {

    // Budget or each event per organizer
    eventsRdd.groupByKey().collect().foreach(println)

    // Sum of budgets of all events per organizer
    eventsRdd.reduceByKey(_ + _).collect().foreach(println)

    // Average budget per organizer
    eventsRdd.mapValues(budget => (budget, 1))
      .reduceByKey((v1, v2) => (v1._1 + v2._1, v1._2 + v2._2))
      .mapValues { case (budgetsSum, eventsNum) => budgetsSum / eventsNum }
      .collect()
      .foreach(println)

    sc.stop()
  }

}
