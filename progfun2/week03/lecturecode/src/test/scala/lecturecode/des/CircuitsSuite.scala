package lecturecode.des

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CircuitsSuite extends FunSuite {

  object sim extends Circuits with Parameters
  import sim._

  val trueWire, falseWire, sum, cout = new Wire
  trueWire.setSignal(true)
  falseWire.setSignal(false)

  test("Testing Half Adder") {
    halfAdder(trueWire, trueWire, sum, cout)
    afterDelay(HalfAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === true)) }
    halfAdder(falseWire, trueWire, sum, cout)
    afterDelay(HalfAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === false)) }
    halfAdder(trueWire, falseWire, sum, cout)
    afterDelay(HalfAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === false)) }
    halfAdder(falseWire, falseWire, sum, cout)
    afterDelay(HalfAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === false)) }
  }

  test("Testing Full Adder") {
    fullAdder(trueWire, trueWire, trueWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === true)) }
    fullAdder(trueWire, trueWire, falseWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === true)) }
    fullAdder(falseWire, trueWire, trueWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === true)) }
    fullAdder(falseWire, trueWire, falseWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === false)) }
    fullAdder(trueWire, falseWire, trueWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === true)) }
    fullAdder(trueWire, falseWire, falseWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === false)) }
    fullAdder(falseWire, falseWire, trueWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === true) && (cout.getSignal === false)) }
    fullAdder(falseWire, falseWire, falseWire, sum, cout)
    afterDelay(FullAdderDelay) { assert((sum.getSignal === false) && (cout.getSignal === false)) }
  }

}
