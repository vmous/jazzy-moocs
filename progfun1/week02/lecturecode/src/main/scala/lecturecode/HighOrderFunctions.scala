package lecturecode

object HighOrderFunctions {

  /** Summation function */
  def sum(f: Int => Int, a: Int, b: Int): Int = {
    if (a > b) 0 else f(a) + sum(f, a + 1, b)
  }

  /** Take the sum of the integers between a and b. */
  def sumInts(a: Int, b: Int): Int = {
    def id(x: Int): Int = x

    sum(id, a, b)
  }

  /** Take the sum of cubes of integers between a and b. */
  def sumCubes(a: Int, b: Int): Int = {
    def cube(x: Int): Int = x * x * x

    sum(cube, a, b)
  }

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
