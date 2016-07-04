package lecturecode

object Rational {

  /**
    * Class abstraction of a rational number represented by
    * a numerator and a denominator.
    */
  class Rational(x: Int, y: Int) {
    def numer = x
    def denom = y

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
