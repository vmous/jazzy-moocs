package lecturecode

object Factorial {

  /** Calculates the factorial of a number.
    * 
    * @param n The number.
    * 
    * @return The given number's factorial
    */
  def factorial(n: Int): Int = {
    if (n == 0) 1 else n * factorial(n - 1)
  }
}
