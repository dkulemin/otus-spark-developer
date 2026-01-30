import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object Main extends App {
  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Dataframe API")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  val countriesDF = spark.read
    .option("multiline","true")
    .json("data/countries.json")

  def borders(session: SparkSession, df: DataFrame): DataFrame = {
    import session.implicits._

    df.filter(size($"borders") > 4)
      .withColumns(
        Map(
          "Country" -> $"name.official",
          "NumBorders" -> size($"borders"),
          "BorderCountries" -> array_join($"borders", ",")
        )
      )
      .select("Country", "NumBorders", "BorderCountries")
      .orderBy(desc("NumBorders"))
      .toDF()
  }

  def languages(session: SparkSession, df: DataFrame): DataFrame = {
    import session.implicits._

    df.select(
        $"name.official",
        explode(array($"languages.*")).alias("Language")
      )
      .filter(!$"Language".isNull)
      .groupBy("Language")
      .agg(
        count($"official").alias("NumCountries"),
        array_agg($"official").alias("Countries")
      )
      .orderBy(desc("NumCountries"))
      .toDF()
  }

  borders(spark, countriesDF).write.mode("overwrite").parquet("result/borders")
  languages(spark, countriesDF).write.mode("overwrite").parquet("result/languages")

}

