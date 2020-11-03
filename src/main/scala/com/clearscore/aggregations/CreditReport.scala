package com.clearscore.aggregations

import com.clearscore.schemas.data.{Delphi, UserBankRecord, UserScoreRecord}
import com.clearscore.schemas.reports.{AverageCreditScoreReport, BinnedCreditScoreReport}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{avg, col, count, explode, lit, max}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{Column, DataFrame, Dataset, Encoders}

import scala.reflect.runtime.universe._

/** Implements methods to deliver `Question 1,3 and 4 of the assessment`
 *
 * #1 What you need to do is to give us the average credit score across all credit reports. This is from all reports that you get given. In order to obtain
 * the credit score from a report, you need to look at the report -> ScoreBlock -> Delphi object.
 *
 * #3 We would like to know the spread of the credit score ranges of our users. This is so that we can get further information on our demographics. This
 * should be using the latest report for each user, only. The range that we want to know is by every group of 50. So, that would mean 0-50, 51-100,
 * 101-150 etc.
 *
 * #4 For each of our users, we want to know some of the summaries available on their bank data joined up with their employment statuses. We want
 * this data from the latest credit report for each user, again.
 *
 * */
object CreditReport {

  /**
   * creates the bin ranges for the CreditScore (max is 700)
   * */
  private[clearscore] val binRange = (0 to 651 by 50)
    .map { v =>
      val lower =  if ( v > 0) v+1 else v
      val upper = v+50
      (s"${"%03d".format(lower)}-${"%03d".format(upper)}",lower,upper)
    }

  /** Calculates the average credit score over all reports
   *
   * @param scoreBlocks credit report scores
   * @return `AverageCreditScoreReport` containing the average credit score for this run
   * */
  def averageCreditScore(scoreBlocks: Dataset[UserScoreRecord]): Dataset[AverageCreditScoreReport] = {
    scoreBlocks
      .withColumn("Score",col("delphi.Score").cast(DoubleType))
      .agg(avg("Score").as("average_credit_score"))
      .withColumn("batch_timestamp",lit(java.sql.Timestamp.from(java.time.Instant.now)))
      .select(Encoders.product[AverageCreditScoreReport].schema.names.map(col):_*)
      .as[AverageCreditScoreReport](Encoders.product[AverageCreditScoreReport])
  }

  /** Creates a Dataset of scoreBlocks for a set of CreditReports
   *
   * @param dataFrame the collection of scoreBlock
   * @return a Dataset containing `Delphi` scoreblocks
   * */
  def extractUserScores(dataFrame: DataFrame): Dataset[UserScoreRecord] = {
    dataFrame
      .select(col("user-uuid"),col("pulled-timestamp"),col("report.ScoreBlock.Delphi").as("delphi" ))
      .withColumn("delphi",explode(col("delphi")))
      .select(Encoders.product[UserScoreRecord].schema.names.map(col):_*)
      .as[UserScoreRecord](Encoders.product[UserScoreRecord])
  }

  /** Creates a Dataset of Bank data for a set of Credit Reports
   * @param dataFrame the collection of Credit Reports
   * @return a Dataset containing `UserBankRecord`s
   * */
  def extractBankDetails(dataFrame: DataFrame): Dataset[UserBankRecord] = {
    val bankNamespace = "report.Summary.Payment_Profiles.CPA.Bank"
    dataFrame
      .select(
        col("user-uuid"),
        col("pulled-timestamp"),
        col(s"${bankNamespace}.Total_number_of_Bank_Active_accounts_").as("numActiveBankAcc"),
        col(s"${bankNamespace}.Total_outstanding_balance_on_Bank_active_accounts").as("totOutstandingBalance")
      )
      .select(Encoders.product[UserBankRecord].schema.names.map(col):_*)
      .as[UserBankRecord](Encoders.product[UserBankRecord])
  }

  /** Aggregates a count of `column` in Credit Score bins
   *
   *  @param column column to aggregate (count)
   *  @return the report by credit score bin
   * */
  def countByCreditScoreBin(column: Column)(dataset: Dataset[UserScoreRecord]): Dataset[BinnedCreditScoreReport] = {
    val bins = dataset.sparkSession.createDataset(binRange.toList)(Encoders.tuple(Encoders.STRING,Encoders.scalaInt,Encoders.scalaInt)).toDF("bin_name","min","max")
    val aggregate = count(column)

    bins.join(dataset, dataset("delphi.Score") >= bins("min") and dataset("delphi.Score") <= bins("max") ,"left_outer")
      .groupBy("bin_name")
      .agg(aggregate.as("measure"))
      .withColumn("batch_timestamp",lit(java.sql.Timestamp.from(java.time.Instant.now)))
      .withColumn("measure_label",lit(aggregate.expr.toString()))
      .select(Encoders.product[BinnedCreditScoreReport].schema.names.map(col):_*)
      .as[BinnedCreditScoreReport](Encoders.product[BinnedCreditScoreReport])
  }

  /** Only returns the latest report for every user
   *  @param dataset CreditScore records to filter
   *  @return the latest CreditScore report
   * */
  def showLatestReportOnly[T <: Product : TypeTag ](dataset: Dataset[T]): Dataset[T] = {
    val latestSpec = Window.partitionBy("user-uuid")

    dataset
      .withColumn("latestReportPulled",max(dataset("pulled-timestamp")).over(latestSpec))
      .filter(col("pulled-timestamp") === col("latestReportPulled"))
      .select(Encoders.product[T].schema.names.map(col):_*)
      .as[T](Encoders.product[T])
  }

}
