package lecturecode

object HighOrderFunctions {

  /** Tail recursive summation function. */
  def sum(f: Int => Int, a: Int, b: Int): Int = {
    def loop(a: Int, acc: Int): Int = {
      if (a > b) acc
      else loop(a + 1, acc + f(a))
    }
    loop(a, 0)
  }

  /** Take the sum of the integers between a and b. */
  def sumInts(a: Int, b: Int): Int = sum(x => x, a, b)

  /** Take the sum of cubes of integers between a and b. */
  def sumCubes(a: Int, b: Int): Int = sum(x => x * x * x, a, b)

  /** Take the sum of factorials between a and b. */
  def sumFactorials(a: Int, b: Int): Int = {
    /** Tail recursive factorial computation. */
    def factorial_tailrec(n: Int): Int = {
      def loop(acc: Int, n: Int): Int = {
        if (n == 0) acc
        else loop(acc * n, n - 1)
      }
      loop(1, n)
    }

    sum(factorial_tailrec, a, b)
  }

}
