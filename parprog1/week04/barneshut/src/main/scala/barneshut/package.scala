import common._
import barneshut.conctrees._

package object barneshut {

  /**
    * Auxiliary class Boundaries, which contains the minX, maxX, minY and maxY
    * fields for the boundaries of the scene.
    */
  class Boundaries {
    var minX = Float.MaxValue
    var minY = Float.MaxValue
    var maxX = Float.MinValue
    var maxY = Float.MinValue

    def width = maxX - minX
    def height = maxY - minY
    def size = math.max(width, height)
    def centerX = minX + width / 2
    def centerY = minY + height / 2

    override def toString = s"Boundaries($minX, $minY, $maxX, $maxY)"
  }

  /**
    * The quadtree is an immutable data structure denoting a square cell of
    * space.
    *
    * We consider the top left corner to be at coordinate (0, 0). We also
    * consider the X axis to grow to the right and the Y axis to the bottom.
    *
    * __________________________________________
    * |                    |                    |
    * |    (x_D,y_D)       |                    |
    * |   * m_D            |                    |
    * |                    |                    |
    * |                    |                    |
    * |                    |                    | (x_E,y_E)
    * |                    |                    * m_E
    * |                    |                    |
    * |                    |(center_x, center_y)|
    * |____________________|____________________|
    * |                    |                    |
    * |                    |                    |
    * |                    |                    |
    * |                    |    O               |
    * |                    |    (mass_x,mass_y) |
    * |                    |                    |
    * |                    |                    |   total=4
    * |                    |      (x_C,y_C)     |
    * |   (x_B,y_B)        |               * m_C|
    * |______*_____________|____________________|
    *        m_B
    * |<----------------- size ---------------->|
    *
    * Note that a quad-tree is an immutable data structure.
    */
  sealed abstract class Quad {
    /**
      * X coordinate of the center of mass of the bodies in the
      * respective cell.
      */
    def massX: Float
    /**
      * Y coordinate of the center of mass of the bodies in the
      * respective cell.
      */
    def massY: Float
    /** Total mass of bodies in the cell. */
    def mass: Float
    /** X coordinate of the center of the cell. */
    def centerX: Float
    /** Y coordinate of the center of the cell. */
    def centerY: Float
    /** Length of the side of the cell. */
    def size: Float
    /** Total number of bodies in the cell. */
    def total: Int

    /**
      * Creates a new quadtree which additionally contains the body b, and covers
      * the same area in space as the original quadtree.
      *
      * Note: quad-tree is an immutable data structure -- insert does not modify
      * the existing Quad object.
      */
    def insert(b: Body): Quad
  }

  /**
    * Represents the empty quad tree.
    *
    * The center and the size of the Empty quadtree are specified in its
    * constructor. The Empty quad-tree does not contain any bodies, so we specify
    * that its center of mass is equal to its center.
    */
  case class Empty(centerX: Float, centerY: Float, size: Float) extends Quad {
    def massX: Float = centerX
    def massY: Float = centerY
    def mass: Float = 0.0f
    def total: Int = 0
    def insert(b: Body): Quad = Leaf(centerX, centerY, size, Seq(b))
  }

  /**
    * Represents a division of a spacial cell into four quadrants.
    *
    * This Fork node is specified by four child quad-trees nw, ne, sw and se, in
    * the northwest, northeast, southwest and southeast quadrant, respectively.
    * The northwest is located on the top left, northeast on the top right,
    * southwest on the bottom left and southeast on the bottom right.
    *
    * The constructor assumes that the children nodes that represent four
    * adjacent cells of the same size and adjacent to each other. The center of
    * the Fork quad-tree is then specified by, say, the lower right corner of the
    * quad-tree nw. If the Fork quad-tree is empty, the center of mass coincides
    * with the center.
    */
  case class Fork(
    nw: Quad, ne: Quad, sw: Quad, se: Quad
  ) extends Quad {
    val centerX: Float = nw.centerX + nw.size / 2
    val centerY: Float = nw.centerY + nw.size / 2
    val size: Float = nw.size * 2
    val mass: Float = nw.mass + ne.mass + sw.mass + se.mass
    val massX: Float =
      if (mass > 0)
        (nw.massX * nw.mass + ne.massX * ne.mass + sw.massX * sw.mass + se.massX * se.mass) / mass
      else
        centerX
    val massY: Float =
      if (mass > 0)
        (nw.massY * nw.mass + ne.massY * ne.mass + sw.massY * sw.mass + se.massY * se.mass) / mass
      else
        centerY
    val total: Int = nw.total + ne.total + sw.total + se.total

    /**
      * Inserting into a Fork is recursive -- it updates the respective child and
      * creates a new Fork.
      */
    def insert(b: Body): Fork = {
      if (b.x < centerX && b.y < centerY) Fork(nw.insert(b), ne, sw, se)
      else if (b.x >= centerX && b.y < centerY) Fork(nw, ne.insert(b), sw, se)
      else if (b.x < centerX && b.y >= centerY) Fork(nw, ne, sw.insert(b), se)
      else Fork(nw, ne, sw, se.insert(b))
    }
  }

  /** Represents one or more bodies. */
  case class Leaf(centerX: Float, centerY: Float, size: Float, bodies: Seq[Body])
      extends Quad {
    val mass = bodies.foldLeft(0.0f)((acc, r) => acc + r.mass)
    val massX = bodies.foldLeft(0.0f)((acc, r) => acc + r.mass * r.x) / mass
    val massY = bodies.foldLeft(0.0f)((acc, r) => acc + r.mass * r.y) / mass
    val total: Int = bodies.size

    /**
      * If the size of a Leaf is greater than a predefined constant minimumSize,
      * inserting an additional body into that Leaf quadtree creates a Fork
      * quad-tree with empty children, and adds all the bodies into that Fork
      * (including the new body). Otherwise, inserting creates another Leaf with
      * all the existing bodies and the new one.
      */
    def insert(b: Body): Quad = {
      if (size <= minimumSize) Leaf(centerX, centerY, size, bodies :+ b)
      else {
        // forked size
        val fs = size / 2
        // forked center offset (is half of the forked size)
        val fcOffset = fs / 2
        // forked north-west
        val fnw = Empty(centerX - fcOffset, centerY - fcOffset, fs)
        // forked north-east
        val fne = Empty(centerX + fcOffset, centerY - fcOffset, fs)
        // forked south-west
        val fsw = Empty(centerX - fcOffset, centerY + fcOffset, fs)
        // forked south-east
        val fse = Empty(centerX + fcOffset, centerY + fcOffset, fs)

        val fork = Fork(fnw, fne, fsw, fse)

        (bodies :+ b).foldLeft(fork)((k, l) => k.insert(l))
      }
    }

  }

  def minimumSize = 0.00001f

  def gee: Float = 100.0f

  def delta: Float = 0.01f

  def theta = 0.5f

  def eliminationThreshold = 0.5f

  def force(m1: Float, m2: Float, dist: Float): Float = gee * m1 * m2 / (dist * dist)

  def distance(x0: Float, y0: Float, x1: Float, y1: Float): Float = {
    math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)).toFloat
  }

  /**
    * Data-type representing a stellar object.
    *
    * Value mass is the body's mass, while x and y are its coordinates. Values xspeed and yspeed
    * represent the velocity of the body.
    */
  class Body(val mass: Float, val x: Float, val y: Float, val xspeed: Float, val yspeed: Float) {

    def updated(quad: Quad): Body = {
      var netforcex = 0.0f
      var netforcey = 0.0f

      def addForce(thatMass: Float, thatMassX: Float, thatMassY: Float): Unit = {
        val dist = distance(thatMassX, thatMassY, x, y)
        /* If the distance is smaller than 1f, we enter the realm of close
         * body interactions. Since we do not model them in this simplistic
         * implementation, bodies at extreme proximities get a huge acceleration,
         * and are catapulted from each other's gravitational pull at extreme
         * velocities (something like this:
         * http://en.wikipedia.org/wiki/Interplanetary_spaceflight#Gravitational_slingshot).
         * To decrease the effect of this gravitational slingshot, as a very
         * simple approximation, we ignore gravity at extreme proximities.
         */
        if (dist > 1f) {
          val dforce = force(mass, thatMass, dist)
          val xn = (thatMassX - x) / dist
          val yn = (thatMassY - y) / dist
          val dforcex = dforce * xn
          val dforcey = dforce * yn
          netforcex += dforcex
          netforcey += dforcey
        }
      }

      /* Goes through the quad-tree and proceeds case-wise:

       - empty quadtree does not affect the net force
       - each body in a leaf quadtree adds some net force
       - a fork quadtree that is sufficiently far away acts as a single point of
         mass
       - a fork quadtree that is not sufficiently far away must be recursively
         traversed

       When are we allowed to approximate a cluster of bodies with a single
       point? The heuristic that is used is that the size of the cell divided by
       the distance dist between the center of mass and the particle is less than
       some constant theta */
      def traverse(quad: Quad): Unit = (quad: Quad) match {
        case Empty(_, _, _) =>
          // no force
        case Leaf(_, _, _, bodies) =>
          // add force contribution of each body by calling addForce
          bodies.foreach(b => addForce(b.mass, b.x, b.y))
        case Fork(nw, ne, sw, se) =>
          val dist = distance(quad.massX, quad.massY, x, y)
          if (quad.size / dist < theta) {
            // approximate because node is far enough from the body
            addForce(quad.mass, quad.massX, quad.massY)
          }
          else {
            // node is NOT far enough, traverse
            traverse(nw)
            traverse(ne)
            traverse(sw)
            traverse(se)
          }
      }

      traverse(quad)

      val nx = x + xspeed * delta
      val ny = y + yspeed * delta
      val nxspeed = xspeed + netforcex / mass * delta
      val nyspeed = yspeed + netforcey / mass * delta

      new Body(mass, nx, ny, nxspeed, nyspeed)
    }

  }

  val SECTOR_PRECISION = 8

  /**
    * The SectorMatrix is just a square matrix that covers a square region of
    * space specified by the boundaries.
    *
    * The sectorPrecision argument denotes the width and height of the matrix,
    * and each entry contains a ConcBuffer[Body] object. Effectively, the
    * SectorMatrix is a combiner -- it partitions the square region of space into
    * sectorPrecision times sectorPrecision buckets, called sectors.
    *
    *  ________________________
    *  |0,0  |1,0  |2,0  |3,0  |
    *  |     |     |     |     |<-------\
    *  |_____|_____|_____|_____|         \
    *  |0,1  |1,1* |2,1  |3,1  |          \
    *  |     |     |     |     |<-------\  \
    *  |_____|_____|_____|_____|       ConcBuffer[Body]
    *  |0,2  |1,2  |2,2  |3,2  |        /
    *  |     |     |  *  |     |<------/
    *  |_____|_____|_____|_____|
    *  |0,3 *|1,3  |2,3  |3,3  |
    *  |     |     |     |*  * |
    *  |_____|_____|_____|____*|
    *
    * Combiners such as the SectorMatrix are used in parallel programming to
    * partition the results into some intermediate form that is more amenable to
    * parallelization. Recall from the lecture that one of the ways to implement
    * a combiner is by using a bucket data structure. We add three methods on the
    * SectorMatrix that makes it a combiner: += method, combine method and the
    * toQuad method.
    */
  class SectorMatrix(val boundaries: Boundaries, val sectorPrecision: Int) {
    val sectorSize = boundaries.size / sectorPrecision
    val matrix = new Array[ConcBuffer[Body]](sectorPrecision * sectorPrecision)
    for (i <- 0 until matrix.length) matrix(i) = new ConcBuffer

    /**
      * This method uses the body position, boundaries and sectorPrecision to
      * determine the sector into which the body should go into, and adds the
      * body into the corresponding ConcBuffer object.
      *
      * Importantly, if the Body lies outside of the Boundaries, it should be
      * considered to be located at the closest point within the Boundaries for
      * the purpose of finding which ConcBuffer should hold the body.
      */
    def +=(b: Body): SectorMatrix = {

      def sectorMatrixPos(p1: Float, p2: Float): Int = {
        ((p1 - p2) / sectorSize).toInt max 0 min sectorPrecision - 1
      }

      val x = sectorMatrixPos(b.x, boundaries.minX)
      val y = sectorMatrixPos(b.y, boundaries.minY)
      apply(x, y) += b

      this
    }

    def apply(x: Int, y: Int) = matrix(y * sectorPrecision + x)

    /**
      * Takes another SectorMatrix, and creates a SectorMatrix which contains the
      * elements of both input SectorMatrix data structures. This method calls
      * combine on the pair of ConcBuffers in this and that matrices to produce
      * the ConcBuffer for the resulting matrix.
      *  __________________          __________________    __________________
      *  |0,0  |1,0  |2,0 *|         |0,0  |1,0  |2,0  |   |0,0  |1,0  |2,0 *|
      *  |     |     |     |         |     |     |   * |   |     |     |   * |
      *  |_____|_____|_____|         |_____|_____|_____|   |_____|_____|_____|
      *  |0,1  |1,1* |2,1  |         |0,1  |1,1  |2,1  |   |0,1  |1,1* |2,1  |
      *  |     |     |     | combine |     |     |     | = |     |     |     | 
      *  |_____|_____|_____|         |_____|_____|_____|   |_____|_____|_____|
      *  |0,2  |1,2  |2,2  |         |0,2  |1,2* |2,2  |   |0,2  |1,2* |2,2  |
      *  |     |     |  *  |         |     |     |     |   |     |     |  *  |
      *  |_____|_____|_____|         |_____|_____|_____|   |_____|_____|_____|
      *
      * We assume that combine will only be called on matrices of same
      * dimensions, boundaries and sector precision.
      */
    def combine(that: SectorMatrix): SectorMatrix = {
      for (i <- 0 until this.matrix.length) {
        this.matrix.update(i, this.matrix(i) combine that.matrix(i))
      }
      this
    }

    /**
      * The nice thing about the sector matrix is that a quadtree can be
      * constructed in parallel for each sector. Those little quadtrees can then
      * be linked together. The toQuad method does exactly this.
      */
    def toQuad(parallelism: Int): Quad = {
      def BALANCING_FACTOR = 4
      def quad(x: Int, y: Int, span: Int, achievedParallelism: Int): Quad = {
        if (span == 1) {
          val sectorSize = boundaries.size / sectorPrecision
          val centerX = boundaries.minX + x * sectorSize + sectorSize / 2
          val centerY = boundaries.minY + y * sectorSize + sectorSize / 2
          var emptyQuad: Quad = Empty(centerX, centerY, sectorSize)
          val sectorBodies = this(x, y)
          sectorBodies.foldLeft(emptyQuad)(_ insert _)
        } else {
          val nspan = span / 2
          val nAchievedParallelism = achievedParallelism * 4
          val (nw, ne, sw, se) =
            if (parallelism > 1 && achievedParallelism < parallelism * BALANCING_FACTOR) parallel(
              quad(x, y, nspan, nAchievedParallelism),
              quad(x + nspan, y, nspan, nAchievedParallelism),
              quad(x, y + nspan, nspan, nAchievedParallelism),
              quad(x + nspan, y + nspan, nspan, nAchievedParallelism)
            ) else (
              quad(x, y, nspan, nAchievedParallelism),
              quad(x + nspan, y, nspan, nAchievedParallelism),
              quad(x, y + nspan, nspan, nAchievedParallelism),
              quad(x + nspan, y + nspan, nspan, nAchievedParallelism)
            )
          Fork(nw, ne, sw, se)
        }
      }

      quad(0, 0, sectorPrecision, 1)
    }

    override def toString = s"SectorMatrix(#bodies: ${matrix.map(_.size).sum})"
  }

  class TimeStatistics {
    private val timeMap = collection.mutable.Map[String, (Double, Int)]()

    def clear() = timeMap.clear()

    def timed[T](title: String)(body: =>T): T = {
      var res: T = null.asInstanceOf[T]
      val totalTime = /*measure*/ {
        val startTime = System.currentTimeMillis()
        res = body
        (System.currentTimeMillis() - startTime)
      }

      timeMap.get(title) match {
        case Some((total, num)) => timeMap(title) = (total + totalTime, num + 1)
        case None => timeMap(title) = (0.0, 0)
      }

      println(s"$title: ${totalTime} ms; avg: ${timeMap(title)._1 / timeMap(title)._2}")
      res
    }

    override def toString = {
      timeMap map {
        case (k, (total, num)) => k + ": " + (total / num * 100).toInt / 100.0 + " ms"
      } mkString("\n")
    }
  }
}
