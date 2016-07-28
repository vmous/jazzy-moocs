package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class ListSuite extends FunSuite {

  import SortList._

  test("testing list class type variance") {
    val lst = List(8, 9, 4, 1)
    var srt_lst = jazzyinsertsort(lst)

    srt_lst.toSeq should equal (List(1, 4, 8, 9).toSeq)

  }

}
