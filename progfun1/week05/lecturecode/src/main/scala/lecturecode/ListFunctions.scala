package lecturecode

object ListFunctions {

  /**
    * O(1)
    */
  def head[T](xs: List[T]): T = xs match {
    case Nil => throw new Error("head of empty list")
    case List(y) => y
    case y :: ys => y
  }

  /**
    * O(1)
    */
  def tail[T](xs: List[T]): List[T] = xs match {
    case Nil => throw new Error("tail of empty list")
    case List(y) => Nil
    case y :: ys => ys
  }

  /**
    * O(|xs|)
    */
  def last[T](xs: List[T]): T = xs match {
    case Nil => throw new Error("last of empty list")
    case List(y) => y 
    case y :: ys => last(ys)
  }

  /**
    * O(|xs|)
    */
  def init[T](xs: List[T]): List[T] = xs match {
    case Nil => throw new Error("init of the empty list")
    case List(y) => Nil
    case y :: ys => y :: init(ys)
  }

  /**
    * O(|xs|)
    */
  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case Nil => ys
    //case z :: zs => z :: zs :: ys
    case z :: zs => z :: concat(zs, ys)
  }

  /**
    * 0(n*n)! This is due to the immutable nature of the list implementation.
    */
  def reverse[T](xs: List[T]): List[T] = xs match {
    case Nil => xs
    case y :: ys => reverse(ys) ++ List(y)
  }


  /**
    * O(n)
    */
  def removeAt[T](xs: List[T], n: Int): List[T] = {
    (xs take n) ::: (xs drop n + 1)
  }


  /**
    * O(n)
    */
  def flatten(xs: List[Any]): List[Any] = xs match {
    case Nil => Nil
    case y :: ys => (y match {
      case l: List[Any] => flatten(l)
      case i => List(i)
    }) ::: flatten(ys)
  }

  /**
    * Insertion sort implementation.
    *
    * O(n*n)
    */
  def jazzyinsertsort[T](xs: List[T])(lte: (T, T) => Boolean): List[T] = xs match {
    case List() => List()
    case y :: ys =>
      def jazzyinsert(x: T, xs: List[T]): List[T] = xs match {
        case List() => List(x)
        case y :: ys => if (lte(x, y)) x :: xs else y :: jazzyinsert(x, ys)
      }
      jazzyinsert(y, jazzyinsertsort(ys)(lte))
  }

  /**
    * Merge sort implementation.
    *
    * If the list contains zero elements, it is already sorted.
    * Otherwise:
    *  - separate the list into two sublists, each containing around half of the
    *    elements of the original list
    *  - sort the two sublists
    *  - merge the two sorted sublists into a single sorted list.
    */
  def jazzymergesort[T](xs: List[T])(lt: (T, T) => Boolean): List[T] = {
    val n = xs.length/2
    // n is zero when xs.length is 0 or 1
    if (n == 0) xs
    else {
      def jazzymerge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (lt(x, y)) x :: jazzymerge(xs1, ys)
          else y :: jazzymerge(xs, ys1)
      }
      val (fst, snd) = xs splitAt n
      jazzymerge(jazzymergesort(fst)(lt), jazzymergesort(snd)(lt))
    }
  }
}
