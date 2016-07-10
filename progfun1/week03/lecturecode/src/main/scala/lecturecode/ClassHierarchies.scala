package lecturecode

object ClassHierarchies {

  /**
    * Class of sets of integers.
    */
  abstract class IntSet {
    /**
      * Include a new element.
      * 
      * @param x The element to include in the Set.
      * 
      * @return The set with the new element.
      */
    def incl(x: Int): IntSet

    /**
      * Test membership
      * 
      * @param x The Int to check if is included in the set.
      * 
      * @return true if given element in set. False otherwise.
      */
    def contains(x: Int): Boolean
  }

  /**
    * The empty node of the binary tree representation of the set.
    */
  object Empty extends IntSet {
    def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
    def contains(x: Int): Boolean = false
    override def toString() = "."
  }

  /**
    * The non-empty node of the binary tree representation of the set.
    */
  class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {

    def incl(x: Int) : IntSet = {
      if (x < elem) new NonEmpty(elem, left incl x, right)
      else if (x > elem) new NonEmpty(elem, left, right incl x)
      else this
    }

    def contains(x: Int): Boolean = {
      if (x < elem) left contains x
      else if (x > elem) right contains x
      else true
    }

    override def toString() = "{" + left + elem + right + "}"

  }

}
