package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnitRunner])
class UniqueIDSuite extends FunSuite {

  import UniqueID._

  test("testing unsynchronized id generation") {
    val tasks: Seq[Future[Seq[Long]]] =
      for (i <- 1 to 100) yield future {
        (for (i <- 0 to 10) yield getUniqueId())
      }

    val aggregated: Future[Seq[Seq[Long]]] = Future.sequence(tasks)

    val sequence: Seq[Seq[Long]] = Await.result(aggregated, Duration.Inf)

    val flattened: Seq[Long] = sequence.flatten

    assert(!(flattened.size === flattened.toSet.size))
  }

}
