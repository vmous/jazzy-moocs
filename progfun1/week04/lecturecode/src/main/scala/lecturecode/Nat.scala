package lecturecode

/**
  * A class that represents natuaral numbers. (Peano numbers)
  *
  * Numbers that start from 0 and positive integers but not negative.
  * We assume that this class has a subset of method we assume for Ints as shown
  * below.
  *
  * Implementation is based on Peano numbers, a simple way of representing the
  * natural numbers using only a zero valua and a successor function.
  * https://wiki.haskell.org/Peano_numbers
  */
abstract class Nat {
  /**
    * Tests if the given natural number is zero
    */
  def isZero: Boolean

  /**
    * For a positive natural number it gives the previous number.
    *
    * Throw an exception if the given number is zero (0)
    */
  def predecessor: Nat

  /**
    * For a positive natural number it gives the next nubmer.
    */
  def successor: Nat = new Succ(this)

  /**
    * Addition of natural numbers.
    */
  def + (that: Nat): Nat

  /**
    * Subtraction of natural numbers.
    *
    * Throw an exception if the result is negative.
    */
  def - (that: Nat): Nat
}

object Zero extends Nat {
  def isZero: Boolean = true
  def predecessor: Nat = throw new Error("0.predecessor")
  def + (that: Nat): Nat = that
  def - (that: Nat): Nat = if (that.isZero) this else throw new Error("negative number")
}

class Succ(n: Nat) extends Nat {
  def isZero: Boolean = false
  def predecessor: Nat = n
  def + (that: Nat): Nat = new Succ(n + that)
  def - (that: Nat): Nat = if (that.isZero) this else n - that.predecessor
}
