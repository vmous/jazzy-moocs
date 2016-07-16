package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ClassHierarchiesSuite extends FunSuite {

  import ClassHierarchies._

  test("testing is empty") {
    val t9 = Empty
    val t10 = new NonEmpty(0, Empty, Empty)
    assert(t9 isEmpty)
    assert(!(t10 isEmpty))
  }

  test("testing include, exclude and contains") {
    val t1 = new NonEmpty(3, Empty, Empty)
    val t2 = t1 incl 4
    val t8 = t2 excl 3
    assert(t1 contains 3)
    assert(!(t1 contains 4))
    assert(t2 contains 3)
    assert(t2 contains 4)
    assert(!(t8 contains 3))
    assert(t8 contains 4)
  }

  test("testing union") {
    val t3 = new NonEmpty(7, new NonEmpty(2, Empty, Empty), new NonEmpty(8, Empty, Empty))
    val t4 = new NonEmpty(3, new NonEmpty(2, Empty, Empty), new NonEmpty(5, Empty, Empty))
    assert((t3 union t4) contains 5)
    assert(!((t3 union t4) contains 10))
  }

  test("testing intersection") {
    val t5 = new NonEmpty(4, new NonEmpty(2, Empty, Empty), new NonEmpty(9, new NonEmpty(7, Empty, Empty), Empty))
    val t6 = new NonEmpty(3, Empty, new NonEmpty(4, Empty, Empty))
    val t7 = new NonEmpty(3, new NonEmpty(1, Empty, Empty), Empty)
    assert((t5 intersection t6) contains 4)
    println((t5 intersection t7) isEmpty)
  }

}
