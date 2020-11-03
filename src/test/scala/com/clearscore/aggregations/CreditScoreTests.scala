package com.clearscore.aggregations

import com.clearscore.SparkSuite
import com.clearscore.aggregations.CreditScore.{countByCreditScoreBin, extractUserScores, showLatestReportOnly}
import com.clearscore.schemas.data._
import com.clearscore.schemas.reports.AverageCreditScoreReport
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, count, sum}
import org.scalatest.FlatSpec

class CreditScoreTests extends FlatSpec with SparkSuite {

  import spark.implicits._

  private val creditScores: Seq[UserScoreRecord] = Seq(
    UserScoreRecord(`user-uuid`= "1",delphi= Delphi(ReasonCode = "0", RequestID="1",Score="700",ScoreName="Algo",Scorecard_Identifier = "4"),`pulled-timestamp` = "1"),
    UserScoreRecord(`user-uuid`= "1",delphi= Delphi(ReasonCode = "0", RequestID="12",Score="23",ScoreName="Algo",Scorecard_Identifier = "4"),`pulled-timestamp` = "2"),
    UserScoreRecord(`user-uuid`= "1",delphi= Delphi(ReasonCode = "0", RequestID="19",Score="200",ScoreName="Algo",Scorecard_Identifier = "4"),`pulled-timestamp` = "3"),
    UserScoreRecord(`user-uuid`= "1",delphi= Delphi(ReasonCode = "0", RequestID="100",Score="0",ScoreName="Algo",Scorecard_Identifier = "4"),`pulled-timestamp` = "5"),
    UserScoreRecord(`user-uuid`= "1",delphi= Delphi(ReasonCode = "0", RequestID="2221",Score="120",ScoreName="Algo",Scorecard_Identifier = "4"),`pulled-timestamp` = "4")
  )

  private val sampleAverage = creditScores.map( _.delphi.Score.toDouble).foldLeft(0.0)(_ + _) / creditScores.size
  private val noReportFound = AverageCreditScoreReport(java.sql.Timestamp.from(java.time.Instant.now),0)


  "CreditScore" should s"average to $sampleAverage on sample data" in {
    val average:Dataset[AverageCreditScoreReport] = creditScores.toDS().transform(CreditScore.average)

    assert(average.collect().headOption.getOrElse(noReportFound).average_credit_score === sampleAverage)
  }

  private val userScores: Dataset[UserScoreRecord] = spark.read.json("./src/test/resources/bulk-reports/reports/*/*/*")
    .transform(extractUserScores)
  private val assessmentAverage = 532.1726618705036

  it should s"average to $assessmentAverage for the assessment data " in {
    val average = userScores.transform(CreditScore.average)

    assert(average.collect().headOption.getOrElse(noReportFound).average_credit_score === assessmentAverage)
  }

  it should s"bin into ${CreditScore.binRange.size} bins" in {
    val binnedScore = userScores
      .transform(countByCreditScoreBin(col("user-uuid")))

    binnedScore.show

    assert(binnedScore.count === CreditScore.binRange.size)
  }

  it should "be able to only show the latest report for a user" in {
    val uniqueUsers = userScores.select("user-uuid").distinct.count

    val binnedScore = userScores
      .transform(showLatestReportOnly)
      .transform(countByCreditScoreBin(col("user-uuid")))

    binnedScore.show

    val numberUsersBinned = binnedScore
      .agg(sum("measure").as("sum"))
      .collect()
      .headOption.getOrElse(Row(0L)).getAs[Long]("sum")

    assert( numberUsersBinned === uniqueUsers)
  }

}
