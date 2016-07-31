package lecturecode

object Collections {

  /**
    * List of all combination of of numbers x and y where x is drawn from 1..M
    * and y is drawn from 1..N
    */
  def combinations(xr: Range, yr: Range) = {
    xr flatMap (x =>
      yr map (y => (x, y))
    )
  }

}
