object ListPlusOne {
  /**
   * Создайте список из нескольких чисел (например, List(1, 2, 3, 4, 5))
   * и примените к нему функцию, которая увеличивает каждое число на 1.
   * Выведите получившийся список на экран.
   * */

  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5)
    list.map(x => x + 1).foreach(println)
  }
}
