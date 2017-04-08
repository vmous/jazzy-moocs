package lecturecode

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{ Dataset, Row, SparkSession }
import org.apache.spark.sql.types._

object EmployeesData {

  val people: Seq[(Int, String, String)] = List(
    (1, "Yuxiang", "北京市"),
    (2, "Vassilis", "Αθήνα"),
    (3, "Pablo", "Madrid"),
    (4, "Max", "München"),
    (5, "Michael", "München"))

  def getPeopleTupleRDD(spark: SparkSession): RDD[(Int, String, String)] = {
    spark.sparkContext.parallelize(people)
  }

  case class Person(id: Int, name: String, city: String)
  def getPeopleCaseClassRDD(spark: SparkSession): RDD[Person] = {
    val peoplePerson = people.map(p => Person(p._1, p._2, p._3))
    spark.sparkContext.parallelize(peoplePerson)
  }

  val explicitPeopleSchema: StructType = {
    val fields: Seq[StructField] = List(
      StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("city", StringType, true))

    StructType(fields)
  }

  def getDFWithSchemaInference(spark: SparkSession): Dataset[Row] = {
    import spark.implicits._
    getPeopleTupleRDD(spark).toDF()
  }

  def getDFWithSchemaInferenceWithNamedColumns(spark: SparkSession): Dataset[Row] = {
    import spark.implicits._
    getPeopleTupleRDD(spark).toDF("id", "name", "city")
  }

  def getDFWitSchemaInferenceWithCaseClass(spark: SparkSession): Dataset[Row] = {
    import spark.implicits._
    getPeopleCaseClassRDD(spark).toDF
  }

  def getDFWithSchemaDefinition(spark: SparkSession): Dataset[Row] = {
    val rowPeopleRDD = getPeopleTupleRDD(spark)
      .map(attr => Row(attr._1, attr._2, attr._3))

    spark.createDataFrame(rowPeopleRDD, explicitPeopleSchema)
  }

  def getDFFromDataSource(spark: SparkSession): Dataset[Row] = {
    spark.read
      .schema(explicitPeopleSchema)
      .csv("src/main/resources/people.csv")
  }

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("employees-data")
      .getOrCreate()

    val peopleDF =
      //getDFWitSchemaInferenceWithCaseClass(spark)
      getDFFromDataSource(spark)
    // Register the DataFrame as an SQL temporary view
    // This essentially gives a name to our DataFrame in SQL
    // so we can refer to it in an SQL FROM statement.
    peopleDF
      .createOrReplaceTempView("people")

    peopleDF.printSchema()

    // SQL literals
    spark.sql("SELECT * FROM people WHERE city = 'Αθήνα'").show()

    // For the `$"colname"` notations
    import spark.implicits._

    // DataFrame API
    peopleDF.select("id", "name")
      .where("city == 'München'") // `filter` is equivalent to `where`
      .orderBy("id")
      .sort($"id".desc).show()

    peopleDF.filter($"id" === 3).show()
    peopleDF.filter(peopleDF("name") === "Vassilis").show()
    peopleDF.filter("city = '北京市'").show()

    // DataFrame aggregations
    peopleDF.groupBy($"city")
      .agg(org.apache.spark.sql.functions.count($"city")).show()

    spark.close()

  }

}
