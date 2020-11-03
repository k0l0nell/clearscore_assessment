package com.clearscore.schemas.reports

import java.sql.Timestamp

/** data structure for Question 1
 * */
case class AverageCreditScoreReport(batch_timestamp: Timestamp, average_credit_score: Double)
