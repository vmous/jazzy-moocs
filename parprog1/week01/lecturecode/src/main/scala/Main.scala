package lecturecode

object Main extends App {

  import Threader._
  import Pi._

  val t = new HelloThread

  t.start()
  t.join()

  // Printing might be get mixed because separate statements
  // in two threads can overlap.
  for (t <- 0 until 10) {
    println("=========")
    val t1 = new HelloThreadWithHiccups
    val t2 = new HelloThreadWithHiccups

    t1.start()
    t2.start()
    t1.join()
    t2.join()
  }

  println("Estimating π (sequentially): " + monteCarloPiSeq(64000))
  println("Estimating π (parallely): " + monteCarloPiPar(64000))
  println("Estimating π (parallely with taskss): " + monteCarloPiParTasks(64000))
}
