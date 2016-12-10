package lecturecode

import common.parallel
import org.scalameter._

object ScanArray {

  sealed abstract class TreeFold[A] { val res: A }
  case class Leaf[A](from: Int, until: Int, override val res: A) extends TreeFold[A]
  case class Node[A](l: TreeFold[A], override val res: A,
    r: TreeFold[A]) extends TreeFold[A]

  /**
    * fold left array segment from from to until-1, sequentially. Used in the
    * base case for upsweep. This is the same operation we would use in the
    * base case of parallel fold.
    */
  def foldSegSeq[A,B](inp: Array[A], from: Int, until: Int, b0: B, f: (B,A) => B): B = {
    var b = b0
    var i = from
    while (i < until) {
      b = f(b, inp(i))
      i = i + 1
    }
    b
  }

  def upsweep[A](inp: Array[A], from: Int, until: Int, a0: A, f: (A, A) => A): TreeFold[A] = {
    // requires f to be associative
    if (until - from < threshold)
      /* Even though this implementation of foldSegSeq gets a0 the initial
       element, the way we invoked it here is with input element of the array
       at the initial element of the segment and then reducing starting from
       the subsequent element.
       */
      Leaf(from, until, foldSegSeq(inp, from + 1, until, inp(from), f))
    else {
      val mid = from + (until - from) / 2
      val (tl, tr)  = parallel(
        upsweep(inp, from, mid, a0, f),
        upsweep(inp, mid, until, a0, f))
      Node(tl, f(tl.res, tr.res), tr)
    }
  }

  /**
    * Scan array segment inp(from) to inp(until-1), storing results into
    * out(from+1) to out(until).
    *
    * At the end, out(i+1) stores fold of elements:
    *     [a0, in(from),... in(i)] for i from from to until-1
    *
    * In particular, out(from+1) stores f(a0,inp(from)) and out(until) stores
    * fold of [a0, in[(from),... inp(until-1)]. The value a0 is not directly
    * stored into out anywhere. This is used below cut-off in downsweep for
    * scanLeftPar, and also to implement scanLeftSeq as a comparison point.
   */
  def scanLeftSegSeq[A](inp: Array[A], from: Int, until: Int, a0: A, f: (A, A) => A, out: Array[A]) = {
    if (from < until) {
      var i = from
      var a = a0
      while (i < until) {
        a = f(a,inp(i))
	out(i + 1) = a
        i = i + 1
      }
    }
  }

  def scanLeftSeq[A](inp: Array[A], a0: A, f: (A, A) => A, out: Array[A]) = {
    out(0) = a0

    scanLeftSegSeq(inp, 0, inp.length, a0, f, out)
  }

  def downsweep[A](inp: Array[A], a0: A, f: (A, A) => A, t: TreeFold[A], out: Array[A]): Unit = {
    t match {
      case Leaf(from, until, res) => 
        scanLeftSegSeq(inp, from, until, a0, f, out)
      case Node(l, res, r) => {
        /* As we're processing the tree of results, the initial element for the
         left of tree is our own initial element a0, whereas the initial
         element for the right sub-tree is a0 combined with the result of
         reducing the left symmetry. 
         */
        val (_, _) = parallel(
          downsweep(inp, a0, f, l, out),
          downsweep(inp, f(a0,l.res), f, r, out))
      }
    }
  }

  def scanLeftSegPar[A](inp: Array[A], from: Int, until: Int, a0: A, f: (A, A) => A, out: Array[A]) = {
    val t = upsweep(inp, from, until, a0, f)
    downsweep(inp, a0, f, t, out)
  }

  def scanLeftPar[A](inp: Array[A], a0: A, f: (A, A) => A, out: Array[A]) = {
    out(0) = a0

    scanLeftSegPar(inp, 0, inp.length, a0, f, out)
  }

  var threshold = 20000

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 6,
    Key.exec.maxWarmupRuns -> 6,
    Key.exec.benchRuns -> 5,
    Key.verbose -> false
  ) withWarmer(new Warmer.Default)


  def testConcat : Unit = {
    println("===========================================")
    println("Testing ArrayScan on concatenation example.")
    println("===========================================")

    def concat(x: List[Int], y: List[Int]): List[Int] =
      x ::: y

    def arrEq[A](a1: Array[A], a2: Array[A]): Boolean = {
      def eqSeq(from: Int, until: Int): Boolean = {
	var i= from
	while (i < until) {
	  if (a1(i) != a2(i)) {
	    println(s"Array difference: a1(${i})=${a1(i)}, a2(${i})=${a2(i)}")
	    return false
	  } else {
	    i= i + 1
	  }
	}
	true
      }
      if (a1.length != a2.length) {
	println("Different sizes!")
	false
      } else eqSeq(0, a1.length)
    }

    threshold = 100

    val alen = 2000
    val inp = (0 until alen).map((x:Int) => List(x)).toArray
    val outSeq = new Array[List[Int]](alen + 1)
    val outPar = new Array[List[Int]](alen + 1)
    val init = List(12309, 32123)
    val seqtime = standardConfig measure {
      scanLeftSeq(inp, init, concat, outSeq)
    }
    println(s"sequential time: $seqtime ms")

    val partime = standardConfig measure {
      scanLeftPar(inp, init, concat, outPar)
    }
    println(s"parallel time: $partime ms")
    println(s"speedup: ${seqtime / partime}")
    print("Are results equal?")
    println(arrEq(outSeq, outPar))
    //println(outPar.toList)
  }

  def testVelocity = {
    println("======================================")
    println("Testing ArrayScan on velocity example.")
    println("======================================")

    threshold = 20000

    val c = 2.99792458e8
    def velocityAdd(v1: Double, v2: Double): Double = {
      val u1 = v1/c
      val u2 = v2/c
      (u1 + u2)/(1 + u1*u2)*c
    }

    val alen = 2000000
    val inp = (0 until alen).map((x:Int) => (x % 50)*0.0001*c).toArray
    val outSeq = new Array[Double](alen + 1)
    val outPar = new Array[Double](alen + 1)
    val seqtime = standardConfig measure {
      scanLeftSeq(inp, 0.0, velocityAdd, outSeq)
    }
    println(s"sequential time: $seqtime ms")

    val partime = standardConfig measure {
      scanLeftPar(inp, 0.0, velocityAdd, outPar)
    }
    println(s"parallel time: $partime ms")
    println(s"speedup: ${seqtime / partime}")
  }

  def testNonZero = {
    println("====================================================")
    println("Testing ArrayScan on addition with non-zero initial.")
    println("====================================================")
    val inp: Array[Int] = (1 to 10).toArray
    val outSeq: Array[Int] = new Array[Int](inp.length + 1)
    val outPar: Array[Int] = new Array[Int](inp.length + 1)
    val f = (x: Int, y: Int) => x + y
    threshold = 3
    scanLeftSeq(inp, 10, f, outSeq)
    println(outSeq.toList)
    scanLeftPar(inp, 10, f, outPar) // a0 = 10
    println(outPar.toList)
  }

  def main(args: Array[String]): Unit = {
    testNonZero
    testConcat
    testVelocity
  }

}
