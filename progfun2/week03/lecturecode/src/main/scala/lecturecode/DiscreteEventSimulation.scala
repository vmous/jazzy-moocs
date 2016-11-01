package lecturecode

/**
  * DiscreteEventSimulation
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
  */
object DiscreteEventSimulation {

  /**
    * A class representing a wire that connects inputs to outputs, inputs to
    * components, components to components or components to outputs.
    */
  class Wire()

  /**
    * NOT gate (inverter).
    *
    * IN ---|>o--- OUT
    */
  def inverter(input: Wire, output: Wire): Unit = ???

  /**
    * AND gate.
    *
    * IN1 ---|&&
    *        |&& --- OUT
    * IN2 ---|&&
    */
  def andGate(a1: Wire, a2: Wire, output: Wire): Unit = ???

  /**
    * OR gate.
    *
    * IN1 ---|OR
    *        |OR --- OUT
    * IN2 ---|OR
    */
  def orGate(o1: Wire, o2: Wire, output: Wire): Unit = ???

  /**
    * Half adder.
    *
    * a ---+-----|OR       d
    *      |     |OR--------------|&&
    *      |  +--|OR              |&&---- s
    *      |  |        +--|>o-----|&&
    *      |  |        |       e
    *      +-----|&&   |
    *         |  |&&---+----------------- c
    * b ------+--|&&
    *
    *
    *      +--------+
    *      |        |
    * a ---+        +--- s
    *      |   HA   |
    * b ---+        +--- c
    *      |        |
    *      +--------+
    *
    * S = a | b & ¬ (a & b)
    * C = a & b
    */
  def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire): Unit = {
    val d = new Wire()
    val e = new Wire()
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }

  /**
    * Full (1-bit) adder.
    *
    *                            +--------+
    *                            |        |
    * a -------------------------+        +------------ sum
    *                            |   HA   |  c2
    *                        +---+        +------|OR
    *        +--------+      |   |        |      |OR--- cout
    *        |        |  s   |   +--------+   +--|OR
    * b -----+        +------+                |
    *        |   HA   |           c1          |
    * cin ---+        +-----------------------+
    *        |        |
    *        +--------+
    *
    *
    *      +--------+
    *      |        |
    * a ---+        +--- sum
    *      |   FA   |
    * b ---+        +--- cout
    *      |        |
    *      +---+----+
    *          |
    * cin -----+
    */
  def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cout: Wire): Unit = {
    val s = new Wire()
    val c1 = new Wire()
    val c2 = new Wire()
    halfAdder(b, cin, s, c1)
    halfAdder(a, s, sum, c2)
    orGate(c1, c2, cout)
  }

}
