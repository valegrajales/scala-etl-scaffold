package com.metromile.etl.common

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

abstract class BaseETL {
  private var input_files: List[String] = List[String]()
  private var etl_name: String = ""

  def extract(spark: SparkSession): DataFrame

  def transform(spark: SparkSession, df: DataFrame): DataFrame

  def load(spark: SparkSession, df: DataFrame): DataFrame

  def execute(spark: SparkSession): Unit = {
    val logger: Logger = LoggerFactory.getLogger("BaseETL")
    logger.info("Loading data from source " + input_files)
    val df = extract(spark)
    val df_transformed = transform(spark, df)
    val save_result = load(spark, df_transformed)
  }

  def set_etl_name(name:String): Unit = {
    etl_name = name
  }

  def get_etl_name(): String = {
    etl_name
  }

  def set_input_files(files: List[String]): Unit = {
    input_files = files
  }

  def get_input_files(): List[String] = {
    input_files
  }
}
