package com.clearscore.aggregations

import com.clearscore.schemas.data.{AccountUserRecord, UserBankRecord}
import com.clearscore.schemas.reports.EnrichedBankDataReport
import org.apache.spark.sql.{Dataset, Encoders}
import org.apache.spark.sql.functions.{col, lit}

/** Implements methods to deliver `Question 4 of the assessment`
 *
 * #4 For each of our users, we want to know some of the summaries available on their bank data joined up with their employment statuses. We want
 * this data from the latest credit report for each user, again.
 *
 * */
object Enrichment {

  /** Enriches the Account data set with the Credit Report Bank data set
   *
   * @param accountUser Account data set
   * @param bankData Bank data from Credit report
   *
   * @return a User Employment and Bank report
   * */
  def userEmploymentAndBankData(
                                 accountUser: Dataset[AccountUserRecord],
                                 bankData: Dataset[UserBankRecord]): Dataset[EnrichedBankDataReport] = {

    accountUser
      .join(
        bankData.transform(CreditReport.showLatestReportOnly),
        accountUser("uuid") === bankData("user-uuid"),
        "left_outer")
      .select(
        accountUser("uuid").as("user_uuid"),
        accountUser("user.employmentStatus").as("employment_status"),
        accountUser("user.bankName").as("bank_name"),
        bankData("numActiveBankAcc").as("number_of_active_bank_accounts"),
        bankData("totOutstandingBalance").as("total_outstanding_balance")
      )
      .withColumn("batch_timestamp",lit(java.sql.Timestamp.from(java.time.Instant.now)))
      .select(Encoders.product[EnrichedBankDataReport].schema.names.map(col):_*)
      .na.fill("Unknown",Seq("employment_status","bank_name"))
      .as[EnrichedBankDataReport](Encoders.product[EnrichedBankDataReport])
  }

}
