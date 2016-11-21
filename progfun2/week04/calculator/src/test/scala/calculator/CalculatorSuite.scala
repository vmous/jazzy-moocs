package calculator

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest._

import TweetLength.MaxTweetLength
import Polynomial._
import Calculator._

@RunWith(classOf[JUnitRunner])
class CalculatorSuite extends FunSuite with ShouldMatchers {

  /******************
   ** TWEET LENGTH **
   ******************/

  def tweetLength(text: String): Int =
    text.codePointCount(0, text.length)

  test("tweetRemainingCharsCount with a constant signal") {
    val result = TweetLength.tweetRemainingCharsCount(Var("hello world"))
    assert(result() == MaxTweetLength - tweetLength("hello world"))

    val tooLong = "foo" * 200
    val result2 = TweetLength.tweetRemainingCharsCount(Var(tooLong))
    assert(result2() == MaxTweetLength - tweetLength(tooLong))
  }

  test("tweetRemainingCharsCount with a supplementary char") {
    val result = TweetLength.tweetRemainingCharsCount(Var("foo blabla \uD83D\uDCA9 bar"))
    assert(result() == MaxTweetLength - tweetLength("foo blabla \uD83D\uDCA9 bar"))
  }


  test("colorForRemainingCharsCount with a constant signal") {
    val resultGreen1 = TweetLength.colorForRemainingCharsCount(Var(52))
    assert(resultGreen1() == "green")
    val resultGreen2 = TweetLength.colorForRemainingCharsCount(Var(15))
    assert(resultGreen2() == "green")

    val resultOrange1 = TweetLength.colorForRemainingCharsCount(Var(12))
    assert(resultOrange1() == "orange")
    val resultOrange2 = TweetLength.colorForRemainingCharsCount(Var(0))
    assert(resultOrange2() == "orange")

    val resultRed1 = TweetLength.colorForRemainingCharsCount(Var(-1))
    assert(resultRed1() == "red")
    val resultRed2 = TweetLength.colorForRemainingCharsCount(Var(-5))
    assert(resultRed2() == "red")
  }

  /***************************
   ** 2ND ORDER POLYNOMIALS **
   ***************************/

  test("test 2nd order polynomial with zero solutions") {
    val a = Var(1.0)
    val b = Var(1.0)
    val c = Var(1.0)
    val delta = computeDelta(a, b, c)
    val solutions = computeSolutions(a, b, c, delta)
    assert(solutions().size === 0)
    assert(solutions() === Set())
  }

  test("test 2nd order polynomial with one solution") {
    val a = Var(3.0)
    val b = Var(6.0)
    val c = Var(3.0)
    val delta = computeDelta(a, b, c)
    val solutions = computeSolutions(a, b, c, delta)
    assert(solutions().size === 1)
    assert(solutions() === Set(-9.0))
  }

  test("test 2nd order polynomial with two solutions") {
    val a = Var(5.0)
    val b = Var(6.0)
    val c = Var(1.0)
    val delta = computeDelta(a, b, c)
    val solutions = computeSolutions(a, b, c, delta)
    assert(solutions().size === 2)
    assert(solutions() === Set(-0.2, -1.0))
  }

  test("test 2nd order polynomial as signals") {
    val a = Var(1.0)
    val b = Var(1.0)
    val c = Var(1.0)
    val delta = computeDelta(a, b, c)
    val solutions = computeSolutions(a, b, c, delta)
    assert(solutions().size === 0)
    assert(solutions() === Set())

    a.update(3.0)
    b.update(6.0)
    c.update(3.0)
    assert(solutions().size === 1)
    assert(solutions() === Set(-9.0))

    a.update(5.0)
    c.update(1.0)
    assert(solutions().size === 2)
    assert(solutions() === Set(-0.2, -1.0))
  }

  /****************
   ** CALCULATOR **
   ****************/

  test("test calculator") {
    assert(true)
  }

}
