package lecturecode

object Primes {

  /**
   * Stream of integers starting from a given number.
   */
  def from(x: Int): Stream[Int] = x #:: from(x + 1)

}
