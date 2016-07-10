package lecturecode

import ClassHierarchies._

object Main extends App {
  val t1 = new NonEmpty(3, Empty, Empty)
  println(t1)
  val t2 = t1 incl 4
  println(t2)
}
