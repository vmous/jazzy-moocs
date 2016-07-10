package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ClassHierarchiesSuite extends FunSuite {

  import ClassHierarchies._

  test("testing include and contains") {
    val t1 = new NonEmpty(3, Empty, Empty)
    val t2 = t1 incl 4
    assert(t1 contains 3)
    assert(!(t1 contains 4))
    assert(t2 contains 3)
    assert(t2 contains 4)
  }

}
