package lecturecode

import common.parallel
import org.scalameter._

object ParallelSort {
  // a bit of reflection to access the private sort1 method, which takes an offset and an argument
  private val sort1 = {
    val method = scala.util.Sorting.getClass.getDeclaredMethod("sort1", classOf[Array[Int]], classOf[Int], classOf[Int])
    method.setAccessible(true)
    (xs: Array[Int], offset: Int, len: Int) => {
      method.invoke(scala.util.Sorting, xs, offset.asInstanceOf[AnyRef], len.asInstanceOf[AnyRef])
    }
  }

  def quickSort(xs: Array[Int], offset: Int, length: Int): Unit = {
    sort1(xs, offset, length)
  }

  /**
    * The parallel merge sort.
    *
    * <ul>The algorithm:
    * <li>recursively sorts the two halves of the array in parallel,</li>
    * <li>
    *   sequentially merge the two array halves by copying into temporary array,
    * </li>
    * <li>copy the temporary array back into the original array</li>
    */
  def parMergeSort(xs: Array[Int], maxDepth: Int): Unit = {

    /* At each level of the merge sort, we will alternate between the source
       array xs and the intermediate array ys.
       1) Allocate a helper array.
       This step is a bottleneck, and takes:
       - ~76x less time than a full quickSort without GCs (best time)
       - ~46x less time than a full quickSort with GCs (average time)
       Therefore:
       - there is a almost no performance gain in executing allocation concurrently to the sort
       - doing so would immensely complicate the algorithm */
    val ys = new Array[Int](xs.length)

    /* 2) Sort the elements.
       The merge step has to do some copying, and is the main performance bottleneck of the algorithm.
       This is due to the final merge call, which is a completely sequential pass.

       Merging can be done sequentially. It takes two arrays, the source and the
       destination, and the two sorted intervals in the source array that are
       given with the points of the beginning, the middle point and the end point.
       It writes the resulting merged array into the destination. */
    def merge(src: Array[Int], dst: Array[Int], from: Int, mid: Int, until: Int) = {
      var left = from
      var right = mid
      var i = from
      while (left < mid && right < until) {
        while (left < mid && src(left) <= src(right)) {
          dst(i) = src(left)
          i += 1
          left += 1
        }
        while (right < until && src(right) <= src(left)) {
          dst(i) = src(right)
          i += 1
          right += 1
        }
      }
      while (left < mid) {
        dst(i) = src(left)
        i += 1
        left += 1
      }
      while (right < until) {
        dst(i) = src(right)
        i += 1
        right += 1
      }
    }

    /* Without the merge step, the sort phase parallelizes almost linearly.
       This is because the memory pressure is much lower than during copying in
       the third step.

       The shape of parallel merge sort is typical for divide and conquer parallel
       algorithms. In the base case, we are just going to invoke a sequential
       sorting algorithm (e.g., a quick sort). The base cases in divide and
       conquer parallel algorithms are triggered when we reach a threshold after
       which we assume sequential processing is enough. In this case here, this is
       controlled using the depth parameter and we will stop parallelization when
       the depth of the compute tree reaches maximum depth at which point we have
       saturated the parallel resources that we have on a given machine.
       When we have not reached yet the base case, we are going to then split the
       array in middle, computing the middle point. Then we recursively sort the
       first half of the array and the second half of the array, each time
       updating the depth as we proceed to smaller array segments. These two sorts
       are taking place in parallel.
       The parallel subroutines leave the two array segments sorted. In order to
       get the final sorted array we need to merge the two segments, and we do so
       by calling function merge. */
    def sort(from: Int, until: Int, depth: Int): Unit = {
      if (depth == maxDepth) {
        quickSort(xs, from, until - from)
      }
      else {
        val mid = (from + until) / 2
        parallel(sort(mid, until, depth + 1), sort(from, mid, depth + 1))

        /* We have two arrays that we use, xs and ys. Which of them we are going to
           use as primary and which as auxiliary is determined by the compute depth
           we are currently are. We determine that by checking if the difference of
           the from the max depth is even or odd so that at each level we use a
           different pair. */
        val flip = (maxDepth - depth) % 2 == 0
        val src = if (flip) ys else xs
        val dst = if (flip) xs else ys
        merge(src, dst, from, mid, until)
      }
    }
    // To sort we invoke merge starting from 0 and going to the end of the array
    sort(0, xs.length, 0)

    // 3) In parallel, copy the elements back into the source array.
    // Executed sequentially, this step takes:
    // - ~23x less time than a full quickSort without GCs (best time)
    // - ~16x less time than a full quickSort with GCs (average time)
    // There is a small potential gain in parallelizing copying.
    // However, most Intel processors have a dual-channel memory controller,
    // so parallel copying has very small performance benefits.
    def copy(src: Array[Int], target: Array[Int], from: Int, until: Int, depth: Int): Unit = {
      if (depth == maxDepth) {
        Array.copy(src, from, target, from, until - from)
      } else {
        val mid = from + ((until - from) / 2)
        parallel(copy(src, target, mid, until, depth + 1), copy(src, target, from, mid, depth + 1))
      }
    }
    // If we are flipped to the auxiliary array, copy back to source.
    if (maxDepth % 2 != 0) copy(ys, xs, 0, xs.length, 0)
  }

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 20,
    Key.exec.maxWarmupRuns -> 60,
    Key.exec.benchRuns -> 60,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def initialize(xs: Array[Int]): Unit = {
    var i = 0
    while (i < xs.length) {
      xs(i) = i % 100
      i += 1
    }
  }

  def main(args: Array[String]): Unit = {
    val length = 10000000
    val maxDepth = 7
    val xs = new Array[Int](length)
    val seqtime = standardConfig setUp {
      _ => initialize(xs)
    } measure {
      quickSort(xs, 0, xs.length)
    }
    println(s"sequential sum time: $seqtime ms")

    val partime = standardConfig setUp {
      _ => initialize(xs)
    } measure {
      parMergeSort(xs, maxDepth)
    }
    println(s"fork/join time: $partime ms")
    println(s"speedup: ${seqtime / partime}")
  }

}
