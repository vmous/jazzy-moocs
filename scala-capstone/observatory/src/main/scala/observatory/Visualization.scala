package observatory

import com.sksamuel.scrimage.{Image, Pixel}

import scala.math.{ abs, acos, cos, pow, sin, toRadians}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val SHEPARD_POWER: Double = 3.0
  // Earth's radius in kilometers (https://en.wikipedia.org/wiki/Earth_radius)
  val EARTH_RADIUS: Double = 6371

  /**
    * Implements the great-circle distance.
    *
    * https://en.wikipedia.org/wiki/Great-circle_distance
    *
    * @param ρ The radius of the sphere. Default value is the Earth's radius.
    */
  def distance(loc1: Location, loc2: Location, ρ: Double = EARTH_RADIUS): Double = {
    val φ1: Double = toRadians(loc1.lat)
    val λ1: Double = toRadians(loc1.lon)
    val φ2: Double = toRadians(loc2.lat)
    val λ2: Double = toRadians(loc2.lon)

    val Δφ: Double = abs(φ1 - φ2)
    val Δλ: Double = abs(λ1 - λ2)

    // The spherical law of cosines
    // https://en.wikipedia.org/wiki/Spherical_law_of_cosines

    // simple formula
    val Δσ: Double = acos(sin(φ1) * sin(φ2) + cos(φ1) * cos(φ2) * cos(Δλ))

    // The arc distance
    // https://en.wikipedia.org/wiki/Arc_length
    ρ * Δσ
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    // For larger datasets can use `temperatures.par` but the Coursera grader
    // seems to be causing timeouts for unit-tests, probably due to excessive
    // parallelization for the small testing datasets.
    // The best solution that would scale better for the real application would
    // be to have a threshold on the `temperatures` size above which to use
    // parallilization.
    val (numerator: Double, denominator: Double) = temperatures
      .map { case (knownLocation: Location, knownTemperature: Double) => {
        val d = distance(knownLocation, location)
        if (d < 1) {
          // Station closer than 1km was found. Use its temperature directly.
          return knownTemperature
        }
        else {
          val wix = 1 / pow(d, SHEPARD_POWER)
          (wix, knownTemperature)
        }
      }}
      .foldLeft[(Double, Double)]((0.0, 0.0)) { case ((accA, accB), (wix, ui)) => (accA + (wix * ui), accB + wix) }

    numerator / denominator
  }

  private def lerp(lower: (Double, Color), upper: (Double, Color), value: Double): Color = {
    val x0 = (upper._1 - value) / (upper._1 - lower._1)
    val x1 = (value - lower._1) / (upper._1 - lower._1)
    Color(
      (lower._2.red * x0   + upper._2.red * x1 + 0.5).toInt,
      (lower._2.green * x0 + upper._2.green * x1 + 0.5).toInt,
      (lower._2.blue * x0  + upper._2.blue * x1 + 0.5).toInt
    )
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val pointsSorted: Seq[(Double, Color)] = points.toSeq.sortWith{ _._1 > _._1 }

    if (value >= points.maxBy{ case (v, c) => v }._1)
      points.maxBy { case (v, c) => v }._2
    else if (value <= points.minBy { case (v, c) => v }._1)
      points.minBy { case (v, c) => v }._2
    else if (pointsSorted.exists(point => point._1 == value)) {
      pointsSorted
        .dropWhile(point => point._1 != value)
        .head._2
    }
    else {
      val l: (Double, Color) = pointsSorted
        .filter { case (v, c) => v <= value }
        .head
      val u: (Double, Color) = pointsSorted
        .filter { case (v, c) => v > value }
        .last
      lerp(l, u, value)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val pixels: Seq[Pixel] = for {
      y <- 0 to 179
      x <- 0 to 359
    } yield {
      val location = Location(90.0 - y, x - 180.0)
      val temperature = predictTemperature(temperatures, location)
      val color = interpolateColor(colors, temperature)
      Pixel(color.red, color.green, color.blue, 100)
    }

    Image(360, 180, pixels.toArray)
  }

}
