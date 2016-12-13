package lecture

import scala.collection._

object ParallelGraphContraction {

  /* Here we create a cyclograph in which every node has exactly one successor.
   The graph is, in this example, represented with a mutable map, where the keys
   are the vertices and the values are their successors.
   The graph is then contracted in parallel. Each node's successor is replaced by
   the successor's successor. This is done in the parallel for-loop in which for
   every pair of a node and its successor, the successor is replaced with the
   successor's successor.
   Finally, we check if the program is correct by trying to find all of the nodes
   for which the successor node is not two steps away. After the contraction,
   every node should have the successor, which is exactly by two larger than
   itself. The only exception are the nodes at the very end, who have to do the
   division, modulo graph.size.
   If the program is correct, the printed violation should be none. */
  def main(args: Array[String]): Unit = {
    val graph = mutable.Map[Int, Int]() ++= (0 until 100000).map(i => (i, i + 1))
    /* We have two problems:
     - #1: we are modifying the same collection that we are traversing
     in parallel.
     - #2: we are reading from a collection that is concurrently being
     modified by some other iteration of the loop.
     In both cases, since the mutable map is not a thread-safe collection, we
     risk crashing the program (because the data structure can be observed in a
     corrupted state) or getting incorrect results. */
    graph(graph.size - 1) = 0
    for ((k, v) <- graph.par) graph(k) = graph(v)
    val violation = graph.find({ case (i, v) => v != (i + 2) % graph.size })
    println(s"violation: $violation")
  }

}
