package lecturecode

import common.parallel

object ParallelMap {
  val threshold = 2

  def mapASegSeq[A, B](inp: Array[A], left: Int, right: Int, f: A => B,
    out: Array[B]) = {
    // Writes to out(i) for left <= i <= right - 1
    var i = left
    while (i < right) {
      out(i) = f(inp(i))
      i += 1
    }
  }

  /**
    * <ul>Note:
    * <li>
    *   writes need to be disjoint (otherwise: non-deterministic behaviour)
    * </li>
    * <li>
    *   threshold needs to be large enough (otherwise we loose efficiency)
    * </li>
    * </ul>
    */
  def mapASegPar[A, B](inp: Array[A], left: Int, right: Int, f: A => B,
    out: Array[B]) = {
    // Writes to out(i) for left <= i <= right - 1
    if (right - left < threshold)
      mapASegSeq(inp, left, right, f, out)
    else {
      val mid = left + (right - left) / 2
      parallel(mapASegSeq(inp, left, mid, f, out),
        mapASegSeq(inp, mid, right, f, out))
    }
  }

}
