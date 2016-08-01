package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CombinatorialsSuite extends FunSuite {

  import Combinatorials._

  test("testing findinterestingpairs") {
    assert(findinterestingpairs(7) === List((2, 1), (3, 2), (4, 1), (4, 3), (5, 2), (6, 1), (6, 5)))
  }

  test("testing scalar product (for-expression)") {
    assert(scalarProductForExp(Vector(2, 4), Vector(6, 8)) === 44)
  }

}
