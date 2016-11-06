package lecturecode.des

/**
  * Discrete Event Simulation
  * =========================
  *
  * Digital Circuits
  * ----------------
  * Let's start with a small description language for digital circuits.
  *
  * A digital circuit is composed of <em>wires</em> and of functional components.
  * Wires transport signals that are transformed by components.
  * We represent signals using booleans <em>true</em> and <em>false</em>.
  *
  * The base components (gates) are:
  * <ul>
  *   <li>The <em>Inverter</em>, whose output is the inverse of its output.</li>
  *   <li>The <em>AND Gate</em>, whose output is the conjunction of its inputs.</li>
  *   <li>The <em>OR Gate</em>, whose output is the disjunction of its inputs.</li>
  * </ul>
  *
  * Other components can be constucted by combining these base components.
  *
  * The components have a reaction time (or <em>delay</em>), i.e. their outputs
  * don't change immediately after a change to their inputs.
  *
  * Discrete Event Simulation
  * -------------------------
  * The class <tt>Wire</tt> and the functions <tt>inverter</tt>, <tt>andGate</tt>
  * and <tt>orGate</tt> represent a small description language of digital
  * circuits. Their implementations, based on a simple API for discrete event
  * simulation, allow us to simulate circuits.
  *
  * A discrete event simulator performs <em>actions</em>, specified by the user
  * at a given <em>moment</em>.
  *
  * An <em>action</em> is a function that doesn't take any parameters and which
  * returns <tt>Unit</tt>: <tt>type Actions = () => Unit</tt>
  *
  * The <em>time</em> is simulated; it has nothing to do with the actual time.
  *
  * Design
  * ------
  *
  *              +----------------+
  *              |   Simulation   |
  *              +-------+--------+
  *                      |
  * Wire         +-------+--------+
  * And, Or, Inv |     Gates      |
  *              +-------+--------+
  *                      |
  * HA           +-------+--------+       +-------------+
  * FA           |    Circuits    |       |  Paramters  |
  *              +-------+--------+       +-------+-----+
  *                      |                        |
  *                      +------------------------+
  *                      |
  *              +-------+--------+
  *              |  MySimulation  |
  *              +----------------+
  *
  * Simulation API
  * --------------
  * The idea is to keep in every instance of the Simulation trait an
  * <em>agenda</em> of actions to perform. It is a list of (simulated)
  * <em>events</em>. Each event consists of an action and the time when it must
  * be produced. The agenda list is sorted in such a way that the actions to be
  * performed first are in the beginning.
  */
abstract class Simulation {

  type Action = () => Unit

  case class Event(time: Int, action: Action)

  private var curtime = 0

  /**
    * Returns the current simulated time, in the form of an integer.
    */
  def currentTime: Int = curtime
  private type Agenda = List[Event]

  private var agenda: Agenda = List()

  /**
    * Inserts the event in the right position of the agenda in order to
    * preserve its invariant of always being sorted with the action to be
    * performed first always being the head.
    */
  private def insert(ag: List[Event], item: Event): List[Event] = ag match {
    case first :: rest if first.time <= item.time =>
      first :: insert(rest, item)
    case _ =>
      item :: ag
  }

  /**
    * Registers a body of actions to be performed after delay amount of time
    * after the current simulated time.
    */
  def afterDelay(delay: Int)(block: => Unit): Unit = {
    val item = Event(currentTime + delay, () => block)
    agenda = insert(agenda, item)
  }

  /**
    * The event handling loop removes successive elements from the agenda, and
    * performs the associated actions.
    */
  private def loop(): Unit = agenda match {
    case first :: rest =>
      agenda = rest
      curtime = first.time
      first.action()
      loop()
    case Nil =>
  }

  /**
    * Performs the simulation until no more registered actions remain.
    */
  def run(): Unit = {
    afterDelay(0) {
      println("*** simulation started, time = $currentTime ***")
    }
    loop()
  }

}
