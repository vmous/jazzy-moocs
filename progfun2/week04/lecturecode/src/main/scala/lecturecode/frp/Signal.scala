package lecturecode.frp

/**
  * <tt>frp.Signal</tt> is modeled after Scala.react, which is described in the paper
  * "Deprecating the Observer Pattern"
  * https://infoscience.epfl.ch/record/148043/files/DeprecatingObserversTR2010.pdf?version=1
  *
  * <ul>Each <tt>Signal</tt> maintains:
  *   <li>its current value,</li>
  *   <li>the current expression that defines the signal value,</li>
  *   <li>
  *      a set of <em>observers</em> (so that if the signal changes, all the
  *      observers need to change)
  *   </li>
  * </ul>
  *
  * <ul>How do we record dependencies in observers?
  *   <li>
  *     when evaluating a signal-valued expression, we need to know which signal
  *     caller gets defined or updated by the expression,
  *   </li>
  *   <li>
  *     if we know that, then executing a <tt>sig()</tt> means adding caller
  *     to the observers of <tt>sig</tt>,
  *   </li>
  *   <li>
  *     when signal <tt>sig</tt>'s value changes, all previously observing
  *     signals are re-evaluated and the set <tt>sig.observers</tt> is cleared,
  *   </li>
  *   <li>
  *     re-evaluation wil re-enter a calling signal caller in
  *     <tt>sig.observers</tt>, as long as caller's calue still depends on
  *     <tt>sig</tt>
  *   </li>
  * </ul>
  */
class Signal[T](expr: => T) {

  import Signal._

  // The current value
  private var myValue: T = _

  // The current expression that defines the signal value
  private var myExpr: () => T = _

  // The set of observers
  private var observers: Set[Signal[_]] = Set()

  update(expr)

  /**
    * Used to initilize a signal and whenever someone assigns a new value to the
    * signal.
    *
    * We define the method as protected in this class so tha clients of the class
    * cannot call it, thus ensuring signal is immutable.
    */
  protected def update(expr: => T): Unit = {
    // Takes the expression to evaluate the new signal
    myExpr = () => expr
    // Computes the current value of the signal
    computeValue()
  }

  /**
    * Computes the current value of the signal.
    *
    * <ul>A signal's current value can change when:
    *   <li>somebody calls an update operation on a <tt>Var</tt>, or</li>
    *   <li>the value of the dependent signal changes (propagation)</li>
    * </ul>
    */
  protected def computeValue(): Unit = {
    // Evaluates the current expression with the current signal as the caller
    // and assigns the value to the state for the signal.
    val newValue = caller.withValue(this)(myExpr())
    if (myValue != newValue) {
      myValue = newValue
      val obs = observers
      observers = Set()
      obs.foreach(_.computeValue())
    }
  }

  /** Gives you the current value of the signal. */
  def apply(): T = {
    observers += caller.value
    // the below assertion catches s() = s() + 1
    assert(!caller.value.observers.contains(this), "cyclic signal definition")
    myValue
  }

}

/**
  * This is the "sentinel" object we need in order to be able to evaluate
  * expressions at the top level when there is not other signal that's defined
  * or updated.
  */
object NoSignal extends Signal[Nothing](???) {
  override def computeValue() = ()
}

/**
  * The object signal that is used to map signals or create constant signals.
  *
  * When evaluating an expression, we need to know on whose behalf a signal
  * expression is evaluated (who is the <tt>caller</tt>).
  *
  * <strong>Attempt #1</strong>
  *
  * A simplistic way to do this is by maintaining a global data structure, that
  * refers to the current caller and that we can update as we evaluate signals.
  * To do so, the structure should be accessed in a stack-like fashion because
  * one evaluation of a signal might trigger the update or redefinition of other
  * signals.
  *
  * <strong>Attempt #2</strong>
  *
  * Global variables though are not considered best practice. Especially, in a
  * multi-threaded environment where we want to evaluate several signal
  * expressions in parallel, we will get race conditions with unpredictable
  * results.
  * We can avoid the use of global state still preserving the necessary
  * protection with synchronization. The later can introduce problems itself like
  * starvation and deadlocks but we can avoid that by using thread-local state.
  * This mean that each thread will have access to its own copy of the variable.
  * Thread-local state is supported in Java through
  * <tt>java.util.ThreadLocal</tt>. In Scala it is wrapped with class
  * <tt>scala.util.DynamicVariable</tt>.
  * <ul>Thread-local state still comes with a number of disadvantages:
  *   <li>
  *     its imperative nature often produces hidden dependencies which are
  *     hard to manage (shared with the global variable),
  *   </li>
  *   <li>
  *     its implementation on the JDK involves a global hash table lookup which
  *     can be a performance problem, and
  *   </li>
  *   <li>
  *     it does not play well in situations where threads are multiplexed between
  *     several tasks.
  *   </li>
  * </ul>
  *
  * <strong>Attempt #3</strong>
  *
  * TODO
  *
  * A cleaner solution involves implicit parameters: instead of maintaining a
  * thread-local variable, pass its current value into a signal expression as an
  * implicit parameter. This is purely functional but it currently requires more
  * boilerplate than the thread-local solution. Future versions of Scala might
  * solve this problem.
  */
object Signal {

  import scala.util.DynamicVariable

  /* The '_' in the type of the stackable variable denotes that it can take a
   signal of any type. */
  //private val caller = new StackableVariable[Signal[_]](NoSignal)

  /* As mentioned we want to replace the global variable with a thread-local
   variable <tt>scala.util.DynamicVariable</tt>. Actually we have engineered
   <tt>StackableVariable</tt>'s API, we used above, to match exactly the API of
   <tt>DynamicVariable</tt>. So we simply the use of the former with the
   latter.*/
  private val caller = new DynamicVariable[Signal[_]](NoSignal)

  /**
    * Maps signals or create constant signals.
    *
    * Recall that if an object has an <tt>apply</tt> method, it enables the
    * following syntax:
    *
    * <tt>Signal(expr)</tt>
    */
  def apply[T](expr: => T) = new Signal(expr)

}
