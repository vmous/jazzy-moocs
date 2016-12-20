package lecturecode

import scala.annotation.tailrec


/**
  * A datatype to represent balanced trees.
  *
  * Conc represents the node of the this data type and contains, except from
  * references to the left and right sub-trees, the level which is equal to the
  * longest path from the root to a leaf (in other words, the height of the
  * tree:), and size which denotes the number of elements in the sub tree.
  *
  * The concrete classes are defined bellow.
  *
  * <ul>We define the following invariants for Conc-trees:
  * <li>
  *   A <> node can never contain Empty as its sub-tree (guards against sparse
  *   trees).
  * </li>
  * <li>
  *   The level difference between the left and the right subtree of a <> node
  *   is always 1 or less (ensures the trees remain balanced).
  * </li>
  *
  * We rely on the above invariants to implement concatenation.
  *
  * The conc data type corresponds to a so called conc-list, originally
  * introduced by the Fortress language. The conc-list is a neat data
  * abstraction for parallel programming and could have any number of
  * implementations. Here we study one particularly concise conc-list.
  */
sealed trait Conc[@specialized(Int, Long, Float, Double) +T] {
  def level: Int
  def size: Int
  def left: Conc[T]
  def right: Conc[T]

  def foreach[U](f: T => U): Unit = ???
  def <>[S >: T](that: Conc[S]): Conc[S] = ???
}

object Conc {
  /**
    * The inner nodes of the tree. The level is equal to one plus level of the
    * higher subtree. The size is equal to the number of elements in both
    * subtrees.
    */
  case class <>[+T](left: Conc[T], right: Conc[T]) extends Conc[T] {
    val level = 1 + math.max(left.level, right.level)
    val size = left.size + right.size

    override def foreach[U](f: T => U): Unit = traverse(this, f)

    /**
      * In the spirit of the cons operator used to build functional lists, we
      * overload the conc constructor with a method with the same name.
      * This method ensures the first invariant by eliminating the trees. It
      * then delegates the real work to another method called concat.
      *
      * The complexity of this operation is O(h1 - h2), where h1 is the height
      * of tree 1 and h2 is the height of tree 2.
      */
    override def <>[S >: T](that: Conc[S]): Conc[S] = {
      if (this == Empty) that
      else if (that == Empty) this
      else concat(this, that)
    }

    /**
      * The concat method reorganizes the tree so that the new arrangement
      * adheres to the second invariant (tree remains balanced).
      *
      * The concat method cannot always directly link two trees together by
      * creating a conc node above them, as that would violate the height
      * invariant. We actually have the following cases
      *
      * 1. The tree height difference is 1 or less. The code for this case is
      * simple as it just links the two trees together.
      *
      * 2. If 1. doesn't hold then we have a left tree xs with height d and a
      * right tree ys with height e <= d-2. We cannot link these trees directly.
      * We first break xs into left subtree xs.left and right subtree xs.right.
      * Then we recursively concatenate xs.right with ys, and then relink all the
      * trees together. What exactly is concatenated and how things get
      * ultimately relinked depends on the shape of the xs.
      *
      * 2a. xs is left leaning (xs.left.level >= xs.right.level). This means that
      * the left subtree is deeper than its right subtree. In that case we
      * recursively concatenate xs.right with ys. The resulting subtree, that
      * increases in height by at most one (e' <= d-1), is then linked with
      * xs.left.
      *
      * 2b xs is right leaning. In this case, we also decompose the xs.right to
      * consider xs.right.left and xs.right.right. We then concatenate
      * xs.right.right with ys getting a new tree zs. We then need to relink
      * xs, xs.right.left and zs together. If zs is a tree with (e' = d-1) we
      * link the trees from left to right. Otherwise (e' = d-2 or e' = d-3) we
      * link the trees from right to left.
      */
    private def concat[T](xs: Conc[T], ys: Conc[T]): Conc[T] = {
      val diff = ys.level - xs.level
      // case 1
      if (diff >= -1 && diff <= 1)
        /* Note that the expression below (new <>) just creates one node that
         links the two trees. It is different than calling the conc operator on
         two trees (xs <> ys), which would also take care of the balancing. */ 
        new <>(xs, ys)
      else if (diff < -1) {
        if (xs.left.level >= xs.right.level) {
          // the left subtree is deeper than the right subtree.
          val nr = concat(xs.right, ys)
          new <>(xs.left, nr)
        }
        else {
          val nrr = concat(xs.right.right, ys)
          if (nrr.level == xs.level - 3) {
            val nl = xs.left
            val nr = new <>(xs.right.left, nrr)
            new <>(nl, nr)
          }
          else {
            val nl = new <>(xs.left, xs.right.left)
            val nr = nrr
            new <>(nl, nr)
          }
        }
      }
      else {
        if (ys.right.level >= ys.left.level) {
          val nl = concat(xs, ys.left)
          new <>(nl, ys.right)
        }
        else {
          val nll = concat(xs, ys.left.left)
          if (nll.level == ys.level - 3) {
            val nl = new <>(nll, ys.left.right)
            val nr = ys.right
            new <>(nl, nr)
          }
          else {
            val nl = nll
            val nr = new <>(ys.left.right, ys.right)
            new <>(nl, nr)
          }
        }
      }
    }
  }

