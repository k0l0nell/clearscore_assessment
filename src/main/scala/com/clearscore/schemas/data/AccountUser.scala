package com.clearscore.schemas.data

case class UserName ( lastName: String, firstName: String, format: String )
case class Salary ( amount: BigInt, `type`: String, currency: String )
case class IdDocument ( `type`: String, `value`: String)

case class AccountUser(
                        name: UserName,
                        bankName: String,
                        dateOfBirth: String,
                        employmentStatus: Option[String],
                        salary: Salary,
                        id: BigInt,
                        idDocuments: Array[IdDocument],
                        residentialStatus: String
                      )

case class AccountUserRecord(uuid: String, user: AccountUser)

