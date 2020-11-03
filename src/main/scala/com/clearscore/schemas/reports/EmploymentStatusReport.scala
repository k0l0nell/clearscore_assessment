package com.clearscore.schemas.reports

import java.sql.Timestamp

/** data structure for Question 2
 * */
case class EmploymentStatusReport(batch_timestamp: Timestamp,employment_status:String, user_count: BigInt)
