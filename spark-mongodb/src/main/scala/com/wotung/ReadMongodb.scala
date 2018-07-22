package com.wotung;

import com.mongodb.spark._;
import org.apache.spark.sql.SparkSession;

//object Mongo extends App {
//    override def main(args: Array[String]):Unit = {
object ReadMongodb {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().master("local").appName("MongoSparkController")
          .config("spark.mongodb.input.uri", "mongodb://192.168.1.11:27017/testDB.testCollection?authSource=admin")
          .config("spark.mongodb.output.uri", "mongodb://192.168.1.11:27017/testDB.testCollection?authSource=admin")
          .getOrCreate();
        val rdd = MongoSpark.load(spark);
        println("---count=" + rdd.count());
        if (rdd.count() > 0 ) {
            println("---first=" + rdd.first());
        }
    }
}