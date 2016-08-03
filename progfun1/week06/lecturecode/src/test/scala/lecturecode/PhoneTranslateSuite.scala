package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PhoneTranslateSuite extends FunSuite {

  import PhoneTranslate._

  test("testing phone translate") {
    assert(translate("7225247386") === Set("sack air fun", "pack ah re to", "pack bird to", "Scala ire to", "Scala is fun", "rack ah re to", "pack air fun", "sack bird to", "rack bird to", "sack ah re to", "rack air fun"))
  }

}
