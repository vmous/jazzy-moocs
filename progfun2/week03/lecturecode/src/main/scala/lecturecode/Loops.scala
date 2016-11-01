package lecturecode

/**
  * Loops
  */
object Loops {

  /**
    * Functional implementation of the language construct/keyword "while".
    *
    * Emulates while-loops with just a function definition in the call.
    *
    * Notes:
    * i.  Parameters are passed by name and not by value in order to ensure
    *     re-evaluation in each iteration. Otherwise, if, for example, condition
    *     was passed by value and initially true, the while loop would loop
    *     forever because the condition would never be re-evaluated to be false.
    * ii. The implementation is tail recursive, so it can operate with a constant
    *     stack size.
    */
  def WHILE(condition: => Boolean)(body: => Unit): Unit = {
    if (condition) {
      body
      WHILE(condition)(body)
    }
    else ()
  }

  /**
    * Functional implementation of the language cunstruct/keyword "repeat".
    *
    * Check notes above.
    */
  def REPEAT(body: => Unit)(condition: => Boolean): Unit = {
    body
    if (condition) ()
    else REPEAT(body)(condition)
  }

  /**
    * Functional implementation of the language cunstruct/keyword "repeat-until".
    *
    * Check notes above.
    */
  def REPEAT_(body: => Unit) = new {
    def UNTIL(condition: => Boolean): Unit = {
      REPEAT {
        body
      } (condition)
    }
  }

  /**
    * Functional implementation of the language construct/keyword "for".
    *
    * Note: Not exactly the implementation I'd like since it does not support
    * multiple ranges and variable i to be available in the for body.
    *
    * Traditional for-loops *cannot* be implemented simply be high-order
    * functions. Why? Take a look at example:
    *   for (int i = 0; i < 3; i = i + 1) { System.out.println(i + " "); }
    *
    * you note that arguments contain a variable declaration (i), which is
    * visible in other arguments and in the body.
    *
    * Scala on the other hand a kind of for-loop similar to Java's extended
    * for-loops:
    *   for (i <- 1 until 3) { System.out.println(i + " ") }
    *
    * This looks pretty much like a for-expression. For-loops translate quite
    * similarly to for-expressions, but where for-expressions translate into
    * combinations of functions map and flatMap, for-loops translate into
    * combinations of the function foreach<sup>*</sup>.
    *
    * <sup>*</sup> foreach is a function that's defined on all collections with
    * elements of type T. Its signature is (T => Unit) => Unit and its effect is
    * to apply the given function argument f to each element of the collection.
    */
  def FOR(seq: Seq[Int])(body: => Unit): Unit = {
    seq.foreach(_ => body)
  }

}
