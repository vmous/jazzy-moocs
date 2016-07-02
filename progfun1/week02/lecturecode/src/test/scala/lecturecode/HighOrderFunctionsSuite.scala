package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HighOrderFunctionsSuite extends FunSuite {
 
  import HighOrderFunctions._

  test("sum of integers between 1 and 5") {
    assert(sumInts(1, 5) === 15)
  }

  test("sum of integers between -3 and 3") {
    assert(sumInts(-3, 3) === 0)
  }

  test("sum of cubes of integers between 1 and 5") {
    assert(sumCubes(1, 5) === 225)
  }

  test("sum of cubes of integers between -3 and 3") {
    assert(sumCubes(-3, 3) === 0)
  }

  test("sum of factorials between 1 and 5") {
    assert(sumFactorials(1, 5) === 153)
  }

  test("sum of factorials between -3 and 3") {
    assert(sumFactorials(-3, 3) === 10)
  }

}
