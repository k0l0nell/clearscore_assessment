name := "clearscore_assessment"

version := "0.1"

scalaVersion := "2.11.11"


// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"

// JSON Parsing
libraryDependencies += "net.liftweb" %% "lift-json" % "3.4.2"