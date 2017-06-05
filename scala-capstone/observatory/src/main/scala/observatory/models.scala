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

