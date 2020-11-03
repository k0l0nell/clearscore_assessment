package com.clearscore.schemas.data

/** Case class outlining the structure of a ScoreBlock in a Credit report
 *
 * @param ReasonCode //TODO
 * @param RequestID //TODO
 * @param Score The credit score obtained in this credit report.
 * @param ScoreName //TODO
 * @param Scorecard_Identifier //TODO
 * */
case class Delphi(
                   ReasonCode: String,
                   RequestID: String,
                   Score: String,
                   ScoreName: String,
                   Scorecard_Identifier: String
                 )
/** A flattened case class representing a ScoreBlock in a user's CreditReport
 *
 * @param `user-uuid` Account User UUID
 * @param `pulled-timestamp` Timestamp when this report was pulled
 * @param delphi A flattened ScoreBlock
 * */
case class UserScoreRecord(`user-uuid`: String, `pulled-timestamp`: String , delphi: Delphi)
/** A flattened case class representing a ScoreBlock in a user's CreditReport
 *
 * @param `user-uuid` Account User UUID
 * @param `pulled-timestamp` Timestamp when this report was pulled
 * @param numActiveBankAcc The number of active bank accounts in a user's most recent Credit Report
 * @param totOutstandingBalance Total outstanding balance in a user's most recent Credit Report
 * */
case class UserBankRecord(`user-uuid`: String, `pulled-timestamp`: String, numActiveBankAcc: String, totOutstandingBalance: String)
