package lecturecode

//import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JSONSuite extends FunSuite {

  test("testing JSON show") {
    // { "firstName" : "John",
    //   "lastName" : "Smith",
    //   "address" : {
    //     "streetAddress" : "21 2nd Street",
    //     "state" : "NY",
    //     "postalCode" : "10021"
    //   },
    //   "phoneNumbers" : [
    //     { "type" : "home", "number" : "212 555-1234" },
    //     { "type" : "fax", "number" : "646 555-4567" }
    //   ]
    // }
    val data = JObj(Map(
      "firstName" -> JStr("John"),
      "lastName" -> JStr("Smith"),
      "address" -> JObj(Map(
        "streetAddress" -> JStr("21 2nd Street"),
        "state" -> JStr("NY"),
        "postalCode" -> JNum(10021)
      )),
      "phoneNumbers" -> JSeq(List(
        JObj(Map(
          "type" -> JStr("home"), "number" -> JStr("212 555-1234")
        )),
        JObj(Map(
          "type" -> JStr("fax"), "number" -> JStr("646 55-4567")
        ))
      ))
    ))

    assert(data.show === "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"address\":{\"streetAddress\":\"21 2nd Street\",\"state\":\"NY\",\"postalCode\":10021.0},\"phoneNumbers\":[{\"type\":\"home\",\"number\":\"212 555-1234\"},{\"type\":\"fax\",\"number\":\"646 55-4567\"}]}")
  }

  test("Test JSON query") {
    // { "firstName" : "John",
    //   "lastName" : "Smith",
    //   "address" : {
    //     "streetAddress" : "21 2nd Street",
    //     "state" : "NY",
    //     "postalCode" : "10021"
    //   },
    //   "phoneNumbers" : [
    //     { "type" : "home", "number" : "+1 212 555-1234" },
    //     { "type" : "fax", "number" : "+1 646 555-4567" }
    //   ]
    // }
    // { "firstName" : "Vassilis",
    //   "lastName" : "Moustakas",
    //   "address" : {
    //     "streetAddress" : "Aristotelous 1",
    //     "state" : "Athens, GR",
    //     "postalCode" : "10432"
    //   },
    //   "phoneNumbers" : [
    //     { "type" : "home", "number" : "+30 210 523-3234" }
    //   ]
    // }
    val data = List(
      JObj(Map(
        "firstName" -> JStr("John"),
        "lastName" -> JStr("Smith"),
        "address" -> JObj(Map(
          "streetAddress" -> JStr("21 2nd Street"),
          "state" -> JStr("NY"),
          "postalCode" -> JNum(10021)
        )),
        "phoneNumbers" -> JSeq(List(
          JObj(Map(
            "type" -> JStr("home"), "number" -> JStr("+1 212 555-1234")
          )),
          JObj(Map(
            "type" -> JStr("fax"), "number" -> JStr("+1 646 55-4567")
          ))
        ))
      )),
      JObj(Map(
        "firstName" -> JStr("Vassilis"),
        "lastName" -> JStr("Moustakas"),
        "address" -> JObj(Map(
          "streetAddress" -> JStr("Aristotelous 1"),
          "state" -> JStr("Athens, GR"),
          "postalCode" -> JNum(10432)
        )),
        "phoneNumbers" -> JSeq(List(
          JObj(Map(
            "type" -> JStr("work"), "number" -> JStr("+30 210 523-3234")
          ))
        ))
      ))
    )

    val res = for {
      JObj(bindings) <- data
      JSeq(phones) = bindings("phoneNumbers")
      JObj(phone) <- phones
      JStr(digits) = phone("number")
      if digits startsWith "+30"
    } yield (bindings("firstName"), bindings("lastName"))

    assert(res === List((JStr("Vassilis"), JStr("Moustakas"))))
  }

}
