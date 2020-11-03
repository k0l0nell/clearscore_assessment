package com.clearscore.schemas.reports

import java.sql.Timestamp

/** Output case class for Question 4
 *
 * @param batch_timestamp Timestamp at which the results were generated.
 * @param user_uuid Account User UUID
 * @param employment_status The most current Employment Status linked to the Account
 * @param bank_name The bank name linked to the Account
 * @param number_of_active_bank_accounts The number of active bank accounts in a user's most recent Credit Report
 * @param total_outstanding_balance Total outstanding balance in a user's most recent Credit Report
 * */
case class EnrichedBankDataReport(
                                   batch_timestamp: Timestamp,
                                   user_uuid: String,
                                   employment_status: String,
                                   bank_name: String,
                                   number_of_active_bank_accounts: String,
                                   total_outstanding_balance: String)