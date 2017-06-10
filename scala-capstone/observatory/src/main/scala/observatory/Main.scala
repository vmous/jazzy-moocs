package observatory

import Extraction.{ locateTemperatures, locationYearlyAverageRecords }
import Interaction.{ generateImage, generateTiles }

object Main extends App {

  // Milestone #3: Interactive data visualization
  val yearlyData: Seq[(Int, Iterable[(Location, Double)])] = for {
    year <- 1975 until 1976
  } yield (year, locationYearlyAverageRecords(locateTemperatures(year, "/stations.csv", s"/$year.csv")))
  generateTiles(yearlyData, generateImage)

}
