package com.clearscore.schemas.data

/** Case Class representing the Name information associated with the Account
 * */
case class UserName ( lastName: String, firstName: String, format: String )
/** Case Class representing the salary associated with the Account
 * */
case class Salary ( amount: BigInt, `type`: String, currency: String )
/** Case Class representing the various ID Documents an Account may have
 * */
case class IdDocument ( `type`: String, `value`: String)
/** Case Class representing the Account User structure in the Account JSON
 * */
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

/** Case Class representing the user-uuid and Account User structure in the Account JSON
 * */
case class AccountUserRecord(uuid: String, user: AccountUser)

