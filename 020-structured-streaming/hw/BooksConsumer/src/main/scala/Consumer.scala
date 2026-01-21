import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StringType, DoubleType, IntegerType, StructType, StructField}

object Consumer {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("BooksConsumer")
      .config("spark.log.level", "WARN")
      .getOrCreate()

    import spark.implicits._

    val booksDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:19092")
      .option("subscribe", "books")
      .option("startingOffsets", "earliest")
      .load()

    val jsonSchema = StructType(Array(
      StructField("Name", StringType, false),
      StructField("Author", StringType, false),
      StructField("User Rating", StringType, false),
      StructField("Reviews", StringType, false),
      StructField("Price", StringType, false),
      StructField("Year", StringType, false),
      StructField("Genre", StringType, false)
    ))

    booksDF
      .withColumn("parsed_json", F.from_json($"value".cast("String"), jsonSchema))
      .withColumns(Map(
        "name" -> $"parsed_json.Name",
        "author" -> $"parsed_json.Author",
        "userRating" -> $"parsed_json.User Rating".cast(DoubleType),
        "reviews" -> $"parsed_json.Reviews".cast(IntegerType),
        "price" -> $"parsed_json.Price".cast(IntegerType),
        "year" -> $"parsed_json.Year".cast(IntegerType),
        "genre" -> $"parsed_json.Genre"
      ))
      .filter($"userRating" >= 4.0)
      .select(
        "name",
        "author",
        "userRating",
        "reviews",
        "price",
        "year",
        "genre"
      )
      .writeStream
      .format("parquet")
      .trigger(Trigger.Once())
      .option("checkpointLocation", "tmp/checkpoint_dir")
      .option("path", "output")
      .start()
      .awaitTermination()
  }
}
