import Main.youngOrAdult

object Student extends App {
  /**
   * 6. Напишите программу, которая выполняет следующие действия:
   * Запрашивает у пользователя ввод имени, возраста и статуса (студент или нет).
   * Использует написанные выше функции и выводит на экран информацию о пользователе и его возрастную категорию.
   * */

  println("Введите имя:")
  val name: String = scala.io.StdIn.readLine()
  println("Введите возраст:")
  val age: Int = scala.io.StdIn.readLine().toInt
  println("Введите статус:")
  val isStudent: Boolean = scala.io.StdIn.readLine() match {
    case "1" => true
    case "true" => true
    case _ => false
  }

  println(s"Информация о пользователе: $name, возраст $age, ${youngOrAdult(age)} ${if (isStudent) "студент" else "выпускник"}")
}
