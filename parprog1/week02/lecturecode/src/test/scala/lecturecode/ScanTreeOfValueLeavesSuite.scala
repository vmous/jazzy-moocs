package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScanTreeOfValueLeavesSuite extends FunSuite {

  import ScanTreeOfValueLeaves._

  val t = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
  val a0 = 100
  val plus = { (x: Int, y: Int) => x + y }

  test("testing creation of intermediate values tree") {
    assert(upsweep(t, plus) === NodeRes(NodeRes(LeafRes(1), 4, LeafRes(3)), 62, NodeRes(LeafRes(8), 58, LeafRes(50))))
  }

  test("testing creation of the scan left suffix") {
    val tr = upsweep(t, plus)
    assert(downsweep(tr, a0, plus) === Node(Node(Leaf(101), Leaf(104)), Node(Leaf(112), Leaf(162))))
  }

  test("testing scan left") {
    assert(scanLeft(t, a0, plus) === Node(Node(Node(Leaf(100), Leaf(101)), Leaf(104)), Node(Leaf(112), Leaf(162))))
  }

}
