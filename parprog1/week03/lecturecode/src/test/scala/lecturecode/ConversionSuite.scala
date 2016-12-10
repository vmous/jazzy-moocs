package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConversionSuite extends FunSuite {

  import Conversion._

  val array = (0 until 1000000).toArray

  test("testing sum") {
    assert(sum(array) === 1783293664)
  }

  test("testing max") {
    assert(max(array) === 999999)
  }

}
