package lecturecode

import common.parallel

object Scan {

  def mapSegSeq[A, B](inp: Array[A], left: Int, right: Int, fi: (Int, A) => B, out: Array[B]): Unit = {
    var i = left
    while (i < right) {
      out(i) = fi(i, inp(i))
      i += 1
    }
  }

  def mapSegPar[A, B](inp: Array[A], left: Int, right: Int, fi: (Int, A) => B, out: Array[B]): Unit = {
    if (right - left < threshold)
      mapSegSeq(inp, left, right, fi, out)
    else {
      val mid = left + (right - left) / 2
      parallel(mapSegSeq(inp, left, mid, fi, out),
        mapSegSeq(inp, mid, right, fi, out))
    }
  }

  def reduceSegSeq[A](inp: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A): A = {
    var res = a0
    var i = left
    while(i < right) {
      res = f(res, inp(i))
      i += 1
    }
    res
  }

  def reduceSegPar[A](inp: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A): A = {
    if (right - left < threshold) reduceSegSeq(inp, left, right, a0, f)
    else {
      val mid = left + (right - left) / 2
      val (a1, a2) = parallel(reduceSegPar(inp, left, mid, a0, f),
        reduceSegPar(inp, mid, right, a0, f))
      f(a1, a2)
    }
  }

  def scanLeftSeq[A](inp: Array[A], a0: A, f: (A, A) => A, out: Array[A]): Unit = {
    require(out.length == inp.length + 1)

    out(0) = a0
    var a = a0
    var i = 0
    while(i < inp.length) {
      a = f(a, inp(i))
      i += 1
      out(i) = a
    }
  }

  /**
    * ScanLeft implementation using map and reduce operations.
    *
    * The benefit of this implementation is that it leverages the parallel
    * implementations of map and reduce to benefit the overall running time of
    * scanLeft.
    *
    * The solution follows the definition of scan: an element in position i in
    * the output, is the result of reducing the segment of the input array up to
    * that position. Therefore, the resulting array is obtained using a map over
    * the input array where the function fi given to map is going to reduce the
    * array segment from 0 to i. The invocation of map will fill in the output
    * array with elements, starting from 0 to the end of the input array.
    * The final element of the output array is computed by taking the element
    * before the final and combining it with the corresponding element of the
    * input array. If map and reduce are implemented in parallel, and they each
    * have O(logn) and parallel complexity, then because map is applying all
    * these individual operations in parallel, the overall depth is going to
    * continue to be logn.
    */
  def scanLeftMR[A](inp: Array[A], a0: A, f: (A, A) => A, out: Array[A]): Unit = {
    require(out.length == inp.length + 1)

    val fi = { (i: Int, v: A) => reduceSegPar(inp, 0, i, a0, f) }
    mapSegPar(inp, 0, inp.length, fi, out)
    val last = inp.length - 1
    out(last + 1) = f(out(last), inp(last))
  }

  val threshold = 10000

}
