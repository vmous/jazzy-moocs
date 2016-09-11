package lecturecode

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GeneratorsSuite extends FunSuite {

  import Generators._

  /**
    * Didn't know an easy way to test it. Some thoughts are the following:
    *  - http://www.eg.bucknell.edu/~xmeng/Course/CS6337/Note/master/node42.html
    *  - http://stackoverflow.com/questions/2130621/how-to-test-a-random-generator
    *  - http://programmers.stackexchange.com/questions/147134/how-should-i-test-randomness
    */
  test("testing random generation") {
  }

}
