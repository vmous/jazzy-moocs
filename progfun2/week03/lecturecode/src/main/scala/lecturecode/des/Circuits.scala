package lecturecode.des

abstract class Circuits extends Gates {

  /**
    * Half adder.
    *
    * a ---+-----|OR       d
    *      |     |OR--------------|&&
    *      |  +--|OR              |&&---- sum
    *      |  |        +--|>o-----|&&
    *      |  |        |       e
    *      +-----|&&   |
    *         |  |&&---+----------------- cout
    * b ------+--|&&
    *
    *
    *      +--------+
    *      |        |
    * a ---+        +--- sum
    *      |   HA   |
    * b ---+        +--- cout
    *      |        |
    *      +--------+
    *
    * S = a | b & ¬ (a & b)
    * C = a & b
    */
  def halfAdder(a: Wire, b: Wire, sum: Wire, cout: Wire): Unit = {
    val d = new Wire()
    val e = new Wire()
    orGate(a, b, d)
    andGate(a, b, cout)
    inverter(cout, e)
    andGate(d, e, sum)
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
