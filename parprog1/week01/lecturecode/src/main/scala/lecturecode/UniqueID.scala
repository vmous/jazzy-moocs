package lecturecode

object UniqueID {

  var uidCount = 0L

  def getUniqueId(): Long = {
    uidCount += 1
    uidCount
  }

  private val m = new AnyRef {}
  def getUniqueIdWithMonitor(): Long = m.synchronized {
    uidCount += 1
    uidCount
  }

}
