package lecturecode

import common.parallel

import org.scalameter._

object ReduceArray {

  def reduceSegSeq[A](inp: Array[A], left: Int, right: Int, f: (A, A) => A): A = {
    var res = inp(left)
    var i = left + 1
    while (i < right) {
      res = f(res, inp(i))
      i += 1
    }
    res
  }

  def reduceSegPar[A](inp: Array[A], left: Int, right: Int, f: (A, A) => A): A = {
    if (right - left < threshold) reduceSegSeq(inp, left, right, f)
    else {
      val mid = left + (right - left) / 2
      val (a1, a2) = parallel(reduceSegPar(inp, left, mid, f),
        reduceSegPar(inp, mid, right, f))
      f(a1, a2)
    }
  }

  def reduceSeq[A](inp: Array[A], f: (A, A) => A): A = reduceSegSeq(inp, 0, inp.length, f)
  def reducePar[A](inp: Array[A], f: (A, A) => A): A = reduceSegPar(inp, 0, inp.length, f)

  val c = 2.99792458e8
  def assocOp(v1: Double, v2: Double): Double = {
    val u1 = v1/c
    val u2 = v2/c
    (v1 + v2)/(1 + u1*u2)
  }

  def addVelSeq(inp: Array[Double]): Double = {
    reduceSegSeq(inp, 0, inp.length, assocOp)
  }
  def addVelPar(inp: Array[Double]): Double = {
    reduceSegPar(inp, 0, inp.length, assocOp)
  }

  val threshold = 10000

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 30,
    Key.exec.maxWarmupRuns -> 30,
    Key.exec.benchRuns -> 20,
    Key.verbose -> false
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val alen = 2000000
    val inp = (0 until alen).map((x:Int) => (x % 50)*0.0001*c).toArray
    var resSeq = 0.0
    val seqtime = standardConfig measure {
      resSeq = addVelSeq(inp)
    }
    var resPar = 0.0
    val partime = standardConfig measure {
      resPar = addVelPar(inp)
    }

    println(s"sequential time: $seqtime ms and result $resSeq")
    println(s"parallel time: $partime ms and result $resPar")
    /* Example output on Intel(R) Core(TM) i7-3770K CPU @ 3.50GHz (4 cores, 8 hw threads), 16GB RAM
       [info] sequential time: 33.0507908 ms and result 2.997924579999967E8
       [info] parallel time: 11.158121000000003 ms and result 2.99792458E8
       We get around 3 times speedup. The computed value is slightly different due to roundoff errors. */
  }

}
