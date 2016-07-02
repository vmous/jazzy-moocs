package lecturecode

object Currying {

  /* Application functions */

  /** Identification function */
  def id(x: Int): Int = x

  /** Cube function */
  def cube(x: Int): Int = x * x * x

  /** Tail recursive factorial function. */
  def factorial_tailrec(n: Int): Int = {
    def loop(acc: Int, n: Int): Int = {
      if (n == 0) acc
      else loop(acc * n, n - 1)
    }
    loop(1, n)
  }

  /** Summation function */
  def sum(f: Int => Int, a: Int, b: Int): Int = {
    if (a > b) 0 else f(a) + sum(f, a + 1, b)
  }

  /*
   * Note the repetition below:
   * a and b get passed unchanged from sumInts and sumCubes and sumFactorials
   * into sum.
   * Can we be even shorter by getting rid of these parameters?
   */

  /** Take the sum of the integers between a and b. */
  def sumInts(a: Int, b: Int): Int = sum(id, a, b)

  /** Take the sum of cubes of integers between a and b. */
  def sumCubes(a: Int, b: Int): Int = sum(cube, a, b)

  /** Take the sum of factorials between a and b. */
  def sumFactorials(a: Int, b: Int): Int = sum(factorial_tailrec, a, b)

}
