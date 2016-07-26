package lecturecode

/**
  * A trait describing an Expression.
  */
trait Expr {
  def eval: Int = this match {
    case Number(n) => n
    case Sum(e1, e2) => e1.eval + e2.eval
  }
}

case class Number(n: Int) extends Expr {
}

case class Sum(e1: Expr, e2: Expr) extends Expr {
}

