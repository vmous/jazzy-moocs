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
    * 
    * In general:
    *     def f(args_1)...(args_n) = E
    * where n > 1, is equivalent to:
    *     def f(args_1)...(args_n-1) = { def g(args_n) = E; g }
    * or for short:
    *     def f(args_1)..(args_n-1) = (args_n => E)
    * and repeating that expansion n times we get the currying defition
    *     def f = (args_1 => (args_2 => ...(args_n => E)...))
    */
  def sum(f: Int => Int): (Int, Int) => Int = {
    def sumFunction(a: Int, b: Int): Int = {
      if (a > b) 0
      else f(a) + sumFunction(a + 1, b)
    }
    sumFunction
  }

  /* Note there is no need for the "middlemen" functions at all!
   * Check the application in the respective scalatest class for a detailed
   * explanation of why this is the case.
   */

}
