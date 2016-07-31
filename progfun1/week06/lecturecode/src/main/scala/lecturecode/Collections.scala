package lecturecode

object Collections {

  /**
    * List of all combination of of numbers x and y where x is drawn from 1..M
    * and y is drawn from 1..N.
    */
  def combinations(xr: Range, yr: Range) = {
    xr flatMap (x =>
      yr map (y => (x, y))
    )
  }

  /**
    * Scalar product of vectors.
    */
  def scalarProduct(xs: Vector[Double], ys: Vector[Double]): Double = {
    (xs zip ys).map(xy => xy._1 * xy._2).sum
  }

  /**
    * Scalar product of vectors using pair pattern matching.
    */
  def scalarProductPatmat(xs: Vector[Double], ys: Vector[Double]): Double = {
    (xs zip ys).map{ case (x, y) => x * y }.sum
  }

  /**
    * A number is prime if the only divisors of n are 1 and n itself.
    */
  def isPrime(n: Int): Boolean = (2 until n) forall (d => n % d != 0)

}
