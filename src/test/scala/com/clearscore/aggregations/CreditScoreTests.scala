package com.clearscore.aggregations

import com.clearscore.SparkSuite
import com.clearscore.aggregations.CreditScore.extractScoreBlocks
import com.clearscore.schemas.data.Delphi
import com.clearscore.schemas.reports.AverageCreditScoreReport
import org.apache.spark.sql.Dataset
import org.scalatest.FlatSpec

class CreditScoreTests extends FlatSpec with SparkSuite {

  import spark.implicits._

  val creditScores: Seq[Delphi] = Seq(
    Delphi(ReasonCode = "0", RequestID="1",Score="700",ScoreName="Algo",Scorecard_Identifier = "4"),
    Delphi(ReasonCode = "0", RequestID="12",Score="23",ScoreName="Algo",Scorecard_Identifier = "4"),
    Delphi(ReasonCode = "0", RequestID="19",Score="200",ScoreName="Algo",Scorecard_Identifier = "4"),
    Delphi(ReasonCode = "0", RequestID="100",Score="0",ScoreName="Algo",Scorecard_Identifier = "4"),
    Delphi(ReasonCode = "0", RequestID="2221",Score="120",ScoreName="Algo",Scorecard_Identifier = "4")
  )

  val sampleAverage = creditScores.map( _.Score.toDouble).foldLeft(0.0)(_ + _) / creditScores.size

  val noReportFound = AverageCreditScoreReport(java.sql.Timestamp.from(java.time.Instant.now),0)


  "CreditScore" should s"average to $sampleAverage on sample data" in {
    val average:Dataset[AverageCreditScoreReport] = creditScores.toDS().transform(CreditScore.average)

    assert(average.collect().headOption.getOrElse(noReportFound).average_credit_score === sampleAverage)
  }

  val assessmentAverage = 532.1726618705036
  it should s"average to $assessmentAverage for the assessment data " in {
    val average = spark.read.json("./src/test/resources/bulk-reports/reports/*/*/*")
      .transform(extractScoreBlocks)
      .transform(CreditScore.average)

    assert(average.collect().headOption.getOrElse(noReportFound).average_credit_score === assessmentAverage)
  }

}
