package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConditionalsSuite extends FunSuite {
 
  import Conditionals._

  test("absolute of a positive") {
    assert(abs(1) === 1)
  }

  test("absolute of a negative") {
    assert(abs(-1) === 1)
  }

  test("absolute of a zero") {
    assert(abs(0) === 0)
  }

  test("true && true") {
    assert(and(true, true) === true)
  }

  test("true && false") {
    assert(and(true, false) === false)
  }

  test("false && true") {
    assert(and(false, true) === false)
  }

  test("false && false") {
    assert(and(false, false) === false)
  }

  /**
    * This tests the CBN argument of the AND implementation.
    * 
    */
  test("false && loop") {
    assert(and(false, loop) === false)
  }

  test("true || true") {
    assert(or(true, true) === true)
  }

  test("true || false") {
    assert(or(true, false) === true)
  }

  test("false || true") {
    assert(or(false, true) === true)
  }

  test("false || false") {
    assert(or(false, false) === false)
  }

  /**
    * This tests the CBN argument of the OR implementation.
    * 
    */
  test("true || loop") {
    assert(or(true, loop) === true)
  }

}
