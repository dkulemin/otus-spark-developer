import org.scalatest.funsuite.AnyFunSuite
import SumOfTwo.sumOfTwo
import Greeting.greeting
import OddOrEven.oddOrEven
import ConcatStrings.concatStrings

import java.io.ByteArrayOutputStream

class FirstStepsInScalaTests extends AnyFunSuite {

  test("1. Greeting %username%") {
    def catchStdout(name: String): Unit = {
      val out = new ByteArrayOutputStream()
      Console.withOut(out)(greeting(name))
      assert(out.toString.startsWith(s"Hello, $name!"))
    }

    val usernames = List("Vasya", "Petya", "Vova")

    usernames.foreach(catchStdout)
  }

  test("2. Testing sumOfTwo function") {
    assert(sumOfTwo(2, 2) === 4)
    assert(sumOfTwo(-1, 2) === 1)
    assert(sumOfTwo(0, 0) === 0)
    assert(sumOfTwo(2, -100) === -98)
    assert(sumOfTwo(-2, -100) === -102)
    assert(sumOfTwo(100000000, 100000000) === 200000000)
  }

  test("4. Testing oddOrEven function") {
    assert(oddOrEven(1) === "odd")
    assert(oddOrEven(-1) === "odd")
    assert(oddOrEven(2) === "even")
    assert(oddOrEven(-2) === "even")
    assert(oddOrEven(-1345) === "odd")
    assert(oddOrEven(1345) === "odd")
    assert(oddOrEven(4668) === "even")
    assert(oddOrEven(-4668) === "even")
    assert(oddOrEven(0) === "even")
  }

  test("6. Testing concatStrings function") {
    assert(concatStrings(List("Scala", "rocks!")) === "Scala rocks!")
    assert(concatStrings(List("Spark", "sparkling!")) === "Spark sparkling!")
    assert(concatStrings(List("Unit", "tests", "are", "awesome!")) === "Unit tests are awesome!")
    assert(concatStrings(List("My", "homework", "is", "done!")) === "My homework is done!")
  }

}
