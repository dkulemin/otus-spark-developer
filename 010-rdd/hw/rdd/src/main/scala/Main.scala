
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.fs._

object Main extends App {

  val conf = new SparkConf().setAppName("RDD").setMaster("local[*]").set("spark.log.level", "WARN")
  val sc = new SparkContext(conf)

  val fs = org.apache.hadoop.fs.FileSystem.get(sc.hadoopConfiguration)

  def deleteFile(fs: FileSystem, file: String): Unit = {
    if (fs.exists(new Path(file)))
      fs.delete(new Path(file), true)
  }

  val tripdata = sc.textFile("data/tripdata.csv", 3)
    .mapPartitionsWithIndex { (idx, iter) => if (idx == 0) iter.drop(1) else iter }
    .map(_.split(","))
    .filter(f => f.length > 7)
    .map(f => (f(7).toInt, f(1).substring(11, 13).toInt))

  val zones = sc.textFile("data/taxi_zone_lookup.csv", 3)
    .mapPartitionsWithIndex { (idx, iter) => if (idx == 0) iter.drop(1) else iter }
    .map(_.split(","))
    .map(x => (x(0).toInt, s"${x(1).filterNot(el => el == '"')} ${x(2).filterNot(el => el == '"')}"))

  val joinedRDD = tripdata.join(zones).map(f => ((f._2._1, f._2._2), f._1)).countByKey

  val textFile = "result/output"

  deleteFile(fs, textFile)

  sc.parallelize(
      joinedRDD.toSeq.sortBy(f => (f._1._1, f._1._2)).map(f => s"${f._1._1},${f._1._2},${f._2}"),
      1
  ).saveAsTextFile(textFile)

}

