package com.clearscore.schemas.reports

import java.sql.Timestamp

/** Output case class for Question 2
 *
 * @param batch_timestamp Timestamp at which the results were generated.
 * @param employment_status The employment status category (e.g. STUDENT, FT_EMPLOYED,...)
 * @param user_count The number of users per Employment Status.
 * */
case class EmploymentStatusReport(batch_timestamp: Timestamp,employment_status:String, user_count: BigInt)
