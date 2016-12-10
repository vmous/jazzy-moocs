package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {


  def encodeParentheses(char: Char) = char match {
    case '(' => 1
    case ')' => -1
    case _ => 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    var count = 0
    var i = 0
    var quitFlag = false
    while (i < chars.length && quitFlag == false) {
      val head = encodeParentheses(chars(i))
      count = count + head
      if (count < 0) quitFlag = true
      i = i + 1
    }
    count == 0
  }

  /**
   * Returns `true` iff the parentheses in the input `chars` are balanced.
   *
   * The tricky part in parallel parentheses balancing is choosing the
   * reduction operator -- you probably implemented balance by keeping an
   * integer accumulator, incrementing it for left parentheses and
   * decrementing it for the right ones, taking care that this accumulator does
   * not drop below zero. Parallel parentheses balancing requires to keep
   * two integer values for the accumulator.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int) : (Int, Int) = {
      if (idx == until) (arg1, arg2)
      else {
        val head = encodeParentheses(chars(idx))
        if (head + arg2 >= 0) {
          traverse(idx + 1, until, arg1, head + arg2)
        }
        else {
          traverse(idx + 1, until, head + arg2 + arg1, 0)
        }
      }
    }

    def reduce(from: Int, until: Int) : (Int, Int)= {
      if (until - from <= threshold) traverse(from, until, 0, 0 )
      else {
        val mid = from + (until - from)/2
        val (left, right) = parallel(
          reduce(from, mid),
          reduce(mid, until)
        )
        val diff = left._2 + right._1
        if (left._1 >= 0 || diff <= 0) (left._1 + diff, right._2)
        else (left._1, diff + right._2)
      }
    }

    reduce(0, chars.length) == (0, 0)

  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
