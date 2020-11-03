package com.clearscore.schemas.reports

import java.sql.Timestamp

/** data structure for Question 3
 * */
case class BinnedCreditScoreReport(batch_timestamp: Timestamp, bin_name: String, measure: BigInt, measure_label:String)
