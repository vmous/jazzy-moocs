package observatory

import java.sql.Date
import java.time.LocalDate

import org.apache.spark.sql.types.StructType

import org.apache.spark.sql.{ Dataset, Encoders, SparkSession }

/**
  * 1st milestone: data extraction
  */
object Extraction {

  val spark: SparkSession = SparkSession
    .builder()
    .appName("obervatory")
    .config("spark.master", "local[*]")
    .getOrCreate

  val temperatureMissing: Double = 9999.9

  def farenheit2Celcius(f: Double): Double = (f - 32) * 5 / 9

  import spark.implicits._

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {

    val stationsFilePath: String = this.getClass.getResource(stationsFile).getPath()
    val stationsSchema: StructType = Encoders.product[Station].schema
    val stationsDS: Dataset[Station] = spark.read
      .schema(stationsSchema)
      .csv(stationsFilePath)
      .filter($"latitude".isNotNull)
      .filter($"longtitude".isNotNull)
      .na.fill(0)
      .as[Station]

    val temperaturesFilePath: String = this.getClass.getResource(temperaturesFile).getPath
    val temperaturesSchema: StructType = Encoders.product[Temperature].schema
    val temperaturesDS: Dataset[Temperature] = spark.read
      .schema(temperaturesSchema)
      .csv(temperaturesFilePath)
      .withColumn("temperature", ($"temperature" - 32) / 1.8).na.fill(0)
      .as[Temperature]
      //.filter((t: Temperature) ⇒ t.temperature != temperatureMissing)

    val locUDF = org.apache.spark.sql.functions.udf((lat: Double, lon: Double) => { Location(lat, lon) })
    val dateUDF = org.apache.spark.sql.functions.udf((m: Int, d: Int) => { new Date(year - 1900, m - 1, d) })

    val joined: Dataset[Record] = stationsDS
      .join(temperaturesDS, Seq("stn", "wban"), "inner")
      .withColumn("location", locUDF($"latitude", $"longtitude"))
      .withColumn("date", dateUDF($"month", $"day"))
      .select("date", "location", "temperature")
      .as[Record]
      .cache()

    new Iterable[Record] {
      def iterator = scala.collection.JavaConversions.asScalaIterator(joined.toLocalIterator)
    }.map(r => (r.date.toLocalDate(), r.location, r.temperature))

  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    records
      .groupBy(_._2)
      .map{case (k, l) => (k, l.map(_._3).sum / l.size)}
  }

}
