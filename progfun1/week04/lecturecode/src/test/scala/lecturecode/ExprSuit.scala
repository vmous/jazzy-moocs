package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExprSuite extends FunSuite {

  test("Evaluating Sum") {
    assert(new Sum(new Number(3), new Number(7)).eval === 10)
    assert(new Sum(new Sum(new Number(2), new Number(1)), new Number(7)).eval === 10)
  }

}
