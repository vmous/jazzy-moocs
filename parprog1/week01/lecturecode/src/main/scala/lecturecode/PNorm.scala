package lecturecode

import java.util.concurrent._

import scala._
import scala.util.DynamicVariable

import ParallelCombinator.parallel

object PNorm {

  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Double = {
    val sequence = for (i <- s until t) yield math.floor(math.pow(math.abs(a(i)).toDouble, p))
    sequence.sum
  }

  def pNorm(a: Array[Int], p: Double): Double = {
    math.pow(sumSegment(a, p, 0, a.length), 1 / p)
  }

  def pNormTwoParts(a: Array[Int], p: Double): Double = {
    val m = a.length / 2
    val (sum1, sum2) = parallel(sumSegment(a, p, 0, m), sumSegment(a, p, m, a.length))
    math.pow((sum1 + sum2), 1 / p)
  }

  def pNormTwoParallelParts(a: Array[Int], p: Double): Double = {
    val m = a.length / 2
    val (sum1, sum2) = parallel(sumSegment(a, p, 0, m), sumSegment(a, p, m, a.length))
    math.pow((sum1 + sum2), 1 / p)
  }

  def sumSegmentRec(a: Array[Int], p: Double, s: Int, t: Int): Double = {
    val threshold = 2

    if (t - s < threshold) sumSegment(a, p, s, t)
    else {
      val m = s + (t - s) / 2
      val (sum1, sum2) = parallel(sumSegmentRec(a, p, s, m), sumSegmentRec(a, p, m, t))
      sum1 + sum2
    }
  }

  def pNormRecursive(a: Array[Int], p: Double): Double = {
    math.pow(sumSegmentRec(a, p, 0, a.length), 1 / p)
  }

}
