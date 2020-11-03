package com.clearscore.schemas.reports

import java.sql.Timestamp

/** data structure for Question 4
 * */
case class EnrichedBankDataReport(
                                   batch_timestamp: Timestamp,
                                   user_uuid: String,
                                   employment_status: String,
                                   bank_name: String,
                                   number_of_active_bank_accounts: String,
                                   total_outstanding_balance: String)