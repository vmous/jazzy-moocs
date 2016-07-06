package lecturecode

import math.abs

object Rational {

  /**
    * Class abstraction of a rational number represented by
    * a numerator and a denominator.
    */
  class Rational(x: Int, y: Int) {
    require(y != 0, "denominator must be non-zero")

    def this(x: Int) = this(x, 1)

    private def gcd(a: Int, b: Int): Int = {
      val a_abs = abs(a)
      if (b == 0) a_abs else gcd(b, a_abs % b)
    }
    private val g = gcd(x, y)
    def numer = x / g
    def denom = y / g

    def less(that: Rational): Boolean = numer * that.denom < that.numer * denom

    def max(that: Rational): Rational = if (this.less(that)) that else this

    def neg(): Rational = new Rational(-numer, denom)

    def add(that: Rational): Rational = {
      new Rational(
        numer * that.denom + that.numer * denom,
        denom * that.denom
      )
    }

    def sub(that: Rational): Rational = add(that.neg)

    override def toString(): String = numer + "/" + denom
  }

}
