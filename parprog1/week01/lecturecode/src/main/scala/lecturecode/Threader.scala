package lecturecode

object Threader {

  class HelloThread extends Thread {
    override def run() {
      println("Hello world!")
    }
  }

}
