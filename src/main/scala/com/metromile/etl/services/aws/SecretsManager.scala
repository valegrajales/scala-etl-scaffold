package com.metromile.etl.services.aws

import java.net.URI
import java.util.Base64

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.{
  CreateSecretRequest,
  CreateSecretResponse,
  DeleteSecretRequest,
  DeleteSecretResponse,
  GetSecretValueRequest,
  GetSecretValueResponse,
  InvalidParameterException,
  InvalidRequestException,
  ResourceExistsException,
  ResourceNotFoundException
}

/**
  * Services class fow AWS Secrets Manager
  *
  * @param endpoint SecretsManager endpoint to use for connection
  */
case class SecretsManager(
  endpoint: String = "secretsmanager.us-west-2.amazonaws.com"
) {

  val secretManagerClient: SecretsManagerClient = SecretsManagerClient
    .builder()
    .endpointOverride(URI.create(endpoint))
    .build()

  /**
    * Return a secret key string saved into AWS Secrets Manager Service
    *
    * @param secretName name of the secret key
    * @param versionStage version stage of secret key
    * @throws software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException
    * @return secret key string value
    */
  @throws(classOf[ResourceNotFoundException])
  @throws(classOf[InvalidRequestException])
  @throws(classOf[InvalidParameterException])
  def getSecretString(secretName: String,
                      versionStage: String = "AWSCURRENT"): String = {
    val secretValueRequest: GetSecretValueRequest = GetSecretValueRequest
      .builder()
      .secretId(secretName)
      .versionStage(versionStage)
      .build()

    var secretValueResult: GetSecretValueResponse = null
    var secret: String = null
    try {
      secretValueResult = secretManagerClient.getSecretValue(secretValueRequest)
    } catch {
      case e: ResourceNotFoundException =>
        println("The requested secret " + secretName + " was not found")
        throw e
      case e: InvalidRequestException =>
        println("The request was invalid due to: " + e.getMessage)
        throw e
      case e: InvalidParameterException =>
        println("The request had invalid params: " + e.getMessage)
        throw e
    }

    // Depending on whether the secret was a string or binary, one of these fields will be populated
    if (secretValueResult.secretString() != null) {
      secret = secretValueResult.secretString()
    } else {
      secret = new String(
        Base64.getDecoder.decode(secretValueResult.secretBinary().toString)
      )
    }
    secret
  }

  /**
    * Store a new secret key into AWS SecretsManager service
    *
    * @param secretName name of the secret key
    * @param secretString value of the secret key
    * @param description description of the secret key
    * @throws software.amazon.awssdk.services.secretsmanager.model.ResourceExistsException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException
    * @return new secret key name created
    */
  @throws(classOf[ResourceExistsException])
  @throws(classOf[InvalidRequestException])
  @throws(classOf[InvalidParameterException])
  def storeNewSecret(secretName: String,
                     secretString: String,
                     description: String): String = {
    val newSecret: CreateSecretRequest = CreateSecretRequest
      .builder()
      .name(secretName)
      .description(description)
      .secretString(secretString)
      .build()
    try {
      val secretValueResult: CreateSecretResponse =
        secretManagerClient.createSecret(newSecret)
      return secretValueResult.name()
    } catch {
      case e: ResourceExistsException =>
        println("The requested secret " + secretName + " already exist.")
        throw e
      case e: InvalidRequestException =>
        println("The request was invalid due to: " + e.getMessage)
        throw e
      case e: InvalidParameterException =>
        println("The request had invalid params: " + e.getMessage)
        throw e
    }
  }

  /**
    * Delete a secret from AWS SecretsManager service
    *
    * @param secretName name of the secret key
    * @param forceDeleteWithoutRecovery if true the key will be delete without taking recovery time, default true
    * @throws software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException
    * @throws software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException
    * @return deleted secret name
    */
  @throws(classOf[ResourceNotFoundException])
  @throws(classOf[InvalidRequestException])
  @throws(classOf[InvalidParameterException])
  def deleteStoredSecret(secretName: String,
                         forceDeleteWithoutRecovery: Boolean = true): String = {
    val deleteSecret: DeleteSecretRequest = DeleteSecretRequest
      .builder()
      .secretId(secretName)
      .forceDeleteWithoutRecovery(forceDeleteWithoutRecovery)
      .build()
    try {
      val secretValueResult: DeleteSecretResponse =
        secretManagerClient.deleteSecret(deleteSecret)
      secretValueResult.name()
    } catch {
      case e: ResourceNotFoundException =>
        println("The requested secret " + secretName + " doesn't exist.")
        throw e
      case e: InvalidRequestException =>
        println("The request was invalid due to: " + e.getMessage)
        throw e
      case e: InvalidParameterException =>
        println("The request had invalid params: " + e.getMessage)
        throw e
    }
  }

}
