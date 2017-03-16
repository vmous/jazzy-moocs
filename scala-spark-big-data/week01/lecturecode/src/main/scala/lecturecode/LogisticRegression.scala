package lecturecode

import org.apache.spark.SparkContext

/**
  * Implementation of logistic regression.
  *
  * Note: This is not a working example and it serves only as pseudocode for
  * understanding a Scala implementation of logistic regression.
  */
object LogisticRegression {
  case class Point(x: Double, y: Double)

  def parsePoint(raw: String): Point = {
    Point(0, 0)
  }

  def logisticRegression(inputFile: String, sc: SparkContext) = {
 //   val points = sc.textFile(inputFile)
 //     .map(parsePoint)
 //     .persist
 //
 //   val d = 2
 //
 //   import org.apache.spark.util.Vector
 //   var w = Vector.zeros(d)
 //
 //   val numIterations: Long = 1000
 //   val alpha = 0.1
 //
 //   for (i <- 1 to numIterations) {
 //     val gradient = points.map { p =>
 //       ( 1 / ( 1 + scala.math.exp(-p.y * w.dot(p.x)) ) - 1 ) * p.y * p.y
 //     }.reduce(_ + _)
 //     w -= alpha * gradient
 //   }
  }

}
