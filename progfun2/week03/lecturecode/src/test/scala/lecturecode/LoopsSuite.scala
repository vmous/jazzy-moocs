package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LoopsSuite extends FunSuite {

  import Loops._

  test("Testing while loop") {
    var i = 3
    var x = 0
    WHILE(i > 0) {
      i = i - 1
      x = x + 1
    }

    assert(x == 3)
  }

}
