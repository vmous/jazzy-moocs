package lecturecode

object Primes {

  /**
   * Stream of integers starting from a given number.
   */
  def from(x: Int): Stream[Int] = x #:: from(x + 1)

  /**
    * Algorithm for Sieve of Eratosthenes:
    * https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
   */
  def sieve(s: Stream[Int]): Stream[Int] =
    s.head #:: sieve(s.tail filter(_ % s.head != 0))

}
