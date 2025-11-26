import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Main extends App {
  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Dataset API")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  import spark.implicits._

  case class Trip(
                   VendorID: Int,
                   tpep_pickup_datetime: java.time.LocalDateTime,
                   tpep_dropoff_datetime: java.time.LocalDateTime,
                   passenger_count: Int,
                   trip_distance: Double,
                   RatecodeID: Int,
                   store_and_fwd_flag: String,
                   PULocationID: Int,
                   DOLocationID: Int,
                   payment_type: Int,
                   fare_amount: Double,
                   extra: Double,
                   mta_tax: Double,
                   tip_amount: Double,
                   tolls_amount: Double,
                   improvement_surcharge: Double,
                   total_amount: Double)

  case class TaxiZone(LocationID: Int, Borough: String, Zone: String, service_zone: String)

  val tripDS = spark.read.parquet("data/yellow_taxi_jan_25_2018").as[Trip]

  val zonesDS = spark.read.option("header", "true").csv("data/taxi_zones.csv")
    .select($"LocationID".cast("Int"), $"Borough", $"Zone", $"service_zone")
    .as[TaxiZone]

  tripDS.join(zonesDS, $"PULocationID" === $"LocationID")
    .groupBy($"Zone")
    .agg(
      count("*").alias("trips"),
      min("trip_distance").alias("min_trip_distance"),
      avg("trip_distance").alias("avg_trip_distance"),
      max("trip_distance").alias("max_trip_distance"),
      std($"trip_distance").alias("std_trip_distance"),
    )
    .write
    .mode("overwrite")
    .parquet("result/zone_trips")
}

