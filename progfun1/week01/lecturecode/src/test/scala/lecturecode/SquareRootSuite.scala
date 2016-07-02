package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SquareRootSuite extends FunSuite {
 
  import SquareRoot._

  test("square root of 2") {
    assert(sqrt(2) === 1.4142156862745097)
  }

  test("square root of 4") {
    assert(sqrt(4) ===  2.0000000929222947)
  }

}
