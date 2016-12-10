package lecturecode

import scala.collection.GenSeq


object DataParallelOps {

  /**
    * Implement the parallel sum method, which returns the sum of the integers
    * in the given array.
    *
    * The implementation uses the method fold. Note that the use of
    * foldRight/foldLeft methods prevent the folding to use parallelism.
    */
  def sum(xs: Array[Int]): Int = {
    xs.par.fold(0)(_ + _)
  }

  def max(xs: Array[Int]): Int = {
    xs.par.fold(Int.MinValue)(math.max) // (x, y) => if (x > y) x else y
  }

  def largestPalidrome(xs: GenSeq[Int]): Int = {
    xs.aggregate(Int.MinValue)((largest, n) =>
      if (n > largest && n.toString == n.toString.reverse) n else largest, math.max)
  }

}
