package com.clearscore.aggregations

import com.clearscore.schemas.data.AccountUserRecord
import com.clearscore.schemas.reports.EmploymentStatusReport
import org.apache.spark.sql.functions.{col, countDistinct, lit}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}

/** Implements methods to deliver `Question 2 of the assessment`
 *
 * We care about wanting to know the different employment statuses of our users. This helps with understanding our demographics of our user
 * base, and drives business decisions.
 *
 * */
object EmploymentStatus {

  /** Creates a Dataset of AccountUsers for a set of Accounts
   *
   * @param dataFrame the collection of Accounts
   * @return a Dataset containing `AccountUserRecords`
   * */
  def extractAccountUsers(dataFrame: DataFrame): Dataset[AccountUserRecord] = {
    dataFrame
      .select("uuid","account.user")
      .as[AccountUserRecord](Encoders.product[AccountUserRecord])
  }

  /** Creates a Dataset of AccountUsers for a set of Accounts
   *
   * @param dataset the collection of `AccountUserRecords`
   * @return `EmploymentStatusReport` containing the breakdown of user employment status for this run
   * */
  def byUser(dataset: Dataset[AccountUserRecord]): Dataset[EmploymentStatusReport] = {
    dataset
      .groupBy(col("user.employmentStatus"))
        .agg(countDistinct("uuid").as("user_count"))
      .withColumn("batch_timestamp",lit(java.sql.Timestamp.from(java.time.Instant.now)))
      .withColumnRenamed("employmentStatus","employment_status")
        .na.fill("Unknown", Seq("employment_status"))
      .select(Encoders.product[EmploymentStatusReport].schema.names.map(col):_*)
      .as[EmploymentStatusReport](Encoders.product[EmploymentStatusReport])
  }
}
