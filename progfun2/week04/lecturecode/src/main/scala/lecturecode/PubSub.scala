package lecturecode

object PubSub {

  /**
    * Publishers maintain internally a set of subscribers. Initially that set
    * is empty.
    */
  trait Publisher {

    private var subs: Set[Subscriber] = Set()

    /**
      * Adds a new subscriber.
      */
    def subscribe(sub: Subscriber): Unit = subs += sub

    /**
      * Remove the given subscriber.
      */
    def unsubscribe(sub: Subscriber): Unit = subs -= sub

    /**
      * Invokes the handler method which a subscriber needs to provide.
      */
    def publish(): Unit = subs.foreach(_.handler(this))

  }


  /**
    * Subscribers subscribe to publishers.
    */
  trait Subscriber {

    /**
      * The handler to be invoked by publishers during publishing.
      */
    def handler(pub: Publisher)

  }

}
