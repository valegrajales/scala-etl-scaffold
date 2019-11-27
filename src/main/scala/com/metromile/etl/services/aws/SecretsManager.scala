package com.metromile.etl.services.aws

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.model.{GetSecretValueRequest, GetSecretValueResponse, InvalidParameterException, InvalidRequestException, ResourceNotFoundException}

class SecretsManager(endpoint: String = "secretsmanager.us-west-2.amazonaws.com", region: String = "us-west-2") {
  val secretManagerClient: SecretsManagerClient = SecretsManagerClient.builder().region(Region.US_WEST_2).build()

  def getSecretString(secretName: String, version: String, versionStage: String = "AWSCURRENT"): String = {
    val secretValueRequest: GetSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).versionId(version).versionStage(versionStage).build()

    var secretValueResult: GetSecretValueResponse = null
    try {
      secretValueResult = secretManagerClient.getSecretValue(secretValueRequest)
    }
    catch {
      case e: ResourceNotFoundException =>
        println("The requested secret " + secretName + " was not found")
      case e: InvalidRequestException =>
        println("The request was invalid due to: " + e.getMessage)
      case e: InvalidParameterException =>
        println("The request had invalid params: " + e.getMessage)
    }

    // Depending on whether the secret was a string or binary, one of these fields will be populated
    if (secretValueResult.secretString() != null) {
      return secretValueResult.secretString()
    } else {
      return null
    }
  }

  def getSecretBinary(secretName: String, version: String, versionStage: String = "AWSCURRENT"): String = {
    val secretValueRequest: GetSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).versionId(version).versionStage(versionStage).build()

    var secretValueResult: GetSecretValueResponse = null
    try {
      secretValueResult = secretManagerClient.getSecretValue(secretValueRequest)
    }
    catch {
      case e: ResourceNotFoundException =>
        println("The requested secret " + secretName + " was not found")
      case e: InvalidRequestException =>
        println("The request was invalid due to: " + e.getMessage)
      case e: InvalidParameterException =>
        println("The request had invalid params: " + e.getMessage)
    }

    // Depending on whether the secret was a string or binary, one of these fields will be populated
    if (secretValueResult.secretBinary() != null) {
      return secretValueResult.secretString()
    } else {
      return null
    }
  }
}
