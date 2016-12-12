package kmeans

import scala.annotation.tailrec
import scala.collection._
import scala.util.Random
import org.scalameter._
import common._


/**
  * A K-means algorithm for cluster detection.
  *
  * Partitions n vectors into k clusters. Vectors are separated into clusters
  * based on their mutual similarity -- vectors that are closer to each other in
  * space are more likely to end up in the same cluster, and the distant vectors
  * are likely to be in different clusters.
  *
  * When the number of clusters, dimensions and vectors grows, it becomes
  * difficult and even impossible to manually determine the clusters. K-means is
  * a simple algorithm that takes a set of vectors (called points) and outputs as
  * set of clusters as follows:
  * <ul>
  * <li>Pick k points called means. This is called initialization.</li>
  * <li>
  *   Associate each input point with the mean that is closest to it. We obtain
  *   k clusters of points, and we refer to this process as classifying the
  *   points.
  * </li>
  * <li>
  *   Update each mean to have the average value of the corresponding cluster.
  * </li>
  * <li>
  *   If any of the k means have significantly changed, go back to step 2. If
  *   they did not, we say that the algorithm converged.
  * </li>
  * <li>
  *   The k means represent different clusters -- every point is in the cluster
  *   corresponding to the closest mean.
  * </li>
  * <ul>
  *
  * Above, two steps need additional discussion. First, how do we pick the
  * initial k means? The initialization step can be done in many different
  * ways -- we will just randomly pick some of the input vectors. Second, how do
  * we know that the algorithm converged? We will check that, for each mean, the
  * square distance between the old value of the mean and the new value of the
  * mean is less than or equal to some value eta.
  */
class KMeans {

  def generatePoints(k: Int, num: Int): Seq[Point] = {
    val randx = new Random(1)
    val randy = new Random(3)
    val randz = new Random(5)
    (0 until num)
      .map({ i =>
        val x = ((i + 1) % k) * 1.0 / k + randx.nextDouble() * 0.5
        val y = ((i + 5) % k) * 1.0 / k + randy.nextDouble() * 0.5
        val z = ((i + 7) % k) * 1.0 / k + randz.nextDouble() * 0.5
        new Point(x, y, z)
      }).to[mutable.ArrayBuffer]
  }

  def initializeMeans(k: Int, points: Seq[Point]): Seq[Point] = {
    val rand = new Random(7)
    (0 until k).map(_ => points(rand.nextInt(points.length))).to[mutable.ArrayBuffer]
  }

  def findClosest(p: Point, means: GenSeq[Point]): Point = {
    assert(means.size > 0)
    var minDistance = p.squareDistance(means(0))
    var closest = means(0)
    var i = 1
    while (i < means.length) {
      val distance = p.squareDistance(means(i))
      if (distance < minDistance) {
        minDistance = distance
        closest = means(i)
      }
      i += 1
    }
    closest
  }

  /**
    * Classifies the input points according to the square distance to the means.
    *
    * This method takes a generic sequence of points and a generic sequence of
    * means. It returns a generic map collection, which maps each mean to the
    * sequence of points in the corresponding cluster.
    *
    * Uses groupBy and the local findClosest method and makes sure that all the
    * means are in the GenMap, even if their sequences are empty.
    *
    * Note that GenSeq or GenMap, can be implemented either with a parallel or a
    * sequential collection. This means that the classify method will either run
    * sequentially or in parallel, depending on the type of the collection that
    * is passed to it, and returns a sequential or a parallel map, respectively.
    * The method is oblivious to whether the algorithm is parallel or not.
    */
  def classify(points: GenSeq[Point], means: GenSeq[Point]): GenMap[Point, GenSeq[Point]] = {
    if (points.isEmpty) means.map((_, Nil)).toMap
    else points.groupBy { findClosest(_, means) }
  }

  def findAverage(oldMean: Point, points: GenSeq[Point]): Point = if (points.length == 0) oldMean else {
    var x = 0.0
    var y = 0.0
    var z = 0.0
    points.seq.foreach { p =>
      x += p.x
      y += p.y
      z += p.z
    }
    new Point(x / points.length, y / points.length, z / points.length)
  }

  /**
    * Updates the means corresponding to different clusters.
    *
    * Takes the map of classified points produced in the previous step, and the
    * sequence of previous means. The method returns the new sequence of means
    * with preserved order -- mean i in the resulting sequence corresponds to the
    * mean i from oldMeans.
    *
    * Use the local findAverage method.
    *
    * Note that GenSeq or GenMap, can be implemented either with a parallel or a
    * sequential collection. This means that the classify method will either run
    * sequentially or in parallel, depending on the type of the collection that
    * is passed to it, and returns a sequential or a parallel map, respectively.
    * The method is oblivious to whether the algorithm is parallel or not.
    */
  def update(classified: GenMap[Point, GenSeq[Point]], oldMeans: GenSeq[Point]): GenSeq[Point] = {
    for (om <- oldMeans) yield findAverage(om, classified(om))
  }

  /**
    * K-means convergence detection method.
    *
    * The algorithm converged iff the square distance between the old and the new
    * mean is less than or equal to eta, for all means.
    *
    * Takes a sequence of old means and the sequence of updated means, and
    * returns a boolean indicating if the algorithm converged or not. Given an
    * eta parameter, oldMeans and newMeans, it returns true if the algorithm
    * converged, and false otherwise.
    *
    * Note: the means in the two lists are ordered -- the mean at i in oldMeans
    * is the previous value of the mean at i in newMeans.
    */
  def converged(eta: Double)(oldMeans: GenSeq[Point], newMeans: GenSeq[Point]): Boolean = {
    (oldMeans zip newMeans).forall(pair => pair._1.squareDistance(pair._2) <= eta)
  }

  /**
    * K-means driver method.
    *
    * Takes a sequence of points "points", previously computed sequence of means
    * "means", and the eta value. Returns the sequence of means, each
    * corresponding to a specific cluster.
    *
    * It is tail-recursive.
    *
    * Note: implements the steps 2-4 from the K-means pseudocode.
    */
  @tailrec
  final def kMeans(points: GenSeq[Point], means: GenSeq[Point], eta: Double): GenSeq[Point] = {
    val newMeans = update(classify(points, means), means)
    if (!converged(eta)(means, newMeans)) kMeans(points, newMeans, eta) else newMeans
  }
}

/** Describes one point in three-dimensional space.
 *
 *  Note: deliberately uses reference equality.
 */
class Point(val x: Double, val y: Double, val z: Double) {
  private def square(v: Double): Double = v * v
  def squareDistance(that: Point): Double = {
    square(that.x - x)  + square(that.y - y) + square(that.z - z)
  }
  private def round(v: Double): Double = (v * 100).toInt / 100.0
  override def toString = s"(${round(x)}, ${round(y)}, ${round(z)})"
}


object KMeansRunner {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 20,
    Key.exec.maxWarmupRuns -> 40,
    Key.exec.benchRuns -> 25,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val kMeans = new KMeans()

    val numPoints = 500000
    val eta = 0.01
    val k = 32
    val points = kMeans.generatePoints(k, numPoints)
    val means = kMeans.initializeMeans(k, points)

    val seqtime = standardConfig measure {
      kMeans.kMeans(points, means, eta)
    }
    println(s"sequential time: $seqtime ms")

    val partime = standardConfig measure {
      val parPoints = points.par
      val parMeans = means.par
      kMeans.kMeans(parPoints, parMeans, eta)
    }
    println(s"parallel time: $partime ms")
    println(s"speedup: ${seqtime / partime}")
  }

}
