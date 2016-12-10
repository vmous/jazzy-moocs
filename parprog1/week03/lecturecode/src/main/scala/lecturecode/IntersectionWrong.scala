package lecturecode

import scala.collection._
import org.scalameter._

object IntersectionWrong {

  def main(args: Array[String]): Unit = {
    def intersection(a: GenSet[Int], b: GenSet[Int]): Set[Int] = {
      val result = mutable.Set[Int]()
      /* The problem with this implementation is that "result", which is a
       mutable, non-thread safe data structure. is access by different threads
       in the assignment below. If you run this program multiple times you will
       observe that the results returned by the parallel version of the
       intersection bellow is non-deterministic. */
      for (x <- a) if (b contains x) result += x
      result
    }
    val seqres = intersection((0 until 1000).toSet, (0 until 1000 by 4).toSet)
    val parres = intersection((0 until 1000).par.toSet, (0 until 1000 by 4).par.toSet)
    log(s"Sequential result - ${seqres.size}")
    log(s"Parallel result   - ${parres.size}")
  }

}
