package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WaterPouringSuite extends FunSuite {

  import WaterPouring._

  test("testing water pouring 4dl and 7dl with target 6dl") {
    val problem = new Pouring(Vector(4, 7))

    assert(problem.solutions(6).toString() === "Stream(Fill(1) Poor(1,0) Empty(0) Poor(1,0) Fill(1) Poor(1,0)--> Vector(4, 6), ?)")
  }

}
