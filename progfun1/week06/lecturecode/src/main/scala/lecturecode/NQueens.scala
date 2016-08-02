package lecturecode

/**
  * The 8 Queens problem is to place eight queens on a chessboard so that no
  * queen is threatened by another. The N-Queens problem is a generalization
  * of the problme for chessboards of any size.
  *
  * Note: In other words, there can't be two queens in the same row, column or
  *       diagonal.
  *
  * Algorithm
  * - Suppose that we have already generated all solutions consisting of placing
  *   k - 1 queens on a board of size n.
  * - Each solution is represented by a list (of length k - 1) containing the
  *   numbers of columns (between 0 and n - 1)
  * - The column number of the queen in the k - 1th row comes first in the list,
  *   followed by the column number of the queen in row k - 2, etc.
  * - The solution set is thus represented as a set of lists, with one element
  *   for each solution.
  * - Now, to place the kth queen, we generate all possible extensions of each
  *   solution preceded by a new queen
  */
object NQueens {

  def queens(n: Int): Set[List[Int]] = {
    def placeQueens(k: Int): Set[List[Int]] = {
      if (k == 0) Set(List())
      else
        for {
          queens <- placeQueens(k - 1)
          col <- 0 until n
          if isSafe(col, queens)
        } yield col :: queens
    }
    placeQueens(n)
  }

  def isSafe(col: Int, queens: List[Int]): Boolean = {
    val row = queens.length
    val queensWithRow = (row - 1 to 0 by -1) zip queens
    queensWithRow forall {
      case (r, c) => col != c && math.abs(col - c) != row - r
    }
  }

  def show(queens: List[Int]) = {
    val lines =
      for (col <- queens.reverse)
      yield Vector.fill(queens.length)("* ").updated(col, "X ").mkString
    "\n" + (lines mkString "\n")
  }
}
