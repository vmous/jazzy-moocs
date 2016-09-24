package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SquareRootStreamSuite extends FunSuite {
 
  import SquareRootStream._

  test("square root of 2") {
    assert(sqrtStream(2).take(4).toList.last === 1.4142156862745097)
  }

  test("square root of 4") {
    assert(sqrtStream(4).take(4).toList.last ===  2.000609756097561)
  }

  test("square root of 0.001") {
    assert(sqrtStream(0.001).take(9).toList.last === 0.03162278245070105)
  }

  test("square root of 1.0e-20") {
    assert(sqrtStream(1.0e-20).take(37).toList.last ===  1.0000021484861236E-10)
  }

  test("square root of 1.0e20") {
    assert(sqrtStream(1.0e20).take(37).toList.last === 1.0000021484861237E10)
  }

  test("square root of 1.0e60") {
    assert(sqrtStream(1.0e60).take(103).toList.last ===  1.0000788456669446E30)
  }

}
