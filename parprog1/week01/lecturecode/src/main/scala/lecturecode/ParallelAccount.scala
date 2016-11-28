package lecturecode

object ParallelAccount {

  class Account(private var amount: Int = 0) {

    def transfer(target: Account, n: Int) = {
      this.synchronized {
        target.synchronized {
          this.amount -= n
          target.amount += n
        }
      }
    }

  }

}
