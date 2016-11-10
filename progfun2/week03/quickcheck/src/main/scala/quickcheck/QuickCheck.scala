package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    v <- arbitrary[A]
    h <- oneOf(const(empty), genHeap)
  } yield insert(v, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("min1") = forAll { v: A =>
    val h = insert(v, empty)
    findMin(h) == v
  }

  property("min2") = forAll { (v1: A, v2: A) =>
    val h = insert(v1, insert(v2, empty))
    findMin(h) == math.min(v1, v2)
  }

  property("gen1") = forAll { h: H =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("add-to-empty") = forAll { v: A =>
    val h = insert(v, empty)
    ! isEmpty(h)
  }

  property("delete-last") = forAll { v: A =>
    val h = deleteMin(insert(v, empty))
    isEmpty(h)
  }

  property("delete3") = forAll { (a: Int, b: Int, c: Int) =>
    val h = insert(c, insert(b, insert(a, empty)))
    val rh = deleteMin(deleteMin(h))
    findMin(rh) == math.max(math.max(a, b), c)
  }

  property("meld-empty-empty") = {
    val h = meld(empty, empty)
    isEmpty(h)
  }

  property("meld-nonempty-empty") = forAll { some: H =>
    val h = meld(some, empty)
    ! isEmpty(h)
  }

  property("meld-empty-nonempty") = forAll { some: H =>
    val h = meld(empty, some)
    ! isEmpty(h)
  }

  property("meld-minimum") = forAll { (h1: H, h2: H) =>
    val min1 = findMin(h1)
    val min2 = findMin(h2)

    findMin(meld(h1, h2)) == math.min(min1, min2)
  }

  property("always-ordered") = forAll { h: H =>
    checkOrder(h)
  }

  def checkOrder(h: H): Boolean = {
    def isBigger(prev: A, h: H): Boolean = {
      if (isEmpty(h)) true
      else {
        val cur = findMin(h)
        if (ord.gteq(cur, prev))
          isBigger(cur, deleteMin(h))
        else false
      }
    }

    if (isEmpty(h)) true
    else isBigger(findMin(h), deleteMin(h))
  }

  property("size") = forAll { (h1: H, h2: H) =>
    size(meld(h1, h2)) == size(h1) + size(h2)
  }

  def size(h: H): Int = {
    def getSize(h: H, n: Int): Int = {
      if (isEmpty(h)) n else getSize(deleteMin(h), n + 1)
    }
    getSize(h, 0)
  }

}
