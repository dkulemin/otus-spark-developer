object Greeting {
  /** Напишите программу на Scala, которая принимает имя пользователя с клавиатуры
   * и выводит приветственное сообщение.
   * */

  private def greeting(name: String): Unit = println(s"Hello, $name!")

  def main(args: Array[String]): Unit = {
    greeting(scala.io.StdIn.readLine())
  }
}
