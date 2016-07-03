package lecturecode

import math.abs

object FixedPoint {
  val tolerance = 0.0001

  def isCloseEnough(x: Double, y: Double) = {
    abs((x - y) / x) / x < tolerance
  }

  def fixedPoint(f: Double => Double)(firstGuess: Double) = {
    def iterate(guess: Double): Double = {
      val next = f(guess)
      if (isCloseEnough(guess, next)) next
      else iterate(next)
    }
    iterate(firstGuess)
  }

  /* Specification of the square root function:
   *     sqrt(x) = the number y such that y * y = x
   * or by dividing both sides of the equation with y:
   *     sqrt(x) = the number y such that y = x / y
   * Concequently, sqrt(x) is a fixed point of the function (y => x / y)
   */

  /** Stabilizing by averaging. */
  def averageDamp(f: Double => Double)(x: Double) = (x + f(x)) / 2

  /**
    * Square root function implemented as a fixed point.
    * 
    * Note that the function we pass as argument to fixedPoint is
    *     y => (y + x / y) / 2
    * and not simply:
    *     y => x / y
    * If you use the later you test it with sqrt(2) you will see that is not
    * converging as it is oscillating between values 1 and 2.
    * To control these oscillations that we were seeing is to prevent the
    * estimation from varying to much. One way to do this is by averaging
    * successive values of the original sequence. So instead of going 1,2, 1,2
    * we take the average of two successive values that would give us 1.5 and
    * that would set us on the right path to convergence.
    */
  def sqrt(x: Double): Double = fixedPoint(averageDamp(y => x / y))(1.0)

}
