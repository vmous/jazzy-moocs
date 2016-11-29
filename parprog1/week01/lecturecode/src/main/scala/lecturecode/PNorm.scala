package lecturecode

import scala._

object PNorm {

  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Double = {
    val sequence = for (i <- s until t) yield math.floor(math.pow(math.abs(a(i)).toDouble, p))
    sequence.sum
  }

  def pNorm(a: Array[Int], p: Double): Double = {
    math.pow(sumSegment(a, p, 0, a.length), 1 / p)
  }

}
