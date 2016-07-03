package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SumProductSuite extends FunSuite {

  import SumProduct._

  test("sum of cubes of integers between 1 and 5") {
    assert(sum(x => x * x * x)(1, 5) === 225)
  }

  test("product of squares of integers between 3 and 4") {
    assert(product(x => x * x)(3, 4) === 144)
  }

  test("factorial using product function") {
    assert(factorial(5) === 120)
  }

}
