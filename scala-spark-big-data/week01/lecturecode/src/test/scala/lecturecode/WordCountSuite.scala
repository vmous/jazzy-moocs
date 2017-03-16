package lecturecode

import org.apache.spark.{ SparkConf, SparkContext }
import org.scalatest.{ BeforeAndAfterAll, FunSuite }

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WordCountSuite extends FunSuite with BeforeAndAfterAll {
  private var sConf: SparkConf = _
  private var sc: SparkContext = _

  import WordCount._

  override def beforeAll(): Unit = {
    sConf = new SparkConf().setAppName("unit-testing").setMaster("local")
    sc = new SparkContext(sConf)
  }

  test("word count") {
    val counts = wordCount("./src/test/resources/README.md", sc).collect().toList
    assert(counts.size == 287)
    assert(counts.aggregate(0)( (acc, e) => acc + e._2, _ + _ ) === 565)
  }

  override def afterAll(): Unit = {
    sc.stop()
  }

}

