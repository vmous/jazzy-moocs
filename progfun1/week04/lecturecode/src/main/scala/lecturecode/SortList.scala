package lecturecode

/**
  */
object SortList {

  def jazzyinsert(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List(x)
    case y :: ys => if (x <= y) x :: xs else y :: jazzyinsert(x, ys)
  }

  def jazzyinsertsort(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case y :: ys => jazzyinsert(y, jazzyinsertsort(ys))
  }

}
