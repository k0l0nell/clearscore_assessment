package com.clearscore.aggregations

import com.clearscore.SparkSuite
import com.clearscore.aggregations.EmploymentStatus.extractAccountUsers
import com.clearscore.schemas.data.{AccountUser, AccountUserRecord, IdDocument, Salary, UserName}
import com.clearscore.schemas.reports.EmploymentStatusReport
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.sum
import org.scalatest.FlatSpec

class EmploymentStatusTests extends FlatSpec with SparkSuite {

  import spark.implicits._

  private val sampleAccounts = Seq(
    AccountUserRecord(
      uuid="1",
      user=AccountUser(
        employmentStatus = Some("FT_EMPLOYED"),
        name=UserName("Dow","John","unknown"),
        bankName="TSB",dateOfBirth = "Yesterday",
        salary=Salary(amount=70,`type` = "full",currency="GBP"),
        id= 1,idDocuments = Array.empty[IdDocument],
        residentialStatus="Settled")
    ),
    AccountUserRecord(
      uuid="2",
      user=AccountUser(
        employmentStatus = Some("UNEMPLOYED"),
        name=UserName("Doe","Jane","full"),
        bankName="LLOYDS",dateOfBirth = "Day before Yesterday",
        salary=Salary(amount=0,`type` = "full",currency="GBP"),
        id= 1,idDocuments = Array.empty[IdDocument],
        residentialStatus="Settled")
    ),
    AccountUserRecord(
      uuid="3",
      user=AccountUser(
        employmentStatus = None,
        name=UserName("Aunt","Marry","full"),
        bankName="LLOYDS",dateOfBirth = "Day before Yesterday",
        salary=Salary(amount=40,`type` = "full",currency="GBP"),
        id= 1,idDocuments = Array.empty[IdDocument],
        residentialStatus="Settled")
    )
  )
  private val sampleReport: Dataset[EmploymentStatusReport] = sampleAccounts.toDS().transform(EmploymentStatus.byUser)
  private val numberOfUniqueSampleStatuses = sampleAccounts.map(_.user.employmentStatus).toSet.size

  "EmploymentStatus" should s"contain ${numberOfUniqueSampleStatuses} rows in the sample data set" in {
    assert(sampleReport.count === numberOfUniqueSampleStatuses)
  }

  it should "have one unknown employmentStatus in the sample data set" in {
    assert(sampleReport.filter(sampleReport("employment_status") === "Unknown").count === 1)
  }

  private val accountUsers = spark.read.json("./src/test/resources/bulk-reports/accounts")
    .transform(extractAccountUsers)

  it should " aggregate every user into a category" in {
    val aggregatedTotal = accountUsers
      .transform(EmploymentStatus.byUser)
      .agg(sum("user_count").as("sum"))
      .collect().headOption.getOrElse(Row(0L)).getAs[Long]("sum")

    assert(aggregatedTotal === accountUsers.select("uuid").distinct().count )
  }

}
