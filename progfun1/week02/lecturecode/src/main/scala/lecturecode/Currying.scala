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

  /**
    * Summation function.
    * 
    * The summation function now does not define the interval parameters.
    * It just returns a function that is defined internally.
    * Note that the return type is (Int, Int) => Int which means it is a
    * function that takes two integers and returns one integer. The two
    * interger parameters defined in the return type are mapped to the
    * internal function's (sumFunction) arguments, which are the interval
    * parameters we eliminated from sum's signature. The returned function,
    * sumFunction, applies the given function parameter f and sums the
    * results.
    */
  def sum(f: Int => Int): (Int, Int) => Int = {
    def sumFunction(a: Int, b: Int): Int = {
      if (a > b) 0
      else f(a) + sumFunction(a + 1, b)
    }
    sumFunction
  }

  /* Note there is no argument repetition below anymore! */

  /** Take the sum of the integers between a and b. */
  def sumInts = sum(id)

  /** Take the sum of cubes of integers between a and b. */
  def sumCubes = sum(cube)

  /** Take the sum of factorials between a and b. */
  def sumFactorials = sum(factorial_tailrec)

}
