package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExprSuite extends FunSuite {

  test("Evaluating Sum") {
    assert(Sum(Number(3), Number(7)).eval === 10)
    assert(Sum(Sum(Number(2), Number(1)), Number(7)).eval === 10)
  }

}
