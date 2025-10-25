object ForComprehension extends App {
  /**
   * 1. Создайте список чисел от 1 до 10.
   * */

  val list: List[Int] = ((1 to 10).toList)

  println(list)

  /**
   * 2. Используйте for comprehension, чтобы создать новый список, содержащий квадраты чисел из исходного списка.
   * */

  val listOfSquares = for {
    i <- list
  } yield i * i

  println(listOfSquares)

  /**
   * 3. Используйте for comprehension, чтобы создать новый список, содержащий только четные числа из исходного списка.
   * */

  val listOfEvens = for {
    i <- list
    if i % 2 == 0
  } yield i

  println(listOfEvens)
}
