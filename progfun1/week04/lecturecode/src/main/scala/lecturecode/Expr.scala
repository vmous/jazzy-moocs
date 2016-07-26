package lecturecode

/**
  * A trait describing an Expression.
  */
trait Expr {
  def eval: Int = this match {
    case Number(n) => n
    case Variable(x) => throw new Error("Cannot evaluate until \"" + x + "\" has a value") 
    case Sum(e1, e2) => e1.eval + e2.eval
    case Prod(e1, e2) => e1.eval * e2.eval
  }

  def show: String = this match {
    case Number(n) => n.toString()
    case Variable(x) => x
    case Sum(e1, e2) => e1.show + " + " + e2.show
    case Prod(e1, e2) => (e1, e2) match {
      case (Sum(_, _), _) => "(" + e1.show + ")" + " * " + e2.show
      case (_, Sum(_, _)) => e1.show + " * " + "(" + e2.show + ")"
      case (_, _) => e1.show + " * " + e2.show
    }
  }
}

case class Number(n: Int) extends Expr {
}

case class Variable(x: String) extends Expr {
}

case class Sum(e1: Expr, e2: Expr) extends Expr {
}

case class Prod(e1: Expr, e2: Expr) extends Expr {
}
