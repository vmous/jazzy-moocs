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

  test("testing primes using Sieve of Eratosthenes") {
    val primes = sieve(from(2))
    assert((primes take 6).toList === List(2, 3, 5, 7, 11, 13))
  }

}
