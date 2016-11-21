package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Signal(scala.math.pow(b(), 2) - 4 * a() * c())
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    Signal(
      delta() match {
        case c if c < 0 => Set()
        case c if c == 0 => Set( -b() / 2.0 * a() )
        case _ => Set(
          ((-b() + scala.math.sqrt(delta())) / (2.0 * a())),
          ((-b() - scala.math.sqrt(delta())) / (2.0 * a()))
        )
      }
    )
  }
}
