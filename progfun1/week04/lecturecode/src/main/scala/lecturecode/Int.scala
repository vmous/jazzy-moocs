package lecturecode

/**
  * Example of an Int class
  */
abstract class Int {
  def + (that: Double): Double
  def + (that: Float): Float
  def + (that: Long): Long
  def + (that: Int): Int        // same for -, *, /, %

  def << (cnt: Int): Int        // same for >>, >>>

  def & (that: Long): Long
  def & (that: Int): Int        // same for |, ^

  def == (that: Double): Boolean
  def == (that: Float): Boolean
  def == (that: Long): Boolean  // same for !=, <, >, <=, >=
}
