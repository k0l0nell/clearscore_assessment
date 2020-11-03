# ClearScore Data Engineering Tech Test
In response to your tech challenge I have written a couple routines that will give you the much desired answers to your questions. 

## Getting Started
I have chosen the implement the data processing in SPARK 2.4.5 (Scala 2.11) using SBT as the build tool.

## Compiling and running the tests
Building the project is a simple as: 
```shell script
sbt compile
```

The automated tests (Unit Tests), located in `src/test/scala`, are implemented using the ScalaTest FlatSpec and can be run as shown below. 
```shell script
sbt test 
```

The API documentation can be generated/updated by running
```shell script
sbt doc
``` 
and can be found under `./target/scala-2.11/api/`

## Deployment
The data structures for this project can be generated locally (via `sbt run`) or can be submitted to a cluster as a job (via `spark-submit`)
#### Local Mode
To run this application in local mode just run:
```shell script
sbt run --master local[*]
```

The application will then ask you what data structures you want to generate.
```text
Multiple main classes detected, select one to run:

 [1] com.clearscore.runners.AllQuestions
 [2] com.clearscore.runners.Question1
 [3] com.clearscore.runners.Question2
 [4] com.clearscore.runners.Question3
 [5] com.clearscore.runners.Question4

Enter number:
```
Simply enter the number of the desired pipeline and press return. 

**By default**, the source files residing under `./src/test/resources/bulk-reports` will be processed, and the results persisted under `./out`. 

To modify this behaviour you can use `--source` and `--target` as illustrated below 

```shell script
sbt run --master local[*] --target ./output --source ~/data/bulk-reports
```

#### Cluster Deployment
In order to submit the application to a Spark cluster the compiled code needs to be packaged as per below
```shell script
sbt package
```
the resulting jar,located in `./target/scala-2.11`, can then be used by the driver as follows
```shell script
spark-submit --master <MASTER_URI> --class com.clearscore.runners.AllQuestions /path/to/clearscore_assessment_2.11-0.1.jar --source /path/to/sources --target /path/to/out 
```