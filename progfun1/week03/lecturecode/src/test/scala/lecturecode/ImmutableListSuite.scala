package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ImmutableListSuite extends FunSuite {

  import ImmutableList._

  test("testing singleton") {
    val single = singleton(1)
    assert(single.head === 1)
    assert(single.tail isEmpty)
  }

  test("testing nth") {
    val list = new Cons(1, new Cons(4, new Cons(8, new Nil)))
    assert(nth(2, list) == 8)
  }

}
