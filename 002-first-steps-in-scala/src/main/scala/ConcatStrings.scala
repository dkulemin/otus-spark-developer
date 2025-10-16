object ConcatStrings {
  /**
   * Напишите функцию, которая принимает список строк
   * и возвращает новую строку, состоящую из всех строк списка, разделенных пробелами.
   * */

  private def concatStrings(ss: List[String]): String = ss.mkString(" ")

  def main(args: Array[String]): Unit = {
    val list = List("My", "homework", "is", "done!")
    println(concatStrings(list))
  }
}
