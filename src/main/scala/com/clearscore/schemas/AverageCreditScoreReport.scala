package com.clearscore.schemas

import java.sql.Timestamp

case class AverageCreditScoreReport(batch_timestamp: Timestamp, average_credit_score:Double )
