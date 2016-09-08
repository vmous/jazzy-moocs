package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BooksDBSuite extends FunSuite {

  import BooksDB._

  val books: Set[Book] = Set(
    Book(
      title = "Structure and Interpretation of Computer Programs",
      authors = List(
        "Abelson, Herald", "Sussman, Gerald J."
      )
    ),
    Book(
      title = "Introduction to Functional Programming",
      authors = List(
        "Bird, Richard", "Wadler, Phil"
      )
    ),
    Book(
      title = "Effective Java",
      authors = List(
        "Bloch, Joshua"
      )
    ),
    Book(
      title = "Effective Java 2",
      authors = List(
        "Bloch, Joshua"
      )
    ),
    Book(
      title = "Java Puzzlers",
      authors = List(
        "Bloch, Joshua", "Gafter, Neal"
      )
    ),
    Book(
      title = "Programming in Scala",
      authors = List(
        "Odersky, Martin", "Spoon, Lex", "Venners, Bill"
      )
    )
  )

  test("finding the book titles whose author's name is \"Bird\"") {
    val res = for {
      b <- books
      a <- b.authors
      if a startsWith "Bird,"
    } yield b.title

    assert(res === Set("Introduction to Functional Programming"))
  }

  test("finding the book titles whose author's name is \"Bird\" with translated \"for\"-expression") {
    val res = books.flatMap(i =>
      (i.authors).withFilter(j => j startsWith "Bird,")
        .map(j => i.title))

    assert(res === Set("Introduction to Functional Programming"))
  }

  test("finding the book titles that contain the word \"Program\"") {
    val res = for {
      b <- books
      if b.title.indexOf("Program") >= 0
    } yield b.title

    assert(res === Set("Structure and Interpretation of Computer Programs", "Introduction to Functional Programming", "Programming in Scala"))
  }

  test("finding the names of all authors who have written at least two books") {
    val res = for {
      b1 <- books
      b2 <- books
      if b1 != b2
      a1 <- b1.authors
      a2 <- b2.authors
      if a1 == a2
    } yield a1

    assert((res === Set("Bloch, Joshua")))
  }

}
