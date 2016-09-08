package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ForExprTransSuite extends FunSuite {

  val lst_1 = List(1, 2, 3, 4, 5)
  val lst_2 = List(6, 7, 8, 9, 0)
  def g(x: Int) = x * x
  def filter(x: Int) = if(x % 2 == 0) true else false

  test("testing simple \"for\"-expression") {
    val f = for (x <- lst_1) yield g(x)
    val t = lst_1.map(x => g(x))
    assert(f === t)
  }

  test("testing \"for\"-expression with one generator followed by a filter") {
    val f = for (x <- lst_1 if filter(x)) yield g(x)
    val t = for (x <- lst_1.withFilter(x => filter(x))) yield g(x)
    assert(f === t)
  }

  test("testing \"for\"-expression with one generator followed by another generator") {
    val f = for (x <- lst_1; y <- lst_2) yield g(x + y)
    val t = lst_1.flatMap(x => for (y <- lst_2) yield g(x + y))
    println(f)
    println(t)
    assert(f === t)
  }

}
