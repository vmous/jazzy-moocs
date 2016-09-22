package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PrimesSuite extends FunSuite {

  import Primes._

  test("testing from") {
    val nats = from(0)
    val m4s = nats map (_ * 4)
    assert((m4s take 5).toList === List(0, 4, 8, 12, 16))
  }

}
