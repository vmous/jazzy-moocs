package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ListSuite extends FunSuite {

  import List._

  test("testing list class type variance") {
    val x: List[String] = Nil
  }

}
