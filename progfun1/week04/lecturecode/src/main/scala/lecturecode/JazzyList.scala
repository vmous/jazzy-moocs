package lecturecode

import scala.language.postfixOps

/**
  * A fundamental data structure in many functional languages is the
  * immutable linked list. It is constructed from two building blocks:
  *  Nil   the empty list
  *  Cons  a cell containing an element and the remainder of the list.
  *
  * Note: This implementation is different from last weeks in the following
  * respect:
  * One shortcoming of the previous implementation is that Nil had to be a
  * class, whereas we would prefer it to be an object (after all, there is only
  * one empty list). To change that we make List covariant.
  */
trait JazzyList[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: JazzyList[T]
  def prepend[U >: T](elem: U): JazzyList[U] = new Cons(elem, this)
}

class Cons[T](val head: T, val tail: JazzyList[T]) extends JazzyList[T] {
  def isEmpty: Boolean = false
}

object Nil extends JazzyList[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

