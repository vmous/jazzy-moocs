package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FixedPointSuite extends FunSuite {

  import FixedPoint._

  test("fixed point for y = 1 + x / 2") {
    assert(fixedPoint(x => 1 + x / 2)(1) === 1.999755859375)
  }

}
