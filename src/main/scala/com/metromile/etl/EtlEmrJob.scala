package com.metromile.etl

import com.typesafe.config.{ConfigFactory, Config}
import com.metromile.etl.common.BaseJobExecutor
import org.apache.spark.sql.SparkSession

/**
 * @author Valentina Grajales
 *         laura.grajales@globant.com
 */
object EtlEmrJob {

  def main(args : Array[String]) {
    val etlConfiguration: Config = ConfigFactory.load()

    // create Spark context with Spark configuration
    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("Parse device data job")
      .getOrCreate()

    BaseJobExecutor.etlExecution(sparkSession, new ParseDeviceData(), etlConfiguration)
  }

}