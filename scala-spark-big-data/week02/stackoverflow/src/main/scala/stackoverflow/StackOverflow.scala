package stackoverflow

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import annotation.tailrec
import scala.reflect.ClassTag

/**
  * A raw stackoverflow posting, either a question or an answer
  *
  * @param postingType    Type of the post. Type 1 = question, type 2 = answer.
  * @param id             Unique id of the post (regardless of type).
  * @param acceptedAnswer Id of the accepted answer post. This information is
  *                       optional, so maybe be missing indicated by an empty
  *                       string.
  * @param parentId       For an answer: id of the corresponding question. For
  *                       a question:missing, indicated by an empty string.
  * @param score          The StackOverflow score (based on user votes).
  * @param tag            The tag indicates the programming language that the
  *                       post is about, in case it's a question, or missing in
  *                       case it's an answer.
  */
case class Posting(postingType: Int, id: Int, acceptedAnswer: Option[Int], parentId: Option[Int], score: Int, tags: Option[String]) extends Serializable


/** The main class */
object StackOverflow extends StackOverflow {

  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("StackOverflow")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  /** Main function */
  def main(args: Array[String]): Unit = {

    // The lines from the csv file as strings
    val lines: RDD[String] = sc.textFile("src/main/resources/stackoverflow/stackoverflow.csv")
    // The raw Posting entries for each line
    val raw: RDD[Posting] = rawPostings(lines)
    // Questions and answers grouped together
    val grouped: RDD[(Int, Iterable[(Posting, Posting)])] = groupedPostings(raw)
    // Questions and scores
    val scored: RDD[(Posting, Int)]  = scoredPostings(grouped)
    // Pairs of (language, score) for each question
    val vectors: RDD[(Int, Int)] = vectorPostings(scored)
//    assert(vectors.count() == 2121822, "Incorrect number of vectors: " + vectors.count())

    val means   = kmeans(sampleVectors(vectors), vectors, debug = true)
    val results = clusterResults(means, vectors)
    printResults(results)
  }
}


/** The parsing and kmeans methods */
class StackOverflow extends Serializable {

  /** Languages */
  val langs =
    List(
      "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
      "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")

  /** K-means parameter: How "far apart" languages should be for the kmeans algorithm? */
  def langSpread = 50000
  assert(langSpread > 0, "If langSpread is zero we can't recover the language from the input data!")

  /** K-means parameter: Number of clusters */
  def kmeansKernels = 45

  /** K-means parameter: Convergence criteria */
  def kmeansEta: Double = 20.0D

  /** K-means parameter: Maximum iterations */
  def kmeansMaxIterations = 120


  //
  //
  // Parsing utilities:
  //
  //

  /** Load postings from the given file */
  def rawPostings(lines: RDD[String]): RDD[Posting] =
    lines.map(line => {
      val arr = line.split(",")
      Posting(postingType =    arr(0).toInt,
              id =             arr(1).toInt,
              acceptedAnswer = if (arr(2) == "") None else Some(arr(2).toInt),
              parentId =       if (arr(3) == "") None else Some(arr(3).toInt),
              score =          arr(4).toInt,
              tags =           if (arr.length >= 6) Some(arr(5).intern()) else None)
    })


  /**
    * Group the questions and answers together
    *
    * Ideally, we want to obtain an RDD with the pairs of
    * <code>(Question, Iterable[Answer])</code>. However, grouping on the
    * question posting directly is expensive because the question posting
    * information is not available globally without shuffling data around the
    * cluster. So, a better alternative is to match on the QID, which is
    * already available in answer postings via their <code>partentId</code> field
    * thus producing an <code>RDD[(QID, Iterable[(Question, Answer))]</code>.
    */
  def groupedPostings(postings: RDD[Posting]): RDD[(Int, Iterable[(Posting, Posting)])] = {
    val questions = postings
      .filter(posting => posting.postingType == 1)
      .map(posting => (posting.id, posting))
    val answers = postings
      .filter(posting => posting.postingType == 2 && posting.parentId.isDefined)
      .map(posting => (posting.parentId.get, posting))

    questions.join(answers).groupByKey()
  }