  sealed trait Leaf[T] extends Conc[T] {
    def left = sys.error("Leaves do not have children.")
    def right = sys.error("Leaves do not have children.")
  }

  /**
    * The empty class represents empty trees. The level and size are both zero.
    */
  case object Empty extends Leaf[Nothing] {
    def level = 0
    def size = 0
  }

  /**
    * The leaves of the tree containing a single element. Their level is zero and
    * the total number of elements one.
    */
  class Single[@specialized(Int, Long, Float, Double) T](val x: T) extends Leaf[T] {
    def level = 0
    def size = 1
    override def toString = s"Single($x)"
  }

  class Chunk[@specialized(Int, Long, Float, Double) T](val array: Array[T], val size: Int, val k: Int) extends Leaf[T] {
    def level = 0
    override def toString = s"Chunk(${array.mkString("", ", ", "")}; $size; $k)"
  }

  /**
    * Class similar to <> but with different semantics.
    *
    * A helper class to achieve O(1) appends with low constant factors. The idea
    * is to represent the the result of a += (plus-equals) operation differently
    * so that we impose different semantics that he normal <> node. Concretely,
    * the difference in semantics is that for Append nodes we allow arbitrary
    * difference in levels between the left and the right child trees.
    */
  case class Append[+T](left: Conc[T], right: Conc[T]) extends Conc[T] {
    val level = 1 + math.max(left.level, right.level)
    val size = left.size + right.size
  }

  /**
    * Innefficient implementation of the += method.
    *
    * This method first creates a single node, and then it creates a new append
    * node to link them together. This method allocates two objects in total so
    * the total number of computational steps it takes is constant and thus it's
    * running time is really 0(1).
    *
    * Unfortunately, a tree created this way is obviously unbalanced. If we are
    * to later use it for parallelism or concatenations, we need to somehow
    * transform the data structure back into a format that does not have any
    * Append nodes. But is it possible to do this reasonably quickly? It turns
    * out that it is not. After we add n elements to the tree this way, we will
    * have n Append nodes in total.
    *
    *                                   -
    *                           -    /-|A|
    *                   -    /-|A|--/   -\
    *                /-|A|--/   -\        \_
    *               /   -\        \_      / \
    *          /\__/      \_      / \    |   |
    *          \/         / \    |   |    \_/
    *          /\        |   |    \_/
    *         /  \        \_/
    *        /    \
    *       /      \
    *      /        \
    *     /          \
    *    /____________\
    *
    * Therefore, we would have to traverse and process all those nodes to
    * eliminate them from the tree. And once again establish the balance in
    * variance. The conversion to a normal Conc-Tree would have to take at least
    * O(n) steps. The fundamental problem here is that we are essentially still
    * building a link list with append nodes. So we need to link these notes more
    * intelligently.
    */
  def appendLeafOofN[T](xs: Conc[T], y: T): Conc[T] = Append(xs, new Single(y))

