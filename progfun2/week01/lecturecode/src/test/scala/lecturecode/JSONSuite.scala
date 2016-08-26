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

    assert(data.show == "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"address\":{\"streetAddress\":\"21 2nd Street\",\"state\":\"NY\",\"postalCode\":10021.0},\"phoneNumbers\":[{\"type\":\"home\",\"number\":\"212 555-1234\"},{\"type\":\"fax\",\"number\":\"646 55-4567\"}]}")
  }

}
