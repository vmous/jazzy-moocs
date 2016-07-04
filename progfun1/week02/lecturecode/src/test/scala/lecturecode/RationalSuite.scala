package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RationalSuite extends FunSuite {
 
  import Rational._

  val x = new Rational.Rational(1, 3)
  val y = new Rational.Rational(5, 7)
  val z = new Rational.Rational(3, 2)

  test("negative 1/3") {
    val res = x.neg
    assert(res.numer === -1 && res.denom === 3)    
  }

  test("add 1/3 and 5/7") {
    val res = x.add(y)
    assert(res.numer === 22 && res.denom === 21)
  }

  test("subtract 1/3, 5/7, 3/2") {
    val res = x.sub(y).sub(z)
    assert(res.numer === -79 && res.denom === 42)
  }

}
