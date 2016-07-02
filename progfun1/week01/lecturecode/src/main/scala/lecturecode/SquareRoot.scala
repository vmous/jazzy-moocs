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

  /**
    * We need to keep the abs(guess * guess - x) proportional to x because
    * else
    *  - for very small numbers number whose square root we need to find might
    *    be even smaller than the threshold value 0.001. So, relatively
    *    speaking, that epsilon value might be huge compared to the number we
    *    want to find.
    *  - for very large numbers we have the opposite problem, which is that very
    *    large floating point numbers can actually be further apart than this
    *    epsilon value. That means in the 52-bits, the system has available for
    *    mantissa, tt could be that the distance between one number and the next
    *    is actually larger than 0.001. And in that case, of course the
    *    iteration can never stop because, simply because there is no value
    *    that's ever good enough.
    */
  def isGoodEnough(guess: Double, x: Double): Boolean = {
    abs(guess * guess - x) / x < 0.001
  }

  def improve(guess: Double, x: Double): Double = {
    (guess + x / guess) / 2
  }

}
