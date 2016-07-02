package lecturecode

object SquareRoot {

  def abs(x: Double): Double = if (x >= 0) x else -x

  /** Calculates the square root.
    * 
    * Implement the classical method of successive approximations using
    * Newton's method.
    * 
    * @param x The parameter whose square root to compute.
    * 
    * @return The square root of the parameter.
    */
  def sqrt(x: Double): Double = sqrtIter(1.0, x)

  def sqrtIter(guess: Double, x: Double): Double = {
    if (isGoodEnough(guess, x)) guess
    else sqrtIter(improve(guess, x), x)
  }

  def isGoodEnough(guess: Double, x: Double): Boolean = {
    abs(guess * guess - x) < 0.001
  }

  def improve(guess: Double, x: Double) = {
    (guess + x / guess) / 2
  }

}
