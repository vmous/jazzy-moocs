package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FactorialSuite extends FunSuite {
 
  import Factorial._

  test("factorial of 4") {
    assert(factorial(4) === 24)
  }

  test("factorial of 4 with tail recursion") {
    assert(factorial_tailrec(4) === 24)
  }

}
