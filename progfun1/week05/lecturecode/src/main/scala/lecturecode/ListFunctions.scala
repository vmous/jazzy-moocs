package lecturecode

import scala.math.Ordering

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
  def jazzyinsertsort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = xs match {
    case List() => List()
    case y :: ys =>
      def jazzyinsert(x: T, xs: List[T]): List[T] = xs match {
        case List() => List(x)
        case y :: ys => if (ord.lteq(x, y)) x :: xs else y :: jazzyinsert(x, ys)
      }
      jazzyinsert(y, jazzyinsertsort(ys))
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
  def jazzymergesort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    // n is zero when xs.length is 0 or 1
    if (n == 0) xs
    else {
      def jazzymerge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x, y)) x :: jazzymerge(xs1, ys)
          else y :: jazzymerge(xs, ys1)
      }
      val (fst, snd) = xs splitAt n
      jazzymerge(jazzymergesort(fst), jazzymergesort(snd))
    }
  }

  /**
    * A simple way of the map implementation could be:
    * 
    * abstract class List[T] {
    *   ...
    *   def map[U](f: T => U): List[U] = this match {
    *     case Nil => this
    *     case x :: xs => f(x) :: xs.map(f)
    *   }
    * }
    *
    * (in fact, the actual definition of map is a bit more complicated, because
    * it is tail-recursive, and also because it works for arbitrary collections,
    * not only lists)
    */
  def scaleList(xs: List[Double], factor: Double): List[Double] = xs map(x => x * factor)

  /**
    * A simple way of the filter implementation could be:
    *
    * abstract class List[T] {
    *   ...
    *   def filter(p: T => Boolean): List[T] = this match {
    *     case Nil => this
    *     case x :: xs => if (p(x)) x :: xs.filter(p) else xs.filter(p)
    *   }
    * }
    */
  def posElems(xs: List[Int]): List[Int] = xs filter (x => x > 0)

  def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case y :: ys =>
      val (first, rest) = xs span (z => z == y)
      first :: pack(rest)
  }

  /**
    * Run length encoding, using pack.
    */
  def encode[T](xs: List[T]): List[(T, Int)] =
    pack(xs) map (ys => (ys.head, ys.length))

  /**
    * A simple way of the reduceLeft and foldLeft implementations could be:
    *
    * abstract class List[T] {
    *   ...
    *   def reduceLeft(op: (T, T) => T): T = this match {
    *     case Nil => throw new Error("Nil.reduceLeft")
    *     case x :: xs => (xs foldLeft x)(op)
    *   }
    *   def foldLeft[U](z: U)(op: (U, T) => U): U = this match {
    *     case Nil => z
    *     case x :: xs => (xs foldLeft op(z, x))(op)
    *   }
    * }
    */
  def sum_reduced(xs: List[Int]): Int = (0 :: xs) reduceLeft (_ + _)
  def prod_reduced(xs: List[Int]): Int = (1 :: xs) reduceLeft(_ * _)
  def sum_folded(xs: List[Int]): Int = (xs foldLeft 0) (_ + _)
  def prod_folded(xs: List[Int]): Int = (xs foldLeft 1) (_ * _)


  /**
    * A simple way of the reduceRight and foldRight implementations could be:
    *
    * abstract class List[T] {
    *   ...
    *   def reduceRight(op: (T, T) => T): T = this match {
    *     case Nil => throw new Error("Nil.reduceRight")
    *     case x :: Nil => x
    *     case x :: xs => op(x, xs.reduceRight(op))
    *   }
    *   def foldRight[U](z: U)(op: (T, U) => U): U = this match {
    *     case Nil => z
    *     case x :: xs => op(x, (xs foldRight z)(op))
    *   }
    * }
    *
    * Note: For operations that are associative and commutative, foldLeft and
    *       foldRight are equivalent (even though there may be a difference in
    *       efficiency). For example, replacing foldRigh with foldLeft in the
    *       implementation of concatenation below, will result in a type error.
    *
    * def concat_left[T](xs: List[T], ys: List[T]): List[T] = (xs foldLeft ys)(_ :: _)
    * Value :: is not a member of parameter type T  
    */
  def concat_right[T](xs: List[T], ys: List[T]): List[T] = (xs foldRight ys)(_ :: _)

  def mapFun[T, U](xs: List[T], f: T => U): List[U] = {
    (xs foldRight List[U]())(f(_) :: _)
  }

  def lengthFun[T](xs: List[T]): Int = (xs foldRight 0)((_,acc) => acc + 1)
}
