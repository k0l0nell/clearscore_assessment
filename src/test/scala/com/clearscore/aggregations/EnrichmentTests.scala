package com.clearscore.aggregations

import com.clearscore.SparkSuite
import org.scalatest.FlatSpec


class EnrichmentTests extends FlatSpec with SparkSuite {
  "Enrichments" should "produce an Employment and Bank report for ALL users in the data store" in {
    val reports = spark.read.json("./src/test/resources/bulk-reports/reports/*/*/*")
    val accounts = spark.read.json("./src/test/resources/bulk-reports/accounts")

    val uniqueAccounts = accounts.select("uuid").distinct().count

    val uReports = CreditReport.extractBankDetails(reports)
    val uAccounts = EmploymentStatus.extractAccountUsers(accounts)

    val bankReport = Enrichment.userEmploymentAndBankData(uAccounts,uReports)

    assert(
      uniqueAccounts === bankReport.select("user_uuid").distinct().count() &&
        accounts.select("uuid").except(bankReport.select("user_uuid")).count === 0 &&
        bankReport.select("user_uuid").except(accounts.select("uuid")).count === 0
    )

  }
}
