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

}