  /**
    * We make sure that if the total number of elements in the tree is n, then
    * there are never more than logn Append nodes in the data structure.
    *
    * To understand how we do that let's consider the following seemingly
    * unrelated topic.
    *
    * Counting in a Binary Number System
    *
    *                         0                            0 * 2^0 = 0
    *                       w=2^0
    * Step #1                     +1
    *                         1                            1 * 2^0 = 1
    *                       w=2^0
    * Step #2                     +1
    *                  1      0                  1 * 2^1 + 0 * 2^0 = 2
    *                w=2^1  w=2^0
    * Step #3                     +1
    *                  1      1                  1 * 2^1 + 1 * 2^0 = 3
    *                w=2^1  w=2^0
    * Step #4                     +1
    *           1      0      0        1 * 2^2 + 0 * 2^1 + 0 * 2^0 = 4
    *         w=2^2  w=2^1  w=2^0
    *  ...
    *
    * Note above that in the cases were we cannot increment the binary digit by
    * one then we carry the digit one to the next higher weight position place.
    * In step 4 above we perform two curry operations, first for the least
    * significant digit and since the second to last digit is again one, a
    * second carry operation ending with the number 100 (in binary).
    *
    * <ul>The binary number system has two important properties:
    * <li>
    *   To count up to n in the binary number system, we will have to do O(n)
    *   amount of work (four steps to count up to number four).
    * </li>
    * <li>
    *   A number n requires O(logn) digits. In other words, the number of
    *   digits we need to flip to get to n is less than 2*n. (In the example
    *   above, it is not incidental that we flipped exactly 7 digits to get to
    *   the number 4.
    * </li>
    * </ul>
    *
    * So concretely:
    * 4_10    100_2
    * 8_10   1000_2
    * 16_10 10000_2
    * ...
    *
    * while the decimal number grows exponentially, the number of binary digits
    * grows linearly. This is the same as saying that the number grows linearly,
    * and the number of digits grows logarithmically.
    *
    * Now, observe that there is a correspondence between a digit at position k,
    * and a ConcTree with level k. When we link two trees with level k, you get
    * a tree with level k+1, just as adding two digits at position k becomes a
    * digit at position k+1.
    *
    *                  1   +1                    0   A   0
    *                w=2^0                      /_\     /_\
    *
    *
    *                                      1         A   0
    *           1      0   +1             / \           /_\
    *         w=2^1  w=2^0               /___\
    *
    *
    *                                      1     0   A   0
    *           1      1   +1             / \   /_\     /_\
    *         w=2^1  w=2^0               /___\
    *
    *
    *
    *    1      0      0         2
    *  w=2^2  w=2^1  w=2^0      / \
    *                          /   \
    *                         /_____\
    *
    * Let's go through the steps above: If we start with a tree at level zero and
    * append another such tree, we can link them with an Append node to a tree
    * with level one in one computational step. If we again add a tree at level
    * 0, we can add it directly to the empty position for the level 0 tree. There
    * is no need to link anything in this step. Next time we add a tree at level
    * 0, we will first have to link the two trees at level 0 (which corresponds
    * to the first carry operation), and then link the two trees at level one
    * (which corresponds to the second carry operation).
    * <ul>Linking the trees in the same order as we count in a binary number
    * system results in a similar pair of properties:
    * <li>
    *   To add n leaves takes O(n) work. Meaning that on average adding each leaf
    * requires O(1) work.
    * </li>
    * <li>
    *   Storing n leaves requires a logarithmic amount of Append nodes.
    * </li>
    * </ul>
    *
    * We will use the Append nodes to store our binary number. In this binary
    * number representation, the 0 digit will correspond to a missing tree, and
    * the 1 digit will correspond to an existing tree.
    *
    * For example, assume that we have the following trees:
    *
    *      4          3      2    1    0
    *      ^          ^      .    ^    .
    *     / \        / \         /_\
    *    /   \      /   \
    *   /     \    /_____\
    *  /_______\
    *
    * In this append list, the trees with levels 4, 3, and 1 exist. And the trees
    * with the levels 2 and 0 are not present. The corresponding append list will
    * look as follows:
    *                           0
    *                       1
    *                   0     -
    *              1       /-|A|
    *                -    /   -\
    *           /---|A|--/      \
    *          /     -\          1
    *      1  /        \        /_\
    *        4          3
    *       / \        / \
    *      /   \      /   \
    *     /     \    /_____\
    *    /_______\
    *
    * The tree above corresponds to binary number: 11010. Note that zero digits
    * are not explicitly represented the zero digits. They are encoded as missing
    * trees.
    *
    * Using the idea above, we implement the appendLeafOof1Amortized. This
    * appends the single tree ys to the tree xs.
    */
  def appendLeafOof1Amortized[T](xs: Conc[T], ys: Leaf[T]): Conc[T] = xs match {
    // if the first tree is empty, return the second
    case Empty => ys
    // if it gets two Single trees then just links them together
    case xs: Leaf[T] => new <>(xs, ys)
    // if xs is an inner node then links xs and ys with an append node
    case _ <> _ => new Append(xs, ys)
    /* if xs is an Append node to start with the work is delegated to the append
     method (check bellow) */
    case xs: Append[T] => append(xs, ys)
  }

  /**
    * Tail recursive append node method that essentially implements counting in a
    * binary number system.
    */
  @tailrec
  private def append[T](xs: Append[T], ys: Conc[T]): Conc[T] = {
    /* If the tree ys is a level smaller than the right sub-tree of xs, a new
     append node is created. */
    if (xs.right.level > ys.level) new Append(xs, ys)
    else {
      /* The right sub-tree of xs and ys are linked into a normal tree, zs. */
      val zs = new <>(xs.right, ys)
      xs.left match {
        /* Recursively call append again for the left subtree. */
        case ws @ Append(_, _) => append(ws, zs)
        /* The remaining two cases handle the scenario in which the left sub-tree
         is not Append, which happens if we manage to push the tree all the way
         to the bottom of this Append list. */
        case ws if ws.level <= zs.level => ws <> zs
        case ws => new Append(ws, zs)
      }
    }
  }

  def traverse[@specialized(Int, Long, Float, Double) T, @specialized(Int, Long, Float, Double) U](xs: Conc[T], f: T => U): Unit = (xs: @unchecked) match {
    case left <> right =>
      traverse(left, f)
      traverse(right, f)
    case s: Single[T] =>
      f(s.x)
    case c: Chunk[T] =>
      val a = c.array
      val sz = c.size
      var i = 0
      while (i < sz) {
        f(a(i))
        i += 1
      }
    case Empty =>
    case Append(left, right) =>
      traverse(left, f)
      traverse(right, f)
    case _ =>
      sys.error("All cases should have been covered: " + xs + ", " + xs.getClass)
  }

}
