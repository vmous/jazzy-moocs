package lecturecode

object SumProduct {

  /** Product function. */
  def product(f: Int => Int)(a: Int, b: Int): Int = {
    if (a > b) 1
    else f(a) * product(f)(a + 1, b)
  }

  /** Factorial based on product function. */
  def factorial(n: Int): Int = product(x => x)(1, n)

}
