package lecturecode

import scala.language.postfixOps
import scala.math.Ordering

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class ListFunctionsSuite extends FunSuite {

  import ListFunctions._

  val lst_empty = List()
  val lst_monad = List(1)
  val lst_normal = List(1, 2 , 3)
  val lst_any = List(List(1, 1), 2, List(3, List(5, 8)))
  val lst_unsort_int = List(4, 7, 6, 3, 1)
  val lst_unsort_str = List("pear", "peach", "nectarine", "watermelon", "melon")
  val lst_doubles = List(1.0, 2.0, 3.0)
  val lst_posneg = List(-1, 1, -2, 2, -3, 3)

  test("testing head") {
    intercept[Error]{ head(lst_empty) }
    assert(head(lst_monad) === 1)
    assert(head(lst_normal) === 1)
  }

  test("testing tail") {
    intercept[Error] { tail(lst_empty) }
    assert(tail(lst_monad) === List())
    assert(tail(lst_normal) === List(2, 3))
  }

  test("testing last") {
    intercept[Error] { last(lst_empty) }
    assert(last(lst_monad) === 1)
    assert(last(lst_normal) === 3)
  }

  test("testing init") {
    intercept[Error] { init(lst_empty) }
    assert(init(lst_monad) === List())
    assert(init(lst_normal) === List(1, 2))
  }

  test("testing concat") {
    assert(concat(lst_monad, lst_monad) === List(1, 1))
    assert(concat(lst_empty, lst_normal) === List(1, 2, 3))
    assert(concat(lst_normal, lst_empty) === List(1, 2, 3))
  }

  test("testing reverse") {
    assert(reverse(lst_empty) === Nil)
    assert(reverse(lst_monad) === List(1))
    assert(reverse(lst_normal) === List(3, 2, 1))
  }

  test("testing remove at") {
    assert(removeAt(lst_empty, 1) === Nil)
    assert(removeAt(lst_empty, 0) === Nil)
    assert(removeAt(lst_normal, 1) === List(1, 3))
    assert(removeAt(lst_normal, 4) === List(1, 2, 3))
  }

  test("testing flatten") {
    assert(flatten(lst_any) === List(1, 1, 2, 3, 5, 8))
  }

  test("testing sorting") {
    jazzyinsertsort(lst_unsort_int).toSeq should equal (List(1, 3, 4, 6, 7).toSeq)
    jazzymergesort(lst_unsort_int).toSeq should equal (List(1, 3, 4, 6, 7).toSeq)
    jazzyinsertsort(lst_unsort_str).toSeq should equal (List("melon", "nectarine", "peach", "pear", "watermelon").toSeq)
    jazzymergesort(lst_unsort_str).toSeq should equal (List("melon", "nectarine", "peach", "pear", "watermelon").toSeq)

  }

  test("testing scale list") {
    assert(scaleList(lst_doubles, 3) === List(3.0, 6.0, 9.0))
  }

  test("testing positive elements") {
    assert(posElems(lst_posneg) === List (1, 2, 3))
  }

}
