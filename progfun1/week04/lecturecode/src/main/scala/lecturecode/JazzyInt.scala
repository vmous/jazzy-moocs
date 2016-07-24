package lecturecode

/**
  * Example of an Int class
  */
abstract class JazzyInt {
  def + (that: Double): Double
  def + (that: Float): Float
  def + (that: Long): Long
  def + (that: JazzyInt): JazzyInt        // same for -, *, /, %

  def << (cnt: JazzyInt): JazzyInt        // same for >>, >>>

  def & (that: Long): Long
  def & (that: JazzyInt): JazzyInt        // same for |, ^

  def == (that: Double): Boolean
  def == (that: Float): Boolean
  def == (that: Long): Boolean  // same for !=, <, >, <=, >=
}
