object SumOfTwo {
  /**
   * Напишите функцию, которая принимает два целых числа и возвращает их сумму.
   * */

  def sumOfTwo(x: Int, y: Int): Int = x + y

  def main(args: Array[String]): Unit = {
    println(sumOfTwo(2, 2))
    println(sumOfTwo(-1, 2))
    println(sumOfTwo(0, 0))
    println(sumOfTwo(2, -100))
    println(sumOfTwo(-2, -100))
    println(sumOfTwo(100000000, 100000000))
  }
}
