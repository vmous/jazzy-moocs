package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner]) 
class LineOfSightSuite extends FunSuite {

  import LineOfSight._

  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }


  test("upsweep should correctly handle an array of size 4 and threshold 3") {
    val threshold = 3
    val res = upsweep(Array[Float](0f, 1f, 8f, 9f), 0, 4, threshold)
    val expected = new Node(new Leaf(0, 2, 1), new Leaf(2, 4, 4))
    assert(res == expected)
  }


  test("upsweep should correctly compute the tree on the indices 1 until 5 of a 5 element array for threshold 1") {
    val threshold = 1
    val res = upsweep(Array[Float](0f, 1f, 8f, 9f, 9f), 1, 5, threshold)
    val expected = new Node(
      new Node(new Leaf(1, 2, 1), new Leaf(2, 3, 4)),
      new Node(new Leaf(3, 4, 3), new Leaf(4, 5, 2.25f)))
    assert(res == expected)
  }


  test("upsweep should correctly compute the tree on the indices 1 until 5 of a 5 element array for threshold 2") {
    val threshold = 2
    val res = upsweep(Array[Float](0f, 1f, 8f, 9f, 9f), 1, 5, threshold)
    val expected = new Node(new Leaf(1, 3, 4), new Leaf(3, 5, 3))
    assert(res == expected)
  }


  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("downsweep should correctly handle a 4 element array") {
    val threshold = 3
    val input = Array[Float](0f, 1f, 8f, 9f)
    val output = new Array[Float](4)
    val tree = upsweep(input, 0, 4, threshold)
    downsweep(input, output, 0f, tree)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("downsweep should correctly handle trees with a single leaf") {
    val input = Array[Float](0f, 1f, 8f, 9f)
    val output = new Array[Float](4)
    val tree = new Leaf(0, 4, 4f)
    downsweep(input, output, 0f, tree)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("parLineOfSight should correctly handle an array of size 4 at threshold 3") {
    val threshold = 3
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, threshold)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("parLineOfSight should correctly handle an array of size 4 at threshold 2") {
    val threshold = 2
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, threshold)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

}

