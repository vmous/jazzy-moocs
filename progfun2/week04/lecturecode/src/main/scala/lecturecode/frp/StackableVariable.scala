package lecturecode.frp

/**
  * The class helps us find out on whose behalf a signal expression is evaluated.
  * The stack-like fashion the data structure is accessed, powers the notion
  * that one evaluation of a signal might trigger others.
  * caller.
  *
  * A common, use of the class is to define a global variable, maintaining a
  * stack trace of the callers with some initial signal:
  *
  * <tt>val caller = new StackableVariable(initialSig)</tt>
  *
  * Someone could update the <tt>caller</tt> in a scoped fashion by using
  * <tt>withValue(otherSig)</tt> method call. While the expression after the
  * method evaluates, the caller will have the value <tt>otherSig</tt> from the
  * argument of <tt>withValue</tt> method.
  *
  * <tt>caller.withValue(otherSig) { ??? }</tt>
  *
  * At any point we could get the value simply with the syntax
  *
  * <tt>caller.value</tt>
  */
class StackableVariable[T](init: T) {

  // Here's the stack, maintained as a list of values
  private var values: List[T] = List(init)

  /** The current value is the head of the list */
  def value: T = values.head

  /**
    * Update the caller with aa new value for the signal.
    *
    * Takes a new value and an operation to perform.
    */
  def withValue[R](newValue: T)(op: => R): R = {
    // Puts the value to the top of the stack.
    values = newValue :: values
    // Executes the operation and when done pops the value off the list.
    try op finally values = values.tail
  }

}
