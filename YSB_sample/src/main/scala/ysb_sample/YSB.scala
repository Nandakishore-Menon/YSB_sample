package org.multiquery
import org.apache.spark.sql.functions._
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.streaming.StreamingQueryListener
import org.apache.spark.sql.streaming.StreamingQueryListener._
//import org.apache.spark.sql.execution.ui._


object YSB extends App{
  val spark = SparkSession
    .builder()
    .appName("Sample YSB")
    .master("local[*]")
    .getOrCreate()
  import spark.implicits._

    val listener = new Listener()
    spark.streams.addListener(listener)

    spark.sql("SET spark.sql.streaming.metricsEnabled=true")

  val KAFKA_TOPIC_NAME = "ysb";
  val KAFKA_BOOTSTRAP_SERVERS_CONS = "localhost:9092";
  println("First SparkContext:");
  println("APP Name :"+spark.sparkContext.appName);
  println("Deploy Mode :"+spark.sparkContext.deployMode);
  println("Master :"+spark.sparkContext.master);

  val schema = StructType(Array(
    StructField("ip_address", StringType, false),
    StructField("user_id", StringType, false),
    StructField("page_id", StringType, false),
    StructField("ad_id", StringType, false),
    StructField("event_type", StringType, false)
  ))

  val mapping = spark.read.csv("data/mapping.csv").toDF("ad_id", "c_id")
  mapping.printSchema()

  val inp = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS_CONS)
    .option("subscribe", KAFKA_TOPIC_NAME)
    .option("startingOffsets", "earliest")
    .load()

  val df = inp.selectExpr("CAST(value AS STRING) as json", "timestamp")
    .select(from_json($"json", schema = schema).as("data"), $"timestamp")
    .select($"data.*", $"timestamp")
    .withWatermark("timestamp", "5  seconds")

//  val joined = df.join(mapping, df("ad_id") === mapping("ad_id"), "inner").drop(mapping("ad_id"));

  val query1 = new YSBQuery(1, spark);

  val q1 = query1.runQuery(df, mapping);

  def q1_save = (df: Dataset[Row], batchId: Long) => {
    df.write.format("csv")
      .option("path", "target/output/query1/window%d".format(batchId))
      .save()
  }


  val q1_result = q1
    .writeStream
    .trigger(Trigger.ProcessingTime("5 seconds"))
    .foreachBatch(q1_save)
    .outputMode("append")
    .start()


  spark.streams.awaitAnyTermination();
}