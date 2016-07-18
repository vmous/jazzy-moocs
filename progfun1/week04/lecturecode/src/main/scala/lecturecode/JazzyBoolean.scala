package lecturecode

object _true extends JazzyBoolean {
  def ifThenElse[T](t: => T, e: => T) = t
}

object _false extends JazzyBoolean {
  def ifThenElse[T](t: => T, e: => T) = e
}

abstract class JazzyBoolean {
  /**
    * if (cond) te else ee
    * cond.ifThenElse(te, ee)
    */
  def ifThenElse[T](t: => T, e: => T): T

  def && (x: => JazzyBoolean): JazzyBoolean = ifThenElse(x, _false)
  def || (x: => JazzyBoolean): JazzyBoolean = ifThenElse(_true, x)
  def unary_! : JazzyBoolean = ifThenElse(_false, _true)

  def == (x: JazzyBoolean): JazzyBoolean = ifThenElse(x, x.unary_!)
  def != (x: JazzyBoolean): JazzyBoolean = ifThenElse(x.unary_!, x)

  def < (x: JazzyBoolean): JazzyBoolean = ifThenElse(_false, x)
}
