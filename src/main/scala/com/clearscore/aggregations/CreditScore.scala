package com.clearscore.aggregations

import com.clearscore.schemas.{AverageCreditScoreReport, Delphi}
import org.apache.arrow.vector.types.pojo.ArrowType.Timestamp
import org.apache.spark.sql.functions.{avg, col, explode, lit}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}

/** Implements methods to deliver `Question 1 of the assessment`
 *
 * What you need to do is to give us the average credit score across all credit reports. This is from all reports that you get given. In order to obtain
 * the credit score from a report, you need to look at the report -> ScoreBlock -> Delphi object.
 *
 * */
object CreditScore {

  /** Calculates the average
   *
   * @param scoreBlocks `Delphi` scoreblocks
   * @return `AverageCreditScoreReport` containing the average
   * */
  def average(scoreBlocks: Dataset[Delphi]): Dataset[AverageCreditScoreReport] = {
    scoreBlocks
      .withColumn("Score",col("Score").cast(DoubleType))
      .agg(avg("Score").as("average_credit_score"))
      .withColumn("batch_timestamp",lit(java.sql.Timestamp.from(java.time.Instant.now)))
      .as[AverageCreditScoreReport](Encoders.product[AverageCreditScoreReport])
  }

  /** Creates a Dataset of scoreBlocks for a set of CreditReports
   *
   * @param dataFrame the collection of scoreBlock
   * @return a Dataset contains `Delphi` scoreblocks
   * */
  def extractScoreBlocks(dataFrame: DataFrame): Dataset[Delphi] = {
    dataFrame
      .select("report.ScoreBlock.Delphi")
      .withColumn("Delphi",explode(col("Delphi")))
      .select("Delphi.*")
      .as[Delphi](Encoders.product[Delphi])
  }

}
