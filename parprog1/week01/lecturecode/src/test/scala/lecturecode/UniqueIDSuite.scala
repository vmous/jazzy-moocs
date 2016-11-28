package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.{ Await, Future, future }
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnitRunner])
class UniqueIDSuite extends FunSuite {

  import UniqueID._

  def runThreads(f: () => Long): Seq[Seq[Long]] = {
    val tasks: Seq[Future[Seq[Long]]] =
      for (i <- 1 to 1000) yield future {
        (for (i <- 0 to 10) yield f())
      }

    // Future.sequence just turns Seq[Future[T]] => Future[Seq[T]] which means
    // "gather results of all already started futures and put it in a future".
    val aggregated: Future[Seq[Seq[Long]]] = Future.sequence(tasks)

    val sequence: Seq[Seq[Long]] = Await.result(aggregated, Duration.Inf)

    sequence
  }

  def noDuplicates(sequence: Seq[Seq[Long]]): Boolean = {

    val flattened: Seq[Long] = sequence.flatten

    (flattened.size === flattened.toSet.size)
  }

  test("testing unsynchronized id generation") {
    assert(!noDuplicates(runThreads(getUniqueId)))
  }

  test("testing monitored id generation") {
    assert(noDuplicates(runThreads(getUniqueIdWithMonitor)))
  }

}
