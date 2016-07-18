package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JazzyBooleanSuite extends FunSuite {

  import lecturecode._

  test("testing JazzyBoolean class type AND") {
    assert((_true && _false) === _false)
    assert((_false && _true) === _false)
    assert((_false && _false) === _false)
    assert((_true && _true) === _true)
  }

  test("testing JazzyBoolean class type OR") {
    assert((_true || _false) === _true)
    assert((_false || _true) === _true)
    assert((_false || _false) === _false)
    assert((_true || _true) === _true)
  }

  test("testing JazzyBoolean class type NOT") {
    assert((!_false) === _true)
    assert((!_true) === _false)
  }

  test("testing JazzyBoolean class type EQ") {
    assert((_false == _false) === _true)
    assert((_true == _true) === _true)
    assert((_true == _false) === _false)
    assert((_false == _true) === _false)
  }

  test("testing JazzyBoolean class type NEQ") {
    assert((_false != _false) === _false)
    assert((_true != _true) === _false)
    assert((_true != _false) === _true)
    assert((_false != _true) === _true)
  }

  test("testing JazzyBoolean class type LT") {
    assert((_false < _false) === _false)
    assert((_true < _true) === _false)
    assert((_true < _false) === _false)
    assert((_false < _true) === _true)
  }

}
