package com.clearscore.schemas.reports

import java.sql.Timestamp

case class AverageCreditScoreReport(batch_timestamp: Timestamp, average_credit_score: Double)
