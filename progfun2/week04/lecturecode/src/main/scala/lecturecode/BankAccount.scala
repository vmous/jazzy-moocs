package lecturecode

import PubSub._

class BankAccount extends Publisher {
  private var balance = 0

  def currentBallance: Int = balance

  def deposit(amount: Int): Unit = {
    if (amount > 0) balance = balance + amount
    publish()
  }

  def withdraw(amount: Int): Int = {
    if (amount > 0 && balance >= amount) {
      balance = balance - amount
      publish()
      balance
    }
    else throw new Error("cannot withdraw zero or more than your balance allows")
  }

  def getBalance() = balance
}
