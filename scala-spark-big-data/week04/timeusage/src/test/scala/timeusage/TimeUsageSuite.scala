package timeusage

import org.apache.spark.sql.{ Column, ColumnName, DataFrame, Row }
import org.apache.spark.sql.types.{
  DoubleType,
  StringType,
  StructField,
  StructType
}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class TimeUsageSuite extends FunSuite with BeforeAndAfterAll {

  lazy val (headerColumns, df) = TimeUsage.read("/timeusage/atussum.csv")

  test("read data source") {
    assert(headerColumns.length === 455)
    val head = df.head()
    assert(head.length === 455)
    assert(head(0).isInstanceOf[String])
    for (i <- 1 until head.length)
      assert(head(i).isInstanceOf[Double])
  }

  test("classifiedColumns") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)
    assert(primary.nonEmpty)
    assert(working.nonEmpty)
    assert(other.nonEmpty)
    assert(primary.length + working.length + other.length === 424)
    assert(primary.toSet - working.toSet - other.toSet === primary.toSet)
    assert(working.toSet - primary.toSet - other.toSet === working.toSet)
    assert(other.toSet - primary.toSet - working.toSet === other.toSet)
  }

  test("timeUsageSummary") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)

    val summaryDf = TimeUsage.timeUsageSummary(primary, working, other, df.sample(withReplacement = false, 0.1))
    assert(summaryDf.columns === Array("working", "sex", "age", "primaryNeeds", "work", "other"))
    summaryDf.show(10)
  }

  test("timeUsageGrouped") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)

    val summaryDf = TimeUsage.timeUsageSummary(primary, working, other, df.sample(withReplacement = false, 0.1))
    val groupedDf = TimeUsage.timeUsageGrouped(summaryDf)
    groupedDf.show(10)
  }

  test("timeUsageGroupedSql") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)

    val summaryDf = TimeUsage.timeUsageSummary(primary, working, other, df.sample(withReplacement = false, 0.1))
    val groupedDf = TimeUsage.timeUsageGroupedSql(summaryDf)
    groupedDf.show(10)
  }

  test("timeUsageSummaryTyped") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)
    val summaryDf = TimeUsage.timeUsageSummary(primary, working, other, df.sample(withReplacement = false, 0.1))
    val typedDf = TimeUsage.timeUsageSummaryTyped(summaryDf)
    typedDf.show(10)
  }

  test("timeUsageGroupedTyped") {
    val (primary, working, other) = TimeUsage.classifiedColumns(headerColumns)
    val summaryDf = TimeUsage.timeUsageSummary(primary, working, other, df.sample(withReplacement = false, 0.1))
    val typedDf = TimeUsage.timeUsageSummaryTyped(summaryDf)
    val groupedTypedDf = TimeUsage.timeUsageGroupedTyped(typedDf)
    groupedTypedDf.show(10)
  }

}
