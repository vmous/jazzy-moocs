package lecturecode

import org.apache.spark.SparkContext

object WordCount {

  def wordCount(inputFile: String, sc: SparkContext) = {
    sc.textFile(inputFile) // ["a b b"]
      .flatMap(_.split(" ")) // ["a", "b", "b"]
      .map((_, 1)) // [("a", 1), ("b", 1), ("b", 1)]
      .reduceByKey { case (acc, c) => acc + c } // [("a", 1), ("b", 2)]
  }

}
