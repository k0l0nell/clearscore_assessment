package com.clearscore.runners

/**
 * Main class to launch data ingestion for all 4 Questions
 *
 * @param "--source" location where the Account and Credit Report files are held
 * @param "--target" location where to save the results of the analysis to.
 * @param "--master" sets the spark.master property for the application
 * */
object AllQuestions {
    def main(args: Array[String]): Unit = {
      Question1.main(args)
      Question2.main(args)
      Question3.main(args)
      Question4.main(args)
    }
  }