  /**
    * Compute the maximum score for each posting.
    *
    * Returns an RDD containing pairs of (a) questions and (b) the score of the
    * answer with the highest score (note: this does not have to be the answer
    * marked as "acceptedAnswer"!).
    */
  def scoredPostings(grouped: RDD[(Int, Iterable[(Posting, Posting)])]): RDD[(Posting, Int)] = {

    def answerHighScore(as: Array[Posting]): Int = {
      var highScore = 0
          var i = 0
          while (i < as.length) {
            val score = as(i).score
                if (score > highScore)
                  highScore = score
                  i += 1
          }
      highScore
    }

    grouped // RDD[(Int, Iterable[(Posting, Posting)])]
      .values // RDD[Iterable[(Posting, Posting)]]
      .map { qa => (qa.head._1, answerHighScore(qa.map(_._2).toArray))
    }

  }

  /**
    * Compute the vectors for the kmeans.
    *
    * Transform the scored RDD into a vectors RDD containing the vectors to be
    * clustered. In our case, the vectors should be pairs with two components (in
    * the listed order!):
    *   i. Index of the language (in the langs list) multiplied by the langSpread
    *      factor.
    *  ii. The highest answer score (computed above).
    *
    * The langSpread factor is provided. It corresponds to how far
    * away are languages from the clustering algorithm's point of view. Its value
    * is set to 50000 and it, basically, makes sure posts about different
    * programming languages have at least distance 50000 using the distance
    * measure provided by the euclideanDist function. For a value of 50000, the
    * languages are too far away to be clustered together at all, resulting in a
    * clustering that only takes scores into account for each language
    * (similarly to partitioning the data across languages and then clustering
    * based on the score). A more interesting (but less scientific) clustering
    * occurs when langSpread is set to 1 (we can't set it to 0, as it loses
    * language information completely), where we cluster according to the score.
    *
    * * See which language dominates the top questions when langSpread is set to 1?
    * * Do you think that partitioning your data would help?
    * * Have you thought about persisting some of your data? Can you think of why
    *   persisting your data in memory may be helpful for this algorithm?
    * * Of the non-empty clusters, how many clusters have "Java" as their label
    *   (based on the majority of questions, see above)? Why?
    * * Only considering the "Java clusters", which clusters stand out and why?
    * * How are the "C# clusters" different compared to the "Java clusters"?
    */
  def vectorPostings(scored: RDD[(Posting, Int)]): RDD[(Int, Int)] = {
    /** Return optional index of first language that occurs in `tags`. */
    def firstLangInTag(tag: Option[String], ls: List[String]): Option[Int] = {
      if (tag.isEmpty) None
      else if (ls.isEmpty) None
      else if (tag.get == ls.head) Some(0) // index: 0
      else {
        val tmp = firstLangInTag(tag, ls.tail)
        tmp match {
          case None => None
          case Some(i) => Some(i + 1) // index i in ls.tail => index i+1
        }
      }
    }

    val v = scored.map {
      case (question, highestScore) =>
        val landIndx = firstLangInTag(question.tags, langs).get
        (landIndx * langSpread, highestScore)
    }
    v.cache()
  }


  /** Sample the vectors */
  def sampleVectors(vectors: RDD[(Int, Int)]): Array[(Int, Int)] = {

    assert(kmeansKernels % langs.length == 0, "kmeansKernels should be a multiple of the number of languages studied.")
    val perLang = kmeansKernels / langs.length

    // http://en.wikipedia.org/wiki/Reservoir_sampling
    def reservoirSampling(lang: Int, iter: Iterator[Int], size: Int): Array[Int] = {
      val res = new Array[Int](size)
      val rnd = new util.Random(lang)

      for (i <- 0 until size) {
        assert(iter.hasNext, s"iterator must have at least $size elements")
        res(i) = iter.next
      }

      var i = size.toLong
      while (iter.hasNext) {
        val elt = iter.next
        val j = math.abs(rnd.nextLong) % i
        if (j < size)
          res(j.toInt) = elt
        i += 1
      }

      res
    }

    val res =
      if (langSpread < 500)
        // sample the space regardless of the language
        vectors.takeSample(false, kmeansKernels, 42)
      else
        // sample the space uniformly from each language partition
        vectors.groupByKey.flatMap({
          case (lang, vectors) => reservoirSampling(lang, vectors.toIterator, perLang).map((lang, _))
        }).collect()

    assert(res.length == kmeansKernels, res.length)
    res
  }


  //
  //
  //  Kmeans method:
  //
  //

