import org.apache.spark.sql.{SparkSession, functions => F}

object Producer {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("BooksProducer")
      .config("spark.log.level", "WARN")
      .getOrCreate()

    import spark.implicits._

    val booksDF = spark.read.option("header", "true").csv("data/bestsellers with categories.csv")

    booksDF.withColumn("value", F.to_json(F.struct(booksDF.columns.map(F.col): _*)))
      .write
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:19092")
      .option("topic", "books")
      .save()
  }
}
