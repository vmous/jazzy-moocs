package lecturecode.des

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GatesSuite extends FunSuite {

  object sim extends Circuits with Parameters
  import sim._

  val trueWire, falseWire, outWire = new Wire
  trueWire setSignal(true)
  falseWire setSignal(false)

  test("Testing Wire") {
    val w = new Wire
    w setSignal(true)
    assert(w.getSignal === true)
    w setSignal(false)
    assert(w.getSignal === false)
  }

  test("Testing NOT gate's truth table") {
    inverter(trueWire, outWire)
    afterDelay(InverterDelay + 1) { assert(outWire.getSignal === false) }
    inverter(falseWire, outWire)
    afterDelay(InverterDelay + 1) { assert(outWire.getSignal === true) }
  }

  test("Testing AND gate's truth table") {
    andGate(trueWire, trueWire, outWire)
    afterDelay(AndGateDelay + 1) { assert(outWire.getSignal === true) }
    andGate(falseWire, trueWire, outWire)
    afterDelay(AndGateDelay + 1) { assert(outWire.getSignal === false) }
    andGate(trueWire, falseWire, outWire)
    afterDelay(AndGateDelay + 1) { assert(outWire.getSignal === false) }
    andGate(falseWire, falseWire, outWire)
    afterDelay(AndGateDelay + 1) { assert(outWire.getSignal === true) }
  }

  test("Testing OR gate's truth table") {
    orGate(trueWire, trueWire, outWire)
    afterDelay(OrGateDelay + 1) { assert(outWire.getSignal === true) }
    orGate(falseWire, trueWire, outWire)
    afterDelay(OrGateDelay + 1) { assert(outWire.getSignal === true) }
    orGate(trueWire, falseWire, outWire)
    afterDelay(OrGateDelay + 1) { assert(outWire.getSignal === true) }
    orGate(falseWire, falseWire, outWire)
    afterDelay(OrGateDelay + 1) { assert(outWire.getSignal === false) }
  }

  test("Testing alternative OR gate's truth table") {
    orGateAlt(trueWire, trueWire, outWire)
    afterDelay(OrGateAltDelay + 1) { assert(outWire.getSignal === true) }
    orGateAlt(falseWire, trueWire, outWire)
    afterDelay(OrGateAltDelay + 1) { assert(outWire.getSignal === true) }
    orGateAlt(trueWire, falseWire, outWire)
    afterDelay(OrGateAltDelay + 1) { assert(outWire.getSignal === true) }
    orGateAlt(falseWire, falseWire, outWire)
    afterDelay(OrGateAltDelay + 1) { assert(outWire.getSignal === false) }
  }

}
