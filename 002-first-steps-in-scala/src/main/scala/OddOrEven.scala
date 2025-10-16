object OddOrEven {
  /**
   * Напишите программу, которая принимает число с клавиатуры и выводит,
   * является ли оно четным или нечетным.
   * */

  private def oddOrEven(x: Int): String = if (x.abs % 2 == 1)  "odd" else "even"

  def main(args: Array[String]): Unit = {
    println(oddOrEven(scala.io.StdIn.readLine().toInt))
  }
}
