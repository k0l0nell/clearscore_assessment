package com.clearscore.runners

object Defaults {
  val target_location: String = "./out"
  val source_location: String = "./src/test/resources/bulk-reports"

  val write_options = Map("format"-> "CSV", "header"-> "true")
}
