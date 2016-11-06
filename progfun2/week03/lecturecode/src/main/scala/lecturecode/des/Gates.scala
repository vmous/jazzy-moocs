package lecturecode.des

abstract class Gates extends Simulation {

  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGateDelay: Int

  /**
    * A class representing a wire that connects inputs to outputs, inputs to
    * components, components to components or components to outputs.
    */
  class Wire() {

    private var sigVal = false
    private var actions: List[Action] = List()

    /**
      * Returns the current value of the signal transported by the wire.
      */
    def getSignal: Boolean = sigVal

    /**
      * Modifies the value of the signal transported by the wire.
      */
    def setSignal(sig: Boolean): Unit = {
      if (sig != sigVal) {
        sigVal = sig
        // Whenever the signal changes all actions are executed.
        actions foreach (_()) // a.k.a. for (a <- actions) a()
      }
    }

    /**
      * Attaches the specified procedure to the <em>actions</em> of the wire. All
      * of the attached actions are executed at each change of the transported
      * signal.
      *
      * Note that once we prefix the new actions to the list of actions we
      * immediately perform it. The turns out to be technically necessary to get
      * the simulation off the ground because otherwise it turns out that the
      * simulation would essentially rest in an inert state forever.
      */
    def addAction(a: Action): Unit = {
      actions = a :: actions
      a()
    }

  }

  /**
    * NOT gate (inverter).
    *
    * We implement the inverter by installing an action on its input wire.
    * This action produces the inverse of the input signal on the output wire.
    * The change must be effective after a delay of InverterDelay units of
    * simulated time.
    *
    * IN ---|>o--- OUT
    */
  def inverter(input: Wire, output: Wire): Unit = {
    def InvertAction(): Unit = {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) { output setSignal !inputSig }
    }
    input addAction InvertAction
  }

  /**
    * AND gate.
    *
    * We implement the AND gate by installing an action on its input wires.
    * This action produces the conjunction of the input signals on the output
    * wire. This happens after a delay of AndGateDelay units of simulated time.
    *
    * IN1 ---|&&
    *        |&& --- OUT
    * IN2 ---|&&
    */
  def andGate(in1: Wire, in2: Wire, output: Wire): Unit = {
    def AndAction(): Unit = {
      val in1Sig = in1.getSignal
      val in2Sig = in2.getSignal
      afterDelay(AndGateDelay) { output setSignal (in1Sig & in2Sig) }
    }
    in1 addAction AndAction
    in2 addAction AndAction
  }

  /**
    * OR gate.
    *
    * We implement the OR gate by installing an action on its input wires.
    * This action produces the disjunction of the input signals on the output
    * wire. This happens after a delay of OrGateDelay units of simulated time.
    *
    * IN1 ---|OR
    *        |OR --- OUT
    * IN2 ---|OR
    */
  def orGate(in1: Wire, in2: Wire, output: Wire): Unit = {
    def AndAction(): Unit = {
      val in1Sig = in1.getSignal
      val in2Sig = in2.getSignal
      afterDelay(AndGateDelay) { output setSignal (in1Sig | in2Sig) }
    }
    in1 addAction AndAction
    in2 addAction AndAction
  }

  /**
    * OR gate (alternative).
    *
    * Alternative implementation of the OR gate, leveraging:
    *   a | b = ¬ (¬ a & ¬ b)
    *
    * IN1 ----|>o-----|&&
    *                 |&& ----|>o--- OUT
    * IN2 ----|>o-----|&&
    *
    *
    * IN1 ---|OR
    *        |OR --- OUT
    * IN2 ---|OR
    */
  def orGateAlt(in1: Wire, in2: Wire, output: Wire): Unit = {
    val notIn1, notIn2, notOut = new Wire

    inverter(in1, notIn1)
    inverter(in2, notIn2)
    andGate(notIn1, notIn2, notOut)
    inverter(notOut, output)
  }

  /**
    * A probe is a component that let's us see the state of a wire.
    */
  def probe(name: String, wire: Wire): Unit = {
    def ProbeAction(): Unit = {
      println(s"$name $currentTime value = ${wire.getSignal}")
    }
    wire addAction ProbeAction
  }

}
