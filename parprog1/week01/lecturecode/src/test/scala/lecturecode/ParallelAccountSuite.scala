package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent.{ Future, future }

@RunWith(classOf[JUnitRunner])
class ParallelAccountSuite extends FunSuite {

  import ParallelAccount._

  val a1 = new Account(500000)
  val a2 = new Account(700000)

  def startThread(a: Account, b: Account, n: Int) = {
    val t = new Thread {
      override def run() {
        for (i <- 0 until n) a.transfer(b, 1)
      }
    }
    t.start
    t
  }

  test("testing deadlock") {
    val t1 = startThread(a1, a2, 150000)
    val t2 = startThread(a2, a1, 150000)
    t1.join()
    t2.join()
  }

}
