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

  /** Calculates the factorial of a number.
    * 
    * Implementation is tail recursive.
    * 
    * @param n The number.
    * 
    * @return The given number's factorial
    */
  def factorial_tailrec(n: Int): Int = {
    def loop(acc: Int, n: Int): Int = {
      if (n == 0) acc
      else loop(acc * n, n - 1)
    }
    loop(1, n)
  }

}
