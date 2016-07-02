package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GreatestCommonDivisorSuite extends FunSuite {
 
  import GreatestCommonDivisor._

  test("greatest common divisor of 14 and 21") {
    assert(gcd(14, 21) === 7)
  }

}
