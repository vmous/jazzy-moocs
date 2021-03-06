package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PNormSuite extends FunSuite {

  import PNorm._

  test("testing pnorm") {
    assert(PNorm.pNorm(Array(1, 1), 2.0) === scala.math.sqrt(2.0))
  }

  test("testing pnorm with two parts") {
    assert(PNorm.pNormTwoParts(Array(1, 1), 2.0) === scala.math.sqrt(2.0))
  }

  test("testing pnorm with two parallel parts") {
    assert(PNorm.pNormTwoParallelParts(Array(1, 1), 2.0) === scala.math.sqrt(2.0))
  }

  test("testing pnorm with as many parallel parts as needed") {
    assert(PNorm.pNormRecursive(Array(1, 1, 1, 1, 1), 2.0) === 2.23606797749979)
  }

}
