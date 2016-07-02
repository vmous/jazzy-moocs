package lecturecode

object HighOrderFunctions {

  /** Take the sum of the integers between a and b. */
  def sumInts(a: Int, b: Int): Int = {
    if (a > b) 0 else a + sumInts(a + 1, b)
  }

  /** Take the sum of cubes of integers between a and b. */
  def sumCubes(a: Int, b: Int): Int = {
    def cube(x: Int): Int = x * x * x

    if (a > b) 0 else cube(a) + sumCubes(a + 1, b)
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

    if (a > b) 0 else factorial_tailrec(a) + sumFactorials(a + 1, b)
  }

}
