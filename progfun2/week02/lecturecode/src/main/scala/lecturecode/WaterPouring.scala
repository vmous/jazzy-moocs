package lecturecode

object WaterPouring {

  /**
   * @param capacity: Vector of all glasses and their capacity.
   *
   * There is a Python solution to the "water pouring problem" in
   * Peter Norvig's Design of Computer Programs course in Udacity.com.
   */
  class Pouring(capacity: Vector[Int]) {
    // State

    // state is a vector of integers
    type State = Vector[Int]
    // will consist of all glasses emtpy
    val initialState = capacity map (_ => 0)

    // Moves
    trait Move {
      def change(state: State): State
    }

    case class Empty(glass: Int) extends Move {
      def change(state: State): State = state updated (glass, 0)
    }

    case class Fill(glass: Int) extends Move {
      def change(state: State): State = state updated (glass, capacity(glass))
    }

    case class Poor(from: Int, to: Int) extends Move {
      def change(state: State): State = {
        val amount = state(from) min (capacity(to) - state(to))
        state updated (from, state(from) - amount) updated (to, state(to) + amount)
      }
    }

    // Auxialiary data stracture that for all glasses
    val glasses = 0 until capacity.length

    val moves =
      (for (g <- glasses) yield Empty(g)) ++
        (for (g <- glasses) yield Fill(g)) ++
        (for (from <- glasses; to <- glasses if from != to) yield Poor(from, to))

    // Paths
    /**
     * A path is defined by its history which is a series of moves.
     *
     * @param history History of moves. The history is taken in reverse in
     * order to make it easier to extend a path with a new move. This means
     * the last move in the path comes first in the history list.
     *
     * @param endState A value parameter containing the end state of the path.
     * In the previous implementation we had a function computing the end state
     * each time it was needed (check ). As the implementation shows, the endState
     * function was called multiple times so saving it in a path's val
     * parameter will save
     */
    class Path(history: List[Move], val endState: State) {

      /**
       * CAUTION: Deprecated. Check documentation for endState val parameter
       * in the class.
       *
       * Returns the state in which a path leads to.
       *
       * One way to implement end state can be the following:
       *
       * def endState: State = trackState(history)
       * private trackSteate(xs: List[Move]): State = xs match {
       *   case Nil => initialState
       *   case move :: xs1 => move change trackState(xs1)
       * }
       *
       * If you look closely the above code is doing the follwing list generation:
       *
       *           change
       *          /     \
       *      move       \
       *                change
       *                /    \
       *            move      \
       *                      change
       *                      /    \
       *                  move      \
       *                             .
       *                              .
       *                               .
       *                                \
       *                                initialState
       *
       * this computation is what makes a foldRight thus the final implementation style bellow.
       */
      //def endState: State = (history foldRight initialState)(_ change _)

      /**
       * Extend the path with another move
       */
      def extend(move: Move): Path = new Path(move :: history, move change endState)

      override def toString = (history.reverse mkString " ") + "--> " + endState
    }

    val initialPath = new Path(Nil, initialState)

    /**
     * A function that extends a given set of paths with new moves successively
     * into paths of longer and longer length.
     *
     * Note: This function's rational is similar to the from function for integers
     *       found in
     *       https://github.com/vmous/jazzy-moocs/blob/3074cfdc30ef6cfbd6529d7bbfa850486ff2c008/progfun2/week02/lecturecode/src/main/scala/lecturecode/Primes.scala#L5-L8
     */
    def from(paths: Set[Path], explored: Set[State]): Stream[Set[Path]] = {
      if (paths.isEmpty) Stream.empty
      else {
        val more = for {
          path <- paths
          next <- moves map path.extend
          if !(explored contains (next.endState))
        } yield next

        paths #:: from(more, explored ++ (more map (_.endState)))
      }
    }

    /**
     * The complete set of all possible paths is the one that starts from
     * the initialPath and in the first iteration giving me the paths of length
     * one, then the paths of length two and so one and so forth, until
     * infinity (!).
     */
    val pathSets = from(Set(initialPath), Set(initialState))

    /**
     * Go through all the path sets and pick those that are solutions and
     * concatenate them into another stream. The last stream then will consist
     * of all the paths that are solution paths ordered by their length.
     *
     * @param target The volume we want to see in one of the glasses.
     *
     * @return a stream of paths that have the target volume in their end state.
     */
    def solutions(target: Int): Stream[Path] = {
      for {
        pathSet <- pathSets
        path <- pathSet
        if path.endState contains target
      } yield path
    }
  }

}
