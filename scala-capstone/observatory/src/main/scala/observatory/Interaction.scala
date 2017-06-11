package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import java.io.File
import scala.collection.parallel.ParSeq
import scala.math.{ atan, Pi, sinh, toDegrees }
import scala.reflect.io.Path

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile,
    *         as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {

    // Note below: (1 << zoom) === pow(2.0, zoom)
    val latitude: Double = toDegrees(
      atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1 << zoom))))
    )
    val longtitude: Double = x.toDouble / (1 << zoom) * 360.0 - 180.0
    Location(latitude, longtitude)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    val scale = 256

    val pxy: Seq[(Int, Int)] = for {
      py <- 0 until scale
      px <- 0 until scale
    } yield {
      (px, py)
    }

    val pixels: ParSeq[Pixel] = pxy.par.map { case (px: Int, py: Int) => {
      val location: Location = tileLocation(zoom + 8, x * scale + px, y * scale + py)
      val temperature: Double = Visualization.predictTemperature(temperatures, location)
      val color: Color = Visualization.interpolateColor(colors, temperature)
      Pixel(color.red, color.green, color.blue, 127)
    }}

    Image(256, 256, pixels.toArray)
  }

  def tile2(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {

    import observatory.Visualization._

    val TILE_SIZE = 256
    val SIZE_IMAGE = TILE_SIZE * TILE_SIZE
    val colors: Seq[(Double, Color)] = Seq(
      (60.0, Color(255,255,255)),
      (32.0, Color(255,0,0)),
      (12.0, Color(255,255,0)),
      (0.0, Color(0,255,255)),
      (-15.0, Color(0,0,255)),
      (-27.0, Color(255,0,255)),
      (-50.0, Color(33,0,107)),
      (-60, Color(0,0,0)))

    val tileLoc = tileLocation(zoom, x, y)
    val twoToPowerOfZoom = math.pow(2, zoom.toDouble)
    val latitudeSpacing = 180d/(twoToPowerOfZoom * TILE_SIZE)
    val longitudeSpacing = 360d/(twoToPowerOfZoom * TILE_SIZE)

    val coords: scala.collection.immutable.Seq[(Int, Int, Location)] = for{
      yCoord <- 0 until TILE_SIZE
      xCoord <- 0 until TILE_SIZE
      latCoord = tileLoc.lat - yCoord * latitudeSpacing
      longCoord = tileLoc.lon + xCoord * longitudeSpacing
    } yield (xCoord, yCoord, Location(latCoord, longCoord))

    val pixels: ParSeq[Pixel] = coords.toParArray.map{ case (xCoord, yCoord, location) =>
      val predTemp = predictTemperature(temperatures, location)
      val color = interpolateColor(colors, predTemp)
      Pixel(color.red, color.green, color.blue, 127)
    }

    Image(TILE_SIZE, TILE_SIZE, pixels.toArray)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    for {
      (year, data) <- yearlyData
      zoom <- 0 to 3
      y <- 0 until (1 << zoom).toInt;
      x <- 0 until (1 << zoom).toInt
    }{ generateImage(year, zoom, x, y, data) }
  }

  def generateImage(year: Int, zoom: Int, x: Int, y: Int, data: Iterable[(Location, Double)]): Unit = {
    val dirPath = Path(s"target/temperatures/$year/$zoom")
    dirPath.createDirectory(failIfExists = false)
    val imgPath = s"$dirPath/$x-$y.png"
    print(s"Generating tile image: \'$imgPath\'... ")
    tile(data, Color.defaultColorMap, zoom, x, y)
      .output(new File(s"$imgPath"))
    println("[Done]")
  }

}
