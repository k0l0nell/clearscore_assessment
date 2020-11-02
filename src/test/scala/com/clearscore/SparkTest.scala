package com.clearscore

import org.scalatest.FlatSpec

class SparkTest extends FlatSpec with SparkSuite {

  "SparkSuite" should " have fired up a Spark Session" in {

    assert(spark.conf.getAll.nonEmpty)
  }

}
