package lecturecode

/**
  * Check
  * https://github.com/vmous/jazzy-moocs/blob/master/progfun1/week01/lecturecode/src/main/scala/lecturecode/SquareRoot.scala
  * for the initial implementation without streams.
  */
object SquareRootStream {

  def sqrtStream(x: Double): Stream[Double] = {

    def improve(guess: Double): Double = {
      (guess + x / guess) / 2
    }

    lazy val guesses: Stream[Double] = 1 #:: (guesses map improve)

    guesses
  }

}
