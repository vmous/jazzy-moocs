package lecturecode

object Main extends App {

  import Threader._

  val t = new HelloThread

  t.start()
  t.join()
}
