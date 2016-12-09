package lecturecode

import common.parallel

/**
  * In scanLeftMR solution above, did not reuse any computation across
  * different elements of the output array. Each element of the output
  * array was computed entirely independently of the other ones.
  *
  * A natural question is: ca we reuse at least some of the computations
  * even if we are not going to use the sequential computation pattern from
  * even before.
  *
  * The important observation is that reduce proceeds by applying the
  * operations in a tree-like fashion in order to obtain parallel benefits for
  * associative operations.
  *
  * The idea is: save these intermediate results in a separate tree
  * (TreeRes class) and make use of it when computing the output collection.
  * To keep our functions simple, we assume that the input collection itself is
  * also a tree (Tree class). This is going to be a different kind of tree
  * compared to the tree that saves the intermediate results.
  */
object ScanTreeOfValueLeaves {

  // Tree of the input and output collections
  sealed abstract class Tree[A]
  case class Leaf[A](a: A) extends Tree[A]
  case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A]

  /**
    * Prepend an element to the Tree.
    *
    * This function is an operation on a binary tree. If we do not worry about
    * balancing, then for leaf, we just create a node that contains a new leaf,
    * namely the element we are pre-pending, and for the node, we are pre-pending
    * only in the left sub-tree, leaving the right subtree as it was.
    */
  def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
    case Leaf(v) => Node(Leaf(x), Leaf(v))
    case Node(l, r) => Node(prepend(x, l), r)
  }

  /* Tree saving the intermediate results.
     Note: the intermediate values are stored in an additional res field, even
     for the internal nodes of this tree. */
  sealed abstract class TreeRes[A] { val res: A }
  case class LeafRes[A](override val res: A) extends TreeRes[A]
  case class NodeRes[A](l: TreeRes[A], override val res: A,
    r: TreeRes[A]) extends TreeRes[A]

  /**
    * Generator of a tree with intermediate values from a normal collection
    * tree.
    *
    * The name "upsweep" suggests the bottom up computation that is performed
    * in order to obtain the tree of intermediate results. 
    *
    * The resulting tree has the same shape as the original tree with the
    * additional values.
    */
  def upsweep[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
    case Leaf(v) => LeafRes(v)
    case Node(l, r) => {
      val (lt, rt) = (upsweep(l, f), upsweep(r, f))
      NodeRes(lt, f(lt.res, rt.res), rt)
    }
  }

  /**
    * Generator of scanLeft suffix.
    *
    * Given a tree of intermedate results, the function computes the scanLeft
    * suffix of elements for the initial collection. The process is called
    * "downsweep" to the top-down order of the computation. Prepend the initial
    * element to the retuned Tree to obtain the full scanLeft result.
    *
    * It is important to observe that element a0 denotes the reduce of all
    * elements that come to the left of the current tree, t, that is given to the
    * function. Initially it is a value given to the scanLeft function.
    *
    * We pattern match on the intermediate results tree. When we have a LeafRes,
    * then we simply apply operation f to element a0 and the element in the
    * leaf. If we have a NodeRes we recursively do a downsweep on the left and
    * right sub-tree. For the left sub-tree, a0 is the reduction of all elements
    * left of it. Left of left doesn't have anything more that a0 that is passed
    * to the downsweep function, itself. For the right sub-tree, on the other
    * hand, we take into account both the elements that are passed to the function
    * and the reduction of all elements of the left sub-tree. Finally, the newly
    * produced left and right subtrees are combined.
    *
    * For example, lets assume the tree bellow and an initial value of a0 equal
    * to 100 and a addition as a combination function:
    *
    *             ________62________
    *            /                  \
    *           /                    \
    *        __4__                  __58__
    *       /     \                /      \
    *      /       \              /        \
    *     1         3            8         50
    *
    * The initial value a0 is passed to the left subtree. This will then be
    * passed to the leaf and we will compute the result, **101**. To the right
    * sub-tree we pass the result of combining 100 with the value of the left
    * sub-tree which means we pass 101 here that is used to compute the leaf to
    * **4**.
    * Now, let's see what happens to the the right sub-tree of the big tree.
    * Now we pass the combination of a0, which is 100, and reduction of the left
    * sub-tree. Notice though, that this reduction is already stored up-front
    * for us in the result tree. So, we do not need to wait for this computation
    * in order to know what 4 is, because this is computed in a separate pass.
    * This means that we can pass the combination of a0=100 and 4 to the right
    * sub-tree of the big tree as the new a0. We then check the new left
    * sub-tree. a0=104 and being on a leaf we compute **112** right away. On the
    * new right sub-tree, we combine a0 with the reduction of the left
    * sub-tree (8) and we pass it as new a0 to the recursive call to the node
    * below. We then can compute **162** right away.
    *
    *              ________62________
    *             /                  \
    *      (100) /                    \ (104)
    *         __4__                  __58__
    *        /     \                /      \
    * (100) /       \ (101)  (104) /        \ (112)
    *      1         3            8         50
    *    (101)     (104)        (112)      (162)
    *
    * As we can observe, we have computed precisely the suffix of scan left
    * (here the sequence 101, 104, 112, 162). To obtain the complete scan left
    * result we need to prepend the initial element at the very beginning of the
    * tree.
    */
  def downsweep[A](t: TreeRes[A], a0: A, f: (A, A) => A): Tree[A] = t match {
    case LeafRes(a) => Leaf(f(a0, a))
    case NodeRes(l, _, r) => {
      val (lt, rt) = parallel(downsweep[A](l, a0, f), downsweep[A](r, f(a0, l.res), f))
      Node(lt, rt)
    }
  }

  /**
    * Implementation of scan left on trees.
    *
    * Because upsweep and downsweep are parallel and because prepend is just
    * a logarithmic operation then if we have an approximately balanced tree, we
    * have a good parallel running time.
    */
  def scanLeft[A](t: Tree[A], a0: A, f: (A, A) => A): Tree[A] = {
    val tr = upsweep(t, f)
    val s = downsweep(tr, a0, f)
    prepend(a0, s)
  }

}
