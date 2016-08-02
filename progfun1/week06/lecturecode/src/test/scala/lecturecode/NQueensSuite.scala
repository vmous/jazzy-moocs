package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NQueensSuite extends FunSuite {

  import NQueens._

  test("testing nqueens") {
    assert(queens(4) === Set(List(1, 3, 0, 2), List(2, 0, 3, 1)))
  }

}
