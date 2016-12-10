package reductions

import org.scalameter._
import common._

object ParallelCountChangeRunner {

  @volatile var seqResult = 0

  @volatile var parResult = 0

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 20,
    Key.exec.maxWarmupRuns -> 40,
    Key.exec.benchRuns -> 80,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val amount = 250
    val coins = List(1, 2, 5, 10, 20, 50)
    val seqtime = standardConfig measure {
      seqResult = ParallelCountChange.countChange(amount, coins)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential count time: $seqtime ms")

    def measureParallelCountChange(threshold: ParallelCountChange.Threshold): Unit = {
      val fjtime = standardConfig measure {
        parResult = ParallelCountChange.parCountChange(amount, coins, threshold)
      }
      println(s"parallel result = $parResult")
      println(s"parallel count time: $fjtime ms")
      println(s"speedup: ${seqtime / fjtime}")
    }

    measureParallelCountChange(ParallelCountChange.moneyThreshold(amount))
    measureParallelCountChange(ParallelCountChange.totalCoinsThreshold(coins.length))
    measureParallelCountChange(ParallelCountChange.combinedThreshold(amount, coins))
  }
}

object ParallelCountChange {

  /** Returns the number of ways change can be made from the specified list of
   *  coins for the specified amount of money.
   *
   * Note that the solution to this problem is recursive. In every recursive
   * call, we either decide to continue subtracting the next coin in the coins
   * list from the money amount, or we decide to drop the coin from the list of
   * coins. For example, if we have 4 CHF, and coin denominations of 1 and 2,
   * the call graph, in which every node depicts one invocation of the
   * countChange method, is as follows:
   *
   *                                    4,[1, 2]
   *                      3,[1, 2]          +            4,[2]
   *              2,[1, 2]    +     3,[2]          2,[2]   +   4,[]
   *         1,[1, 2] + 2,[2]   1,[2] + 3,[]    0,[2] + 2,[]    0
   *   0,[1, 2] + 1,[2]   1       0      0       1       0
   *       1        0
   *
   * We can take advantage of this recursive structure by evaluating different
   * sub-trees in parallel.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if(money == 0) 1
    else if (coins.isEmpty || money < 0) 0
    else countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }

  type Threshold = (Int, List[Int]) => Boolean

  /**
   * In parallel, counts the number of ways change can be made from the
   * specified list of coins for the specified amount of money.
   *
   * As we learned in the lectures, the parCountChange should not spawn parallel
   * computations after reaching the leaf in the call graph -- the
   * synchronization costs of doing this are way too high. Instead, we need to
   * agglomerate parts of the computation. We do this by calling the sequential
   * countChange method when we decide that the amount of work is lower than a
   * certain value, called the threshold.
   *
   * To separate the concern of deciding on the threshold value from the
   * implementation of our parallel algorithm, we implement the threshold
   * functionality in a separate function, described by the Threshold type
   * alias:
   *     type Threshold = (Int, List[Int]) => Boolean
   *
   * When a threshold function returns true for a given amount of money and the
   * given list of coins, the sequential countChange implementation must be
   * called.
   */
  def parCountChange(money: Int, coins: List[Int], threshold: Threshold): Int = {
    if (threshold(money, coins) || coins.isEmpty || money < 0) {
      countChange(money, coins)
    }
    else {
      val (a, b) = parallel(
        parCountChange(money - coins.head, coins, threshold),
        parCountChange(money, coins.tail, threshold))
      a + b
    }
  }

  /*
   Now that we have the parCountChange method, we ask ourselves what is the right
   implementation of the threshold function? Recall the examples from the
   lectures, such as summing the array values and computing the norm, where this
   was easy -- we exactly knew the amount of work required to traverse a subrange
   of the array, so threshold could return true when the length of the subrange
   was smaller than a certain value.

   Sadly, the total amount of work for a given parCountChange invocation is hard
   to evaluate from the remaining amount of money and a list of coins. In fact,
   the amount of work directly corresponds to the count that parCountChange
   returns, which is the value that we are trying to compute. Counting change is
   a canonical example of a task-parallel problem in which the partitioning the
   workload across processors is solution-driven -- to know how to optimally
   partition the work, we would first need to solve the problem itself.

   For this reason, many parallel algorithms in practice rely on heuristics to
   assess the amount of work in a subtask. Bellow, We will implement several such
   heuristics and assess the effect on performance.
   */

  /**
   * Threshold heuristic based on the starting money.
   *
   * Creates a threshold function that returns true when the amount of money is
   * less than or equal to 2 / 3 of the starting amount. Observe that this
   * heuristic does not take into account how many coins are left in the coins
   * list.
   *
   * Note that a / b will return the integer division of a and b when both
   * operands are Ints. To avoid this problem, we always do the multiplication
   * of startingMoney by 2 before doing the division by 3.
   */
  def moneyThreshold(startingMoney: Int): Threshold =
    (m: Int, c: List[Int]) => if ( m <= startingMoney * 2 / 3 ) true else false

  /**
   * Threshold heuristic based on the total number of initial coins.
   *
   * Returns a threshold function that returns true when the number of coins is
   * less than or equal to the 2 / 3 of the initial number of coins.
   *
   * Note that a / b will return the integer division of a and b when both
   * operands are Ints. To avoid this problem, we always do the multiplication
   * of totalCoins by 2 before doing the division by 3.
   */
  def totalCoinsThreshold(totalCoins: Int): Threshold =
    (m: Int, c: List[Int]) => if ( c.length <= totalCoins * 2 / 3 ) true else false


  /**
   * Threshold heuristic based on the starting money and the initial list of
   * coins.
   *
   * Returns a threshold function that returns true when the amount of money
   * multiplied with the number of remaining coins is less than or equal to the
   * starting money multiplied with the initial number of coins divided by 2.
   */
  def combinedThreshold(startingMoney: Int, allCoins: List[Int]): Threshold = {
    (m: Int, c: List[Int]) => if ( m * c.length <= startingMoney * allCoins.length / 2 ) true else false
  }

}
