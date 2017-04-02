package stackoverflow

import org.scalatest.{FunSuite, BeforeAndAfterAll}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {

  // Scala has 2 answers, PHP has an answer, Java does not.
  val postings = Seq(
    Posting(1, 1, acceptedAnswer = Some(3), None, 15, Some("Scala")),
    Posting(1, 2, acceptedAnswer = None, None, 10, Some("Java")),
    Posting(2, 3, None, parentId = Some(1), 10, Some("Scala")),
    Posting(1, 4, acceptedAnswer = Some(5), None, 15, Some("PHP")),
    Posting(2, 5, acceptedAnswer = None, parentId = Some(4), 15, Some("PHP")),
    Posting(2, 6, acceptedAnswer = None, parentId = Some(1), 20, Some("Scala"))
  )

  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  lazy val postingsRDD = StackOverflow.sc.parallelize(postings)

  override def afterAll(): Unit = {
    import StackOverflow._
    sc.stop()
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("grouped questions and answers") {
    val grouped = testObject.groupedPostings(postingsRDD).collect()

    // Two questions since Java has no answers
    assert(grouped.length == 2)
    // Two answers for Scala question
    assert(grouped.find{ case (qid, _) => qid == 1 }.map{ case (_, pairs) => pairs.size}.getOrElse(0) == 2)
    // No Java question since there are no answers
    assert(!grouped.exists{ case (qid, _) => qid == 2})
  }

  test("highest score") {
    val grouped = testObject.groupedPostings(postingsRDD)
    val highestScores = testObject.scoredPostings(grouped)

    // Answer with highest score for Scala is 20
    assert(highestScores.collect { case (question, score) if question.id == 1 => score }.first() == 20)
    // Answer with highest score for PHP is 15 (only 1 answer)
    assert(highestScores.collect { case (question, score) if question.id == 4 => score }.first() == 15)
  }

  test("vectors") {
    val grouped = testObject.groupedPostings(postingsRDD)
    val highestScores = testObject.scoredPostings(grouped)
    val vectors = testObject.vectorPostings(highestScores).collect()

    // There should exist tuples for Scala and PHP
    assert(vectors.length == 2)
    assert(vectors exists { case (langFactor, _) => langFactor == testObject.langSpread * 10 })
    assert(vectors exists { case (langFactor, _) => langFactor == testObject.langSpread * 2 })
  }


}
