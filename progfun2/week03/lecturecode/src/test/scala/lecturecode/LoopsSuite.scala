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
      x = x + 1
      i = i - 1
    }

    assert(x == 3)
  }

  test("Testing repeat loop") {
    var i = 3
    var x = 0
    REPEAT {
      x = x + 1
      i = i - 1
    } (i == 0)

    assert(x == 3)
  }

}
