package lecturecode

object ClassHierarchies {

  /**
   * Class of sets of integers.
   */
  abstract class IntSet {

    /**
     * Test if set is empty.
     *
     * @return true if the set is empty. False otherwise.
     */
    def isEmpty: Boolean

    /**
     * Include a new element.
     *
     * @param x The element to include in the set.
     *
     * @return The set with the new element.
     */
    def incl(x: Int): IntSet

    /**
     * Exclude an new element.
     *
     * @param x The element to exclude from the set.
     *
     * @return The set with the new element.
     */
    def excl(x: Int): IntSet

    /**
     * Test membership
     *
     * @param x The Int to check if is included in the set.
     *
     * @return true if given element in set. False otherwise.
     */
    def contains(x: Int): Boolean

    /**
     * Form unions of two sets.
     *
     * @param other The other set to unite.
     *
     * @return The united set.
     *
     */
    def union(that: IntSet): IntSet

    /**
     * Form the intersection of two sets.
     *
     */
    def intersection(that: IntSet): IntSet
    // TODO: implement missing functionality (Bonus)
    // def filter(p: Int => Boolean): IntSet

  }

  /**
   * The empty node of the binary tree representation of the set.
   */
  object Empty extends IntSet {

    def isEmpty: Boolean = true
    def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
    def excl(x: Int): IntSet = Empty
    def contains(x: Int): Boolean = false
    def union(that: IntSet): IntSet = that
    def intersection(that: IntSet): IntSet = Empty
    override def toString() = "."

  }

  /**
   * The non-empty node of the binary tree representation of the set.
   */
  class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {

    def isEmpty: Boolean = false

    def incl(x: Int): IntSet = {
      if (x < elem) new NonEmpty(elem, left incl x, right)
      else if (x > elem) new NonEmpty(elem, left, right incl x)
      else this
    }

    def excl(x: Int): IntSet = {
      if (x < elem) new NonEmpty(elem, left excl x, right)
      else if (x > elem) new NonEmpty(elem, left, right excl x)
      else left union right
    }

    def contains(x: Int): Boolean = {
      if (x < elem) left contains x
      else if (x > elem) right contains x
      else true
    }

    def union(that: IntSet): IntSet = {
      ((left union right) union that) incl elem
    }

    def intersection(that: IntSet): IntSet = {
      val l = left intersection that
      val r = right intersection that
      val s = l union l
      if (that contains elem) s incl elem else s
    }

    override def toString() = "{" + left + elem + right + "}"

  }

}
