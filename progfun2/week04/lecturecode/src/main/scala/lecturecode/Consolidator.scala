package lecturecode

import PubSub._

/**
  * The consolidator observes a list of bank accounts and always stays up to
  * date with the total balance of all the bank accounts.
  *
  * Consolidator is a subscriber.
  */
class Consolidator(observed: List[BankAccount]) extends Subscriber {

  // Initially the consolidator subscribes itself to all observed bank accounts.
  observed.foreach(_.subscribe(this))

  // maintain the total balance of all bank accounts.
  private var total: Int = _ // '_' means that the variable is un-initialized.
  // Now the total is initialized.
  compute()

  private def compute() = total = observed.map(_.currentBallance).sum

  def handler(pub: Publisher) = compute()

  def totalBalance = total

}
