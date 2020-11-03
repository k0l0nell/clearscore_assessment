package com.clearscore.schemas.reports

import java.sql.Timestamp

/** Output case class for Question 3
 *
 * @param batch_timestamp Timestamp at which the results were generated
 * @param bin_name Display Name for the Credit Score bin (e.g. 101-150)
 * @param measure Measure value aggregated over the `bin_name`
 * @param measure_label Column expression used to generate the `measure` (e.g. count('user-uuid)). Added so this case class can be reused for other bin calculations.
 * */
case class BinnedCreditScoreReport(batch_timestamp: Timestamp, bin_name: String, measure: BigInt, measure_label:String)
