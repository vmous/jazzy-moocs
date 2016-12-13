package lecturecode

import scala.collection._
import org.scalameter._

object Intersection {

  def main(args: Array[String]): Unit = {
    def intersection(a: GenSet[Int], b: GenSet[Int]): GenSet[Int] = {
      /* We filter the elements from a which are also contained in b.
       Since sets in Scala also have the type A => Boolean as the "contains"
       method (in this case here Int => Boolean), we can use the set b as an
       argument to the filter operation. */
      if (a.size < b.size) a.filter(b(_))
      else b.filter(a(_))
    }
    val seqres = intersection((0 until 1000).toSet, (0 until 1000 by 4).toSet)
    val parres = intersection((0 until 1000).par.toSet, (0 until 1000 by 4).par.toSet)
    log(s"Sequential result - ${seqres.size}")
    log(s"Parallel result   - ${parres.size}")
  }

}
