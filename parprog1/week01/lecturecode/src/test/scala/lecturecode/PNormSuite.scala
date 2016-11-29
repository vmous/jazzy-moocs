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

  test("testing pnorm with two part") {
    assert(PNorm.pNormTwoParts(Array(1, 1), 2.0) === scala.math.sqrt(2.0))
  }

}
