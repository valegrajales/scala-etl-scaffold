package com.metromile.etl

import com.metromile.etl.common.BaseETL
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

class EtlEmr extends BaseETL{
  val logger: Logger = LoggerFactory.getLogger("ParseDeviceData")

  override def extract(spark: SparkSession): DataFrame = {
    val df = spark.read.json("s3a://metromile-stg/devicedatatest/device_stream-3-2019-02-19-04-11-11-69dc28e3-d888-4760-b35e-1e75d7dd8cfc.dms")
    df
  }

  override def transform(spark: SparkSession, df: DataFrame): DataFrame = {
    // Transform method
    df
  }

  override def load(spark: SparkSession, df: DataFrame): DataFrame = {
    // Load or save method
    df
  }

  def extractData(payloadEncoded: String, varType: String): Unit = {
    // Support method
  }

}
