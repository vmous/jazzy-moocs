package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ReduceTreeSuite extends FunSuite {

  import ReduceTree._

  def tree = Node(Leaf(1), Node(Leaf(3), Leaf(8)))

  test("testing reducing simple tree") {
    def fMinus = (x: Int, y: Int) => x - y
    def seqRes = reduceTreeSeq[Int](tree, fMinus)
    def parRes = reduceTreePar[Int](tree, fMinus)
    assert(seqRes === 6)
    assert(seqRes === parRes)
  }

  test("testing toList for trees") {
    assert(toList(tree) === List(1, 3, 8))
  }

  test("testing equality of toList and its MapReduce expression") {
    def lst = (x: Int) => List(x)
    def conc = (x: List[Int], y: List[Int]) => x ++ y
    assert(toList(tree) === reduceTreePar[List[Int]](mapTreePar(tree, lst), conc))
  }

}
