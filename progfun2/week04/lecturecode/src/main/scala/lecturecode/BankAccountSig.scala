package lecturecode

import frp._

class BankAccountSig {

  val balance = Var(0.0)

  def deposit(amount: Double): Unit = {
    if (amount > 0) {
      val b = balance()
      balance() = b + amount
    }
  }

  def withdraw(amount: Double): Double = {
    if (amount > 0 && balance() >= amount) {
      val b = balance()
      balance.update(b - amount)
      balance()
    }
    else throw new Error("cannot withdraw zero or more than your balance allows")
  }

}

/**
 * The consolidator observes a list of bank accounts and always stays up to
 * date with the total balance of all the bank accounts.
 *
 * Consolidator is a subscriber.
 */
object ConsolidatorSig {

  def consolidated(accts: List[BankAccountSig]): Signal[Double] = {
    Signal(accts.map(_.balance()).sum)
  }

  def exchanged(from: Signal[Double], rate: Signal[Double]): Signal[Double] = {
    Signal(from() * rate())
  }

}
