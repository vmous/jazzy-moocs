package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SumProductSuite extends FunSuite {

  import SumProduct._

  test("product of squares of integers between 3 and 4") {
    assert(product(x => x * x)(3, 4) === 144)
  }

  test("factorial using product") {
    assert(factorial(5) === 120)
  }

}
