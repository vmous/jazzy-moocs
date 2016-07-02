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
    assert(sqrt(4) ===  2.000609756097561)
  }

  test("square root of 0.001") {
    assert(sqrt(0.001) === 0.03162278245070105)
  }

  test("square root of 1.0e-20") {
    assert(sqrt(1.0e-20) ===  1.0000021484861236E-10)
  }

  test("square root of 1.0e20") {
    assert(sqrt(1.0e20) === 1.0000021484861237E10)
  }

  test("square root of 1.0e60") {
    assert(sqrt(1.0e60) ===  1.0000788456669446E30)
  }

}
