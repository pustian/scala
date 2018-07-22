package com.wotung;
/**
  * Created by Administrator on 2018/7/21.
  */
import com.mongodb.spark._
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

object WriteMongodb {
  def main(args: Array[String]): Unit = {
    val spark =  SparkSession.builder().master("local").appName("MongoSparkController")
      .config("spark.mongodb.input.uri", "mongodb://192.168.1.12:27017/testDB.testCollection") // 指定mongodb输入
      .config("spark.mongodb.output.uri", "mongodb://192.168.1.12:27017/testDB.testCollection")  // 指定mongodb输出
      .getOrCreate();
    // 生成测试数据
    val documents = spark.sparkContext.parallelize((1 to 10).map(i => Document.parse(s"{test: $i}") ) );
    // 存储数据到mongodb
    MongoSpark.save(documents)
    // 加载数据
    val rdd = MongoSpark.load(spark)
    // 打印输出
    rdd.show();
  }
}
