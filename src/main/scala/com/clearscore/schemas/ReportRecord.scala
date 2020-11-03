package com.clearscore.schemas

case class ReportRecord(
                         `account-id`: String,
                         `bureau-id`: String,
                         `client-ref`: String,
                         `pulled-timestamp`: String,
                         `report-id`: String,
                         `user-uuid`: String,
                         scoreBlock: Array[Delphi]
                       )
