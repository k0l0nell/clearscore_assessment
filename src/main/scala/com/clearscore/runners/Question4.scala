package com.clearscore.runners

import com.clearscore.aggregations.{CreditReport, EmploymentStatus, Enrichment}
import org.apache.spark.sql.SparkSession

/**
 * Main class to launch data ingestion for Question4
 *
 * @param "--source" location where the Account and Credit Report files are held
 * @param "--target" location where to save the results of the analysis to.
 * @param "--master" sets the spark.master property for the application
 *
 * */
object Question4 {
  def main(args: Array[String]): Unit = {
    println(this.getClass.getName)

    var source_location: Option[String] = None
    var target_location: Option[String] = None
    var master: Option[String] = None

    args.sliding(2, 2).toList.collect {
      case Array("--source", source: String) => source_location = Some(source)
      case Array("--target", target: String) => target_location = Some(target)
      case Array("--master", value: String) => master = Some(value)
    }

    val spark = master match {
      case Some(setting) => SparkSession.builder().master(setting).getOrCreate()
      case None => SparkSession.builder().getOrCreate()
    }


    val accounts = spark.read.json(s"${source_location.getOrElse(Defaults.source_location)}/accounts")
    val reports = spark.read.json(s"${source_location.getOrElse(Defaults.source_location)}/reports/*/*/*")

    val uReports = CreditReport.extractBankDetails(reports)
    val uAccounts = EmploymentStatus.extractAccountUsers(accounts)

    Enrichment.userEmploymentAndBankData(uAccounts,uReports)
      .coalesce(1)
      .write.mode("append").format("CSV")
      .options(Defaults.write_options)
      .save(s"${target_location.getOrElse(Defaults.target_location)}/${this.getClass.getCanonicalName}")

    spark.stop()
  }
}
