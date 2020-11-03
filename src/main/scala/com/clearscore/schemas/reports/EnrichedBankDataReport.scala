package com.clearscore.schemas.reports

/** data structure for Question 4
 * */
case class EnrichedBankDataReport(
                                   user_uuid: String,
                                   employment_status: String,
                                   bank_name: String,
                                   number_of_active_bank_accounts: Integer,
                                   total_outstanding_balance: BigDecimal)
