package barneshut

import java.awt._
import java.awt.event._
import javax.swing._
import javax.swing.event._
import scala.collection.parallel.TaskSupport
import scala.collection.parallel.Combiner
import scala.collection.parallel.mutable.ParHashSet
import common._

class Simulator(val taskSupport: TaskSupport, val timeStats: TimeStatistics) {

  /**
    * Given an existing boundaries object and a body, the updateBoundaries
    * updates the minX, minY, maxX and maxY values so that the boundaries include
    * the body.
    */
  def updateBoundaries(boundaries: Boundaries, body: Body): Boundaries = {
    boundaries.minX = boundaries.minX min body.x
    boundaries.minY = boundaries.minY min body.y
    boundaries.maxX = boundaries.maxX max body.x
    boundaries.maxY = boundaries.maxY max body.y
    boundaries
  }

  /**
    * Given two Boundaries objects, creates a new one, which represents the
    * smallest rectangle that contains both the input boundaries.
    */
  def mergeBoundaries(a: Boundaries, b: Boundaries): Boundaries = {
    val result = new Boundaries
    result.minX = a.minX min b.minX
    result.minY = a.minY min b.minY
    result.maxX = a.maxX max b.maxX
    result.maxY = a.maxY max b.maxY
    result
  }

  /**
    * Computes the Scene Boundaries.
    *
    * Since bodies move and the boundaries dynamically change, we must compute
    * them in every iteration of the algorithm. The implementation uses the
    * aggregate combinator on the sequence of bodies to compute the boundaries.
    *
    * Concretely, the aggregate method divides the input sequence into a number
    * of chunks. For each of the chunks, it uses the new Boundaries expression to
    * create the accumulation value, and then folds the values in that chunk
    * calling updateBoundaries on each body, in the same way a foldLeft operation
    * would. Finally, aggregate combines the results of different chunks using a
    * reduction tree and mergeBoundaries.
    */
  def computeBoundaries(bodies: Seq[Body]): Boundaries = timeStats.timed("boundaries") {
    val parBodies = bodies.par
    parBodies.tasksupport = taskSupport
    parBodies.aggregate(new Boundaries)(updateBoundaries, mergeBoundaries)
  }

  def computeSectorMatrix(bodies: Seq[Body], boundaries: Boundaries): SectorMatrix = timeStats.timed("matrix") {
    val parBodies = bodies.par
    parBodies.tasksupport = taskSupport
    parBodies.aggregate(new SectorMatrix(boundaries, SECTOR_PRECISION))(_ += _, _.combine(_))
  }

  def computeQuad(sectorMatrix: SectorMatrix): Quad = timeStats.timed("quad") {
    sectorMatrix.toQuad(taskSupport.parallelismLevel)
  }

  def updateBodies(bodies: Seq[Body], quad: Quad): Seq[Body] = timeStats.timed("update") {
    val parBodies = bodies.par
    parBodies.tasksupport = taskSupport
    parBodies.map(_.updated(quad)).seq
  }

  def eliminateOutliers(bodies: Seq[Body], sectorMatrix: SectorMatrix, quad: Quad): Seq[Body] = timeStats.timed("eliminate") {
    def isOutlier(b: Body): Boolean = {
      val dx = quad.massX - b.x
      val dy = quad.massY - b.y
      val d = math.sqrt(dx * dx + dy * dy)
      // object is far away from the center of the mass
      if (d > eliminationThreshold * sectorMatrix.boundaries.size) {
        val nx = dx / d
        val ny = dy / d
        val relativeSpeed = b.xspeed * nx + b.yspeed * ny
        // object is moving away from the center of the mass
        if (relativeSpeed < 0) {
          val escapeSpeed = math.sqrt(2 * gee * quad.mass / d)
          // object has the espace velocity
          -relativeSpeed > 2 * escapeSpeed
        } else false
      } else false
    }

    def outliersInSector(x: Int, y: Int): Combiner[Body, ParHashSet[Body]] = {
      val combiner = ParHashSet.newCombiner[Body]
      combiner ++= sectorMatrix(x, y).filter(isOutlier)
      combiner
    }

    val sectorPrecision = sectorMatrix.sectorPrecision
    val horizontalBorder = for (x <- 0 until sectorPrecision; y <- Seq(0, sectorPrecision - 1)) yield (x, y)
    val verticalBorder = for (y <- 1 until sectorPrecision - 1; x <- Seq(0, sectorPrecision - 1)) yield (x, y)
    val borderSectors = horizontalBorder ++ verticalBorder

    // compute the set of outliers
    val parBorderSectors = borderSectors.par
    parBorderSectors.tasksupport = taskSupport
    val outliers = parBorderSectors.map({ case (x, y) => outliersInSector(x, y) }).reduce(_ combine _).result

    // filter the bodies that are outliers
    val parBodies = bodies.par
    parBodies.filter(!outliers(_)).seq
  }

  def step(bodies: Seq[Body]): (Seq[Body], Quad) = {
    // 1. compute boundaries
    val boundaries = computeBoundaries(bodies)
    
    // 2. compute sector matrix
    val sectorMatrix = computeSectorMatrix(bodies, boundaries)

    // 3. compute quad tree
    val quad = computeQuad(sectorMatrix)
    
    // 4. eliminate outliers
    val filteredBodies = eliminateOutliers(bodies, sectorMatrix, quad)

    // 5. update body velocities and positions
    val newBodies = updateBodies(filteredBodies, quad)

    (newBodies, quad)
  }

}
