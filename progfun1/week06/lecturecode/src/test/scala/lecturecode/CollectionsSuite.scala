package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CollectionsSuite extends FunSuite {

  import Collections._

  test("testing combinations") {
    assert(combinations((1 to 5), (3 to 10)) === Vector((1,3), (1,4), (1,5), (1,6), (1,7), (1,8), (1,9), (1,10), (2,3), (2,4), (2,5), (2,6), (2,7), (2,8), (2,9), (2,10), (3,3), (3,4), (3,5), (3,6), (3,7), (3,8), (3,9), (3,10), (4,3), (4,4), (4,5), (4,6), (4,7), (4,8), (4,9), (4,10), (5,3), (5,4), (5,5), (5,6), (5,7), (5,8), (5,9), (5,10)))
  }

  test("testing scalar product") {
    assert(scalarProduct(Vector(2, 4), Vector(6, 8)) === 44)
    assert(scalarProductPatmat(Vector(2, 4), Vector(6, 8)) === 44)
  }

  test("testing primes") {
    assert(isPrime(13))
    assert(!isPrime(32))
  }

}
