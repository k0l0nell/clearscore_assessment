package com.clearscore.schemas.data

case class Delphi(
                   ReasonCode: String,
                   RequestID: String,
                   Score: String,
                   ScoreName: String,
                   Scorecard_Identifier: String
                 )

case class UserScoreRecord(`user-uuid`: String, `pulled-timestamp`: String , delphi: Delphi)
case class UserBankRecord(`user-uuid`: String, `pulled-timestamp`: String, numActiveBankAcc: String, totOutstandingBalance: String)
