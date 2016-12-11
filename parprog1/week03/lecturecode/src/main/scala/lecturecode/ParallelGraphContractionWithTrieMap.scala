package lecturecode

import scala.collection._

object ParallelGraphContractionWithTrieMap {

  /* TrieMap collection is a concurrent collection that can be used to safely
   read/write/traverse the collection in parallel with other operations and
   preserving the deterministic nature of our algoritm.
   This concurrent collection automatically creates atomic snapshots whenever a
   parallel operation starts. So concurrent updates are not observed during
   parallel traversals. TrieMap also provides the snapshot method, which
   efficiently creates a clone of the map.
   In the snippet bellow, we create a concurrent TrieMap instead of a regular
   mutable map. This ensures that when the for each loop starts, the graph state
   is atomically captured. Modifications to the graph are not observed during the
   traversal, and for each traverses only those node successor pairs that were in
   the graph at the beginning. Also, in this example, we use the snapshot method
   separately to grab the previous state of the graph (Taking the snapshot is
   very efficient; it happens in constant time). This previous state is then used
   to find the successor's successor.
   Note that snapshots are never modified. The only collection that does get
   modified is graph. */
  def main(args: Array[String]) {
    val graph = concurrent.TrieMap[Int, Int]() ++= (0 until 100000).map(i => (i, i + 1))
    graph(graph.size - 1) = 0
    val previous = graph.snapshot()
    for ((k, v) <- graph.par) graph(k) = previous(v)
    val violation = graph.find({ case (i, v) => v != (i + 2) % graph.size })
    println(s"violation: $violation")
  }

}
