import Main._
import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.logging.log4j.scala.Logging
import org.apache.spark.sql.DataFrame
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

class MainTest extends AnyFlatSpec
    with Logging
    with BeforeAndAfter
    with DataFrameComparer {

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Dataframe API test")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  var countriesDF: DataFrame = _

  before {
    countriesDF = spark.read
      .format("json")
      .option("mode", "FAILFAST")
      .option("multiline", value = true)
      .load("src/test/resources/countries.json")
  }

  it should "print countriesDF schema and show" in {
    countriesDF.printSchema
    countriesDF.show
  }

  /**
   * Тестируем функцию borders
   * */
  it should "output schema of borders function must match" in {
    val etalonSchema = StructType(
      Array(
        StructField("Country", StringType, true),
        StructField("NumBorders", IntegerType, false),
        StructField("BorderCountries", StringType, true)
      )
    )
    assert(borders(spark, countriesDF).schema === etalonSchema)
  }

  it should "Russia borders with 14 countries" in {
    assert(borders(spark, countriesDF).select("NumBorders").take(1)(0).mkString === "14")
  }

  it should "borders function return correct dataframe" in {
    import spark.implicits._

    val expectedDf = Array(
      ("Russian Federation", 14, "AZE,BLR,CHN,EST,FIN,GEO,KAZ,PRK,LVA,LTU,MNG,NOR,POL,UKR")
    ).toSeq.toDF("Country", "NumBorders", "BorderCountries")

    val resultDf = borders(spark, countriesDF)
    assertSmallDatasetEquality(expectedDf, resultDf, orderedComparison = false)
  }

  /**
   * Тестируем функцию languages
   * */
  it should "output schema of languages function must match" in {
    val etalonSchema = StructType(
      Array(
        StructField("Language", StringType, true),
        StructField("NumCountries", LongType, false),
        StructField("Countries", ArrayType(StringType,false), false)
      )
    )
    assert(languages(spark, countriesDF).schema === etalonSchema)
  }

  it should "Russian is the only language :)" in {
    assert(languages(spark, countriesDF).select("NumCountries").take(1)(0).mkString === "1")
  }

  it should "languages function return correct dataframe" in {
    import spark.implicits._

    val expectedDf = Seq(
      ("Russian", 1L, Array("Russian Federation"))
    ).toDF("Language", "NumCountries", "Countries")

    val resultDf = languages(spark, countriesDF)
    assertSmallDatasetEquality(expectedDf, resultDf, orderedComparison = false, ignoreNullable = true)
  }
}
