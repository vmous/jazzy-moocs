package lecturecode

object GreatestCommonDivisor {

  /** Calculates the greatest common divisor of two numbers.
    * 
    * Implements the classical method using Euclid's method.
    * 
    * @param a One number.
    * @param b The other number.
    * 
    * @return The greates common devisor of the two parameters.
    */
  def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }
}
