package com.clearscore.runners

import com.clearscore.aggregations.EmploymentStatus
import org.apache.spark.sql.SparkSession

object Question2 {
  def main(args: Array[String]): Unit = {
    println(this.getClass.getCanonicalName)

    var source_location: Option[String] = None
    var target_location: Option[String] = None

    args.sliding(2, 2).toList.collect {
      case Array("--source", source: String) => source_location = Some(source)
      case Array("--target", target: String) => target_location = Some(target)
    }

    val spark = SparkSession.builder().appName(s"${this.getClass.getCanonicalName} Pipe").config("spark.master","local[*]").getOrCreate()

    spark.read
      .json(s"${source_location.getOrElse(Defaults.source_location)}/accounts")
      .transform(EmploymentStatus.extractAccountUsers)
      .transform(EmploymentStatus.byUser)
      .coalesce(1)
      .write.mode("append").format("CSV")
      .options(Defaults.write_options)
      .save(s"${target_location.getOrElse(Defaults.target_location)}/${this.getClass.getCanonicalName}")

    spark.stop()
  }
}
