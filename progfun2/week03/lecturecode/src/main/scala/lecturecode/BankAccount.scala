package lecturecode

class BankAccount {
  private var balance = 0

  def deposit(amount: Int): Unit = {
    if (amount > 0) balance = balance + amount
  }

  def withdraw(amount: Int): Int = {
    if (amount > 0 && balance >= amount) {
      balance = balance - amount
      balance
    }
    else throw new Error("cannot withdraw zero or more than your balance allows")
  }

  def getBalance() = balance
}
