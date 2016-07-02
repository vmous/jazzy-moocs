package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CurryingSuite extends FunSuite {

  import Currying._

  test("sum of integers between 1 and 5 with currying") {
    assert(sumInts(1, 5) === 15)
  }

  test("sum of integers between -3 and 3 with currying") {
    assert(sumInts(-3, 3) === 0)
  }

  test("sum of cubes of integers between 1 and 5 with currying") {
    assert(sumCubes(1, 5) === 225)
  }

  test("sum of cubes of integers between -3 and 3 with currying") {
    assert(sumCubes(-3, 3) === 0)
  }

  test("sum of factorials between 1 and 5 with currying") {
    assert(sumFactorials(1, 5) === 153)
  }

  test("sum of factorials between -3 and 3 with currying") {
    assert(sumFactorials(-3, 3) === 10)
  }

}
