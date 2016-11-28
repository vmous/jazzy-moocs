package lecturecode

import UniqueID._

object ParallelAccount {

  class Account(private var amount: Int = 0) {

    val uid = getUniqueIdWithMonitor()

    private def lockAndTransfer(target: Account, n: Int) = {
      this.synchronized {
        target.synchronized {
          this.amount -= n
          target.amount += n
        }
      }
    }

    def transfer(target: Account, n: Int) = {
      if (this.uid < target.uid) this.lockAndTransfer(target, n)
      else target.lockAndTransfer(this, -n)
    }

    def getAmount(): Int = amount

  }

}