  /** Main kmeans computation */
  @tailrec final def kmeans(means: Array[(Int, Int)], vectors: RDD[(Int, Int)], iter: Int = 1, debug: Boolean = false): Array[(Int, Int)] = {
    val newMeans = means.clone()

    // we classify the vectors according to their closest mean
    val closest = vectors.map { point => // (language, score)
      (findClosest(point, means), point)
    }.groupByKey() // (closestmean, (language, score))

    // after we have classified the vectors we updated the mean values
    // to the average of all the points in the cluster
    closest.mapValues { points: Iterable[(Int, Int)] =>
      averageVectors(points)
    }.collect().map(m => newMeans.update(m._1, m._2))

    val distance = euclideanDistance(means, newMeans)

    if (debug) {
      println(s"""Iteration: $iter
                 |  * current distance: $distance
                 |  * desired distance: $kmeansEta
                 |  * means:""".stripMargin)
      for (idx <- 0 until kmeansKernels)
      println(f"   ${means(idx).toString}%20s ==> ${newMeans(idx).toString}%20s  " +
              f"  distance: ${euclideanDistance(means(idx), newMeans(idx))}%8.0f")
    }

    if (converged(distance))
      newMeans
    else if (iter < kmeansMaxIterations)
      kmeans(newMeans, vectors, iter + 1, debug)
    else {
      println("Reached max iterations!")
      newMeans
    }
  }

  //
  //
  //  Kmeans utilities:
  //
  //

  /** Decide whether the kmeans clustering converged */
  def converged(distance: Double) =
    distance < kmeansEta


  /** Return the euclidean distance between two points */
  def euclideanDistance(v1: (Int, Int), v2: (Int, Int)): Double = {
    val part1 = (v1._1 - v2._1).toDouble * (v1._1 - v2._1)
    val part2 = (v1._2 - v2._2).toDouble * (v1._2 - v2._2)
    part1 + part2
  }

  /** Return the euclidean distance between two points */
  def euclideanDistance(a1: Array[(Int, Int)], a2: Array[(Int, Int)]): Double = {
    assert(a1.length == a2.length)
    var sum = 0d
    var idx = 0
    while(idx < a1.length) {
      sum += euclideanDistance(a1(idx), a2(idx))
      idx += 1
    }
    sum
  }

  /** Return the closest point */
  def findClosest(p: (Int, Int), centers: Array[(Int, Int)]): Int = {
    var bestIndex = 0
    var closest = Double.PositiveInfinity
    for (i <- 0 until centers.length) {
      val tempDist = euclideanDistance(p, centers(i))
      if (tempDist < closest) {
        closest = tempDist
        bestIndex = i
      }
    }
    bestIndex
  }


  /** Average the vectors */
  def averageVectors(ps: Iterable[(Int, Int)]): (Int, Int) = {
    val iter = ps.iterator
    var count = 0
    var comp1: Long = 0
    var comp2: Long = 0
    while (iter.hasNext) {
      val item = iter.next
      comp1 += item._1
      comp2 += item._2
      count += 1
    }
    ((comp1 / count).toInt, (comp2 / count).toInt)
  }

  //
  //
  //  Displaying results:
  //
  //
  def clusterResults(means: Array[(Int, Int)], vectors: RDD[(Int, Int)]): Array[(String, Double, Int, Int)] = {
    val closest = vectors.map(p => (findClosest(p, means), p)) // (closest, (lang, score))
    val closestGrouped = closest.groupByKey() // (closest, List((lang, score)))

    val median = closestGrouped.mapValues { vs => // Iterable[(langSpread, score)]
      val langIndx = vs.groupBy(_._1).maxBy(_._2.size)._1
      // most common language in the cluster
      val langLabel: String = langs(langIndx / langSpread)
      val clusterSize: Int = vs.size
      // percent of the questions in the most common language
      val langPercent: Double = 100 * vs.count(li => li._1 == langIndx) / clusterSize.toDouble
      val sortedScores = vs.map(_._2).toList.sorted
      val midIndx = clusterSize / 2
      val medianScore: Int =  {
        if (clusterSize % 2 == 0)
          (sortedScores(midIndx - 1) + sortedScores(midIndx)) / 2
        else
          sortedScores(midIndx)
      }

      (langLabel, langPercent, clusterSize, medianScore)
    }

    median.collect().map(_._2).sortBy(_._4)
  }

  def printResults(results: Array[(String, Double, Int, Int)]): Unit = {
    println("Resulting clusters:")
    println("  Score  Dominant language (%percent)  Questions")
    println("================================================")
    for ((lang, percent, size, score) <- results)
      println(f"${score}%7d  ${lang}%-17s (${percent}%-5.1f%%)      ${size}%7d")
  }
}
