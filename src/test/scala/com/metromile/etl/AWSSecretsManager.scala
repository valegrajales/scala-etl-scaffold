package com.metromile.etl

import java.net.URI

import org.scalatest.FlatSpec
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException
import software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException
import software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException

class AWSSecretsManager extends FlatSpec {

  ignore should "have not null values" in {
    val secretName = "testSecret"

    val secretManagerClient: SecretsManagerClient = SecretsManagerClient.builder().endpointOverride(URI.create("http://localhost:4584")).build()

    val secretValueRequest: GetSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).versionId("1").versionStage("AWSCURRENT").build()

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
      println(secretValueResult.secretString())
    }
    else {
      val binarySecretData: SdkBytes = secretValueResult.secretBinary()
      println(binarySecretData.toString)
    }

    assert(secretValueResult.secretString() != null)
  }

}
