package observatory

case class Location(lat: Double, lon: Double)

case class Station (
  stn: Int,
  wban: Int,
  latitude: Double,
  longtitude: Double
)

case class Temperature(
  stn: Int,
  wban: Int,
  month: Int,
  day: Int,
  temperature: Double
)

case class Record(date: java.sql.Date, location: Location, temperature: Double)

case class Color(red: Int, green: Int, blue: Int)
object Color {
  val defaultColorMap: Seq[(Double, Color)] = Seq(
    (60, Color(red = 255, green = 255, blue = 255)),
    (32, Color(red = 255, green = 0, blue = 0)),
    (12, Color(red = 255, green = 255, blue = 0)),
    (0, Color(red = 0, green = 255, blue = 255)),
    (-15, Color(red = 0, green = 0, blue = 255)),
    (-27, Color(red = 255, green = 0, blue = 255)),
    (-50, Color(red = 33, green = 0, blue = 107)),
    (-60, Color(red = 0, green = 0, blue = 0))
  )

  val devColorMap: Seq[(Double, Color)] = Seq(
    (7, Color(red = 0, green = 0, blue = 0)),
    (4, Color(red = 255, green = 0, blue = 0)),
    (2, Color(red = 255, green = 255, blue = 0)),
    (0, Color(red = 255, green = 255, blue = 255)),
    (-2, Color(red = 0, green = 255, blue = 255)),
    (-7, Color(red = 0, green = 0, blue = 255))
  )
}
