package lecturecode

object Conditionals {

  /**
    * Function that returns the absolute value of the a given number.
    * 
    * @param x The number whose absolute value we want.
    * 
    * @return The absolute value of the given number. 
    */
  def abs(x: Int): Int = if (x >= 0) x else -x

  /**
    * Defines an infinite loop function
    */
  def loop: Boolean = true && loop

  /**
    * A function implementing the AND boolean operation.
    * 
    * @param x The first operand.
    * @param y The second operand. Note that this is a CBN argument. Rewrite
    * rules dictate that in cases that true&&e (where e is a boolean expression)
    * 
    * @return The result of the AND boolean operation.
    */
  def and(x: Boolean, y: => Boolean) = if (x) y else false

  /**
    * A function implementing the OR boolean operation.
    * 
    * @param x The first operand.
    * @param y The second operand.
    * 
    * @return The result of the OR boolean operation.
    */
  def or(x: Boolean, y: => Boolean) = if (x) true else y

}
