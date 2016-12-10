package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DataParallelOpsSuite extends FunSuite {

  import DataParallelOps._

  test("testing largest palindrome") {
    val array = (0 until 1000000).toArray
    assert(largestPalidrome(array) === 999999)
  }

}
