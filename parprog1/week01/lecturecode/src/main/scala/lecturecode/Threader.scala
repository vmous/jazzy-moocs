package lecturecode

object Threader {

  class HelloThread extends Thread {
    override def run() {
      println("Hello world!")
    }
  }

  class HelloThreadWithHiccups extends Thread {
    override def run() {
      print("Hello ")
      println("world!")
    }
  }

}
