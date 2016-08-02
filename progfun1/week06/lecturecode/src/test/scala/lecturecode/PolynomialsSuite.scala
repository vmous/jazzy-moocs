package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PolynomialsSuite extends FunSuite {

  import Polynomials._

  test("testing polynomials") {
    val p1 = new Poly(1 -> 2.0, 3 -> 4.0, 5 -> 6.2)
    val p2 = new Poly(0 -> 3.0, 3 -> 7.0)
    assert((p1 + p2).toString() === "6.2x^5 + 11.0x^3 + 2.0x^1 + 3.0x^0")
  }

}
