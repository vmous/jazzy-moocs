package lecturecode

import scala.language.postfixOps

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankAccountSuite extends FunSuite {

  test("testing deposit") {
    val acct = new BankAccount
    acct deposit 50
    assert(acct.getBalance() === 50)
  }

  test("testing withdraw") {
    val acct = new BankAccount
    acct deposit 50
    intercept[Error] {acct withdraw 0}
    intercept[Error] {acct withdraw 100}
    assert((acct withdraw 30) === 20)
  }

  test("testing consolidator") {
    val a, b = new BankAccount
    val c = new Consolidator(List(a, b))

    assert(c.totalBalance === 0)

    a deposit 50
    assert(c.totalBalance === 50)

    b deposit 30
    assert(c.totalBalance === 80)

    a withdraw 20
    b withdraw 25
    assert(c.totalBalance === 35)
  }

}
