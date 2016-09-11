package lecturecode

/**
  * Generators
  */
object Generators {

  trait Generator[+T] {
    // an alias of "this"
    self =>

    def generate: T

    /**
      * map definition for generators.
      */
    def map[S](f: T => S): Generator[S] = new Generator[S] {
      def generate = f(self.generate) // === Generator.this.generate
    }

    /**
      * flatMap definition for generators.
      */
    def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
      def generate = f(self.generate).generate
    }
  }

  /**
    * Generator of random integers.
    */
  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }

  /**
    * Generator of random booleans.
    */
  val booleans = for (i <- integers) yield i > 0

  /**
    * Generator of random pairs of objects.
    */
  def pairs[T, U](t: Generator[T], u: Generator[U]) = for {
    x <- t
    y <- u
  } yield (x, y)

  /**
    * Identity generator.
    */
  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  /**
    * Get an integer within an interval
    */
  def choose(lo: Int, hi: Int): Generator[Int] = {
    for (x <- integers) yield lo + x % (hi - lo)
  }

  /**
    * Randomly choose from an arbitrarily long list of objects.
    */
  def oneOf[T](xs: T*): Generator[T] = {
    for (idx <- choose(0, xs.length)) yield xs(idx)
  }

  /**
    * Ranldomly generate empty or non-empty lists.
    */
  def lists: Generator[List[Int]] = for {
    isEmpty <- booleans
    list <- if (isEmpty) emptyLists else nonEmptyLists
  } yield list

  /**
    * Empty list.
    */
  def emptyLists = single(Nil)

  /**
    * Non-empty list.
    */
  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head :: tail
}
