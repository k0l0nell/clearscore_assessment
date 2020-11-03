package com.clearscore.runners

/**
 * Main class to launch data ingestion for all 4 Questions
 *
 * */
object AllQuestions {
  /**
   * @param args program arguments
   *            {{{
   *             "--source" location where the Account and Credit Report files are held
   *             "--target" location where to save the results of the analysis to.
   *             "--master" sets the spark.master property for the application
   *             }}}
   * */
    def main(args: Array[String]): Unit = {
      Question1.main(args)
      Question2.main(args)
      Question3.main(args)
      Question4.main(args)
    }
  }
