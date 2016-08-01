package lecturecode

object Combinatorials {

  /**
    * Given a positive number n, find all pairs of possible integers i and j,
    * with 1 <= j < i < n such that i + j is prime.
    * For example, if n = 7, the sought pairs are:
    *
    *     i  |2 3 4 4 5 6 6
    *     j  |1 2 1 3 2 1 5
    *   -----|--------------
    *    i+j |3 5 5 7 7 7 11
    */
  def findinterestingpairs(n: Int) = {
    if (n < 0) throw new Error("n.negative")

    def isPrime(n: Int): Boolean = (2 until n) forall (n % _ != 0)

    for {
      i <- (1 until n)
      j <- (1 until i)
      if isPrime(i + j)
    } yield (i, j)
  }

}
