package lecturecode.frp

/**
  * Var is a signal that can be updated by the client program.
  */
class Var[T](expr: => T) extends Signal[T](expr) {

  /**
    * Updates the expression to be evaluated.
    *
    * We override the method and make it public so that client of Var can use
    * update.
    */
  override def update(expr: => T): Unit = super.update(expr)

}

object Var {
  def apply[T](expr: => T) = new Var(expr)
}
