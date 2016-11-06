package lecturecode.des

/**
  * Technology dependent parameters.
  */
trait Parameters {
  val InverterDelay = 2
  val AndGateDelay = 3
  val OrGateDelay = 5
  val OrGateAltDelay = 2 * InverterDelay + AndGateDelay
  val HalfAdderDelay = OrGateDelay + 2 * AndGateDelay + InverterDelay
  val FullAdderDelay = 2 * HalfAdderDelay + OrGateDelay
}

