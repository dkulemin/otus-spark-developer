import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, StructField, StructType, StringType}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
object Main extends App {
  val customSchema = StructType(
    Array(
      StructField("sepal_length", DoubleType),
      StructField("sepal_width", DoubleType),
      StructField("petal_length", DoubleType),
      StructField("petal_width", DoubleType),
      StructField("species", StringType)
    )
  )

  val spark = SparkSession
    .builder
    .master("local[*]")
    .appName("Iris")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  val df = spark.read
    .option("header", "true")
    .schema(customSchema)
    .csv("data/IRIS.csv")

  val tt = df.randomSplit(Array(0.8, 0.2))
  val training = tt(0)
  val test = tt(1)

  val indexer = new StringIndexer()
    .setInputCols(Array("species"))
    .setOutputCols(Array("target"))

  val assembler = new VectorAssembler()
    .setInputCols(Array("sepal_length", "sepal_width", "petal_length", "petal_width"))
    .setOutputCol("features")

  val rf = new RandomForestClassifier()
    .setLabelCol("target")
    .setFeaturesCol("features")
    .setSeed(42)

  val pipeline = new Pipeline().setStages(Array(indexer, assembler, rf))
  val pipelineModel = pipeline.fit(training)

  pipelineModel.write.overwrite().save(s"output/randomForestModel")

  val testResult = pipelineModel.transform(test)

  val evaluator = new MulticlassClassificationEvaluator()
    .setLabelCol("target")
    .setPredictionCol("prediction")
    .setMetricName("accuracy")

  val score = evaluator.evaluate(testResult)

  println(s"Test score: $score")
}

