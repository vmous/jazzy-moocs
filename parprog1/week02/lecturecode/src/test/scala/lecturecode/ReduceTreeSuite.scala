package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ReduceTreeSuite extends FunSuite {

  import ReduceTree._

  test("testing reducing simple tree") {
    def tree = Node(Leaf(1), Node(Leaf(3), Leaf(8)))
    def fMinus = (x: Int, y: Int) => x - y
    def seqRes = reduceTreeSeq[Int](tree, fMinus)
    def parRes = reduceTreePar[Int](tree, fMinus)
    assert(seqRes === 6)
    assert(seqRes === parRes)
  }

}
