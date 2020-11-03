package com.clearscore.schemas.reports

import java.sql.Timestamp

/** Output case class for Question 1
 *
 *  @param batch_timestamp Timestamp at which the results were generated
 *  @param average_credit_score The average credit score over all reports ingested
 * */
case class AverageCreditScoreReport( batch_timestamp: Timestamp, average_credit_score: Double )
