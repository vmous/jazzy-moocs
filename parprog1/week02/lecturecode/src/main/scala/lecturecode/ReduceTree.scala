package lecturecode

import common.parallel

object ReduceTree {

  sealed abstract class Tree[A] { val size: Int }

  case class Leaf[A](a: A) extends Tree[A] {
    override val size = 1
  }

  case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A] {
    override val size = l.size + r.size
  }

  def toList[A](t: Tree[A]): List[A] = t match {
    case Leaf(v) => List(v)
    case Node(l, r) => toList[A](l) ++ toList[A](r)
  }

  def mapTreeSeq[A, B](t: Tree[A], f: A => B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Node(l, r) => Node(mapTreeSeq[A, B](l, f), mapTreeSeq[A, B](r, f))
  }

  def mapTreePar[A, B](t: Tree[A], f: A => B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Node(l, r) => {
      val (lb, rb) = parallel(mapTreePar[A, B](l, f), mapTreePar[A, B](r, f))
      Node(lb, rb)
    }
  }

  def reduceTreeSeq[A](t: Tree[A], f: (A, A) => A): A = t match {
    case Leaf(v) => v
    case Node(l, r) => f(reduceTreeSeq[A](l, f), reduceTreeSeq[A](r, f))
  }

  def reduceTreePar[A](t: Tree[A], f: (A, A) => A): A = t match {
    case Leaf(v) => v
    case Node(l, r) => {
      val (lv, rv) = parallel(reduceTreePar(l, f), reduceTreePar(r, f))
      f(lv, rv)
    }
  }

}
