package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParallelMapSuite extends FunSuite {

  import ParallelMap._

  test("testing mapping a segment sequentially using array") {
    val in = Array(2, 3, 4, 5, 6)
    val out = Array(0, 0, 0, 0, 0)
    val f = (x: Int) => x * x

    mapASegSeq(in, 1, 3, f, out)
    assert(out === Array(0, 9, 16, 0, 0))
  }

  test("testing mapping a segment parallely using array") {
    val in = Array(2, 3, 4, 5, 6)
    val out = Array(0, 0, 0, 0, 0)
    val f = (x: Int) => x * x

    mapASegPar(in, 1, 3, f, out)
    assert(out === Array(0, 9, 16, 0, 0))
  }

}
