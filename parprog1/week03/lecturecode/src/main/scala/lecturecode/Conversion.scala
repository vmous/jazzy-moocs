package lecturecode

import scala.collection.GenSeq
import org.scalameter._

object Conversion {

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

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 10,
    Key.exec.maxWarmupRuns -> 20,
    Key.exec.benchRuns -> 20,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  val array = Array.fill(10000000)("")
  val list = array.toList

  def main(args: Array[String]): Unit = {
    val listtime = standardConfig measure {
      list.par
    }
    println(s"list conversion time: $listtime ms")

    val arraytime = standardConfig measure {
      array.par
    }
    println(s"array conversion time: $arraytime ms")
    println(s"difference: ${listtime / arraytime}")
  }

}
