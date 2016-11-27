package lecturecode

object UniqueID {

  var uidCount = 0L

  def getUniqueId(): Long = {
    uidCount += 1
    uidCount
  }

}
