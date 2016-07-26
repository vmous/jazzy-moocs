package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExprSuite extends FunSuite {

  val expr1 = Sum(Number(3), Number(7))
  val expr2 = Sum(Sum(Number(2), Number(1)), Number(7))

  test("Evaluating Sum") {
    assert(expr1.eval === 10)
    assert(expr2.eval === 10)
  }

  test("Showing Sum") {
    assert(expr1.show === "3 + 7")
    assert(expr2.show === "2 + 1 + 7")
  }

}
