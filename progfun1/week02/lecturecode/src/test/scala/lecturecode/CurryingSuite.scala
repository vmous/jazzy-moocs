package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CurryingSuite extends FunSuite {

  import Currying._

  test("sum of integers between 1 and 5 with currying") {
    /*
     * - sum(id) applies sum to id and returns the sum of ids function
     * - sum(id) is therefore equivalent to sumCubes
     * - the sum of ids function is next applied to the arguments (1, 5)
     * 
     * Generally, function application associates to the left
     * sum(id)(1, 5) == (sum(id))(1, 5)
     */
    assert(sum(id)(1, 5) === 15)
  }

  test("sum of integers between -3 and 3 with currying") {
    assert(sum(id)(-3, 3) === 0)
  }

  test("sum of cubes of integers between 1 and 5 with currying") {
    assert(sum(cube)(1, 5) === 225)
  }

  test("sum of cubes of integers between -3 and 3 with currying") {
    assert(sum(cube)(-3, 3) === 0)
  }

  test("sum of factorials between 1 and 5 with currying") {
    assert(sum(factorial_tailrec)(1, 5) === 153)
  }

  test("sum of factorials between -3 and 3 with currying") {
    assert(sum(factorial_tailrec)(-3, 3) === 10)
  }

}
