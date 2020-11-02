package com.clearscore

import org.apache.spark.sql.SparkSession

trait SparkSuite {
  //Remember to set HADOOP_HOME on Windows

  val spark = SparkSession.builder().config("spark.master","local[4]").getOrCreate()

}