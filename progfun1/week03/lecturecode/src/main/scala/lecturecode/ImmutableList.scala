package lecturecode

import scala.language.postfixOps

/**
  * A fundamental data structure in man functional languages is the
  * immutable linked list. It is constructed from two building blocks:
  *  Nil   the empty list
  *  Cons  a cell containing an element and the remainder of the list.
  */
object ImmutableList {

  trait List[T] {
    def isEmpty: Boolean
    def head: T
    def tail: List[T]
  }

  class Cons[T](val head: T, val tail: List[T]) extends List[T] {
    def isEmpty: Boolean = false
  }

  class Nil[T] extends List[T] {
    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException("Nil.head")
    def tail: Nothing = throw new NoSuchElementException("Nil.tail")
  }

  def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

  def nth[T](n: Int, xs: List[T]): T = {
    if (xs isEmpty) throw new IndexOutOfBoundsException
    else if (n == 0) xs.head
    else nth(n - 1, xs.tail)
  }

}
