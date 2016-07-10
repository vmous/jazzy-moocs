package lecturecode

import ClassHierarchies._

object Main extends App {
  val t1 = new NonEmpty(3, Empty, Empty)
  println(t1)
  val t2 = t1 incl 4
  println(t2)

  val t3 = new NonEmpty(7, new NonEmpty(2, Empty, Empty), new NonEmpty(8, Empty, Empty))
  val t4 = new NonEmpty(3, new NonEmpty(2, Empty, Empty), new NonEmpty(5, Empty, Empty))
  println(t3 union t4)

  val t5 = new NonEmpty(4, new NonEmpty(2, Empty, Empty), new NonEmpty(9, new NonEmpty(7, Empty, Empty), Empty))
  val t6 = new NonEmpty(3, Empty, new NonEmpty(4, Empty, Empty))
  val t7 = new NonEmpty(3, new NonEmpty(1, Empty, Empty), Empty)
  println(t5 intersection t6)
  println(t5 intersection t7)

  println(t2)
  println(t2 excl 3)
}
