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
  def WHILE(condition: => Boolean)(command: => Unit): Unit = {
    if (condition) {
      command
      WHILE(condition)(command)
    }
    else ()
  }

}
