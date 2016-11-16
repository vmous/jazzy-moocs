package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankAccountSigSuite extends FunSuite {

  import ConsolidatorSig._
  import frp._

  test("testing deposit") {
    val acct = new BankAccountSig
    acct deposit 50
    assert(acct.balance() === 50)
  }

  test("testing withdraw") {
    val acct = new BankAccountSig
    acct deposit 50
    intercept[Error] { acct withdraw 0 }
    intercept[Error] { acct withdraw 100 }
    assert((acct withdraw 30) === 20)
  }

  test("testing consolidator") {
    val a, b = new BankAccountSig
    val c = consolidated(List(a, b))

    assert(c() === 0)

    a deposit 50
    assert(c() === 50)

    b deposit 30
    assert(c() === 80)

    a withdraw 20
    b withdraw 25
    assert(c() === 35)
  }

  test("testing euro to drachma exchange rate") {
    val a, b = new BankAccountSig
    val c = consolidated(List(a, b))
    val inDrachma = exchanged(c, Signal(340.75))

    assert(inDrachma() === 0)

    a deposit 50
    assert(inDrachma() === 17037.5)

    b deposit 30
    assert(inDrachma() === 27260)

    a withdraw 20
    b withdraw 25
    assert(inDrachma() === 11926.25)
  }

}
