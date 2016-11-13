package lecturecode

object PubSub {

  /**
    * Publishers maintain internally a set of subscribers. Initially that set
    * is empty.
    *
    * <ul>The Good:
    *   <li>we have views that are decoupled from the state,</li>
    *   <li>we can have a varying number of views of a given state,</li>
    *   <li>overall rather simple and intuitive to set up<li>
    * </ul>
    * <ul>The Bad:
    *   <li>
    *     forces imperative style (all the fundamental operations publish,
    *     subscribe, handler are <tt>Unit</tt>-typed and they return nothing as
    *     a result so everything they do has to be by imperative updates),
    *   </li>
    *   <li>
    *     many moving parts that need to be coordinated (e.g. every subscriber
    *     has to announce itself to the publisher with subscribe and or the
    *     publisher has to handle subscribers with back and forth calls),
    *   </li>
    *   <li>
    *     Concurrency makes things more complicated (i.e having a single view
    *     that observes two different models that could get updated concurrently
    *     introduces possible race conditions that have to be handled),
    *   </li>
    *   <li>
    *     Views are still tightly bound to the state represented in the model;
    *     every view update is directly coupled to the state update (we update
    *     the state the view gets updated immediately which might not be what we
    *     want to have sometimes
    *   </li>
    * </ul>
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
    def handler(pub: Publisher): Unit

  }

}
