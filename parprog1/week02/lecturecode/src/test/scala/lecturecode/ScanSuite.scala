package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScanSuite extends FunSuite {

  import Scan._

  test("testing sequential scan left") {
    val in = Array(1, 2, 3, 4)
    val out = Array(0, 0, 0, 0, 0)
    scanLeftSeq(in, 100, (x: Int, y: Int) => x + y, out)
    assert(out === Array(100, 101, 103, 106, 110))
  }

  test("testing parallel scan left (with map/reduce)") {
    val in = Array(1, 2, 3, 4)
    val out = Array(0, 0, 0, 0, 0)
    val action = { (i: Int, v: Int) =>  }
    scanLeftMR(in, 100, (x: Int, y: Int) => x + y, out)
    assert(out === Array(100, 101, 103, 106, 110))
  }

}
