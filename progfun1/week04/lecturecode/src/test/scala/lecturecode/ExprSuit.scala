package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExprSuite extends FunSuite {

  val num_expr = Number(3)
  val var_expr = Variable("x")
  val sum_expr1 = Sum(Number(3), Number(7))
  val sum_expr2 = Sum(Sum(Number(2), Number(1)), Number(7))
  val prod_expr1 = Prod(Number(2), Number(5))
  val prod_expr2 = Prod(Prod(Number(1), Number(5)), Number(2))
  val complex_expr1 = Prod(Sum(Number(2), Variable("x")), Number(5))
  val complex_expr2 = Sum(Prod(Number(2), Variable("x")), Number(5))
  val complex_expr3 = Sum(Number(2), Prod(Variable("x"), Number(5)))

  test("Evaluating Number") {
    assert(num_expr.eval === 3)
  }

  test("Evaluating Variable") {
    intercept[Error] {
      var_expr.eval
    }
  }

  test("Evaluating Sum") {
    assert(sum_expr1.eval === 10)
    assert(sum_expr2.eval === 10)
  }

  test("Evaluating Prod") {
    assert(prod_expr1.eval === 10)
    assert(prod_expr2.eval === 10)
  }

  test("Showing Sum") {
    assert(sum_expr1.show === "3 + 7")
    assert(sum_expr2.show === "2 + 1 + 7")
  }

  test("Showing Prod") {
    assert(prod_expr1.show === "2 * 5")
    assert(prod_expr2.show === "1 * 5 * 2")
  }

  test("Showing complex combinations of Sum and Prod") {
    assert(complex_expr1.show === "(2 + x) * 5")
    assert(complex_expr2.show === "2 * x + 5")
    assert(complex_expr3.show === "2 + x * 5")
  }

}
