package lecturecode

object Polynomials {

  class Poly(terms0: Map[Int, Double]) {
    val terms = terms0 withDefaultValue 0.0
    def + (other: Poly) = new Poly(terms ++ (other.terms map adjust))
    def adjust(term: (Int, Double)): (Int, Double) = {
      val (exp, coeff) = term
      exp -> (coeff + terms(exp))
    }

    override def toString = {
      (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff+"x^"+exp) mkString " + "
    }
  }

}
