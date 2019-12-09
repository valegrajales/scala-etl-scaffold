package com.metromile.etl.common

//import com.metromile.spark.data.ParseDeviceData
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

object BaseJobExecutor {
  val logger: Logger = LoggerFactory.getLogger(BaseJobExecutor.getClass)

  /*def etlExecution(spark: SparkSession, process: ParseDeviceData, config: Config): Unit = {
    logger.info("Starting ETL: " + config.getString("etl.name"))

    var inputs: String = config.getString("etl.s3.protocol") +
      config.getString("etl.s3.input_bucket") + "/" +
      config.getString("etl.s3.input_prefix") +
      config.getString("etl.s3.input_file")

    logger.info("Start reading " + inputs)

    process.set_input_files(List(inputs))

    process.execute(spark)
  }*/
}
