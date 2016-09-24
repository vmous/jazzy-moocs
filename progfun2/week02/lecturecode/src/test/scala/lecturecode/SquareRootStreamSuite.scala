package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SquareRootStreamSuite extends FunSuite {
 
  import SquareRootStream._

  def assertGoodEnoughApprox(n: Double, approx: Double) =
    assert(sqrtStream(n).filter(isGoodEnough(_, n)).take(1).head === approx)

  test("square root of 2") {
    assertGoodEnoughApprox(2, 1.4142156862745097)
  }

  test("square root of 4") {
    assertGoodEnoughApprox(4, 2.0000000929222947)
  }

  test("square root of 0.001") {
    assertGoodEnoughApprox(0.001, 0.03162278245070105)
  }

  test("square root of 1.0e-20") {
    assertGoodEnoughApprox(1.0e-20, 1.0000021484861236E-10)
  }

  test("square root of 1.0e20") {
    assertGoodEnoughApprox(1.0e20, 1.0000021484861237E10)
  }

  test("square root of 1.0e60") {
    assertGoodEnoughApprox(1.0e60, 1.0000000031080746E30)
  }

}
