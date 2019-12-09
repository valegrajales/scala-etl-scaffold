package com.metromile.etl.services.aws

import org.scalatest._
import software.amazon.awssdk.services.secretsmanager.model.{
  ResourceExistsException,
  ResourceNotFoundException
}

class SecretsManagerTest extends FlatSpec with Matchers {

  behavior of "AWS SecretsManager service"

  it should "Keep the key name when a new key is created" in {
    val secretManagerConnection =
      SecretsManager(endpoint = "http://localhost:4584")
    val newKey: String = secretManagerConnection.storeNewSecret(
      secretName = "Test",
      description = "Test secret key",
      secretString = """{"TestKey":"testKeyValue"}"""
    )
    newKey should equal("Test")
  }

  it should "Have key content for saved key name" in {
    val secretManagerConnection =
      SecretsManager(endpoint = "http://localhost:4584")
    val keyValue: String = secretManagerConnection.getSecretString("Test")
    keyValue should equal("""{"TestKey":"testKeyValue"}""")
  }

  it must "throw ResourceExistsException when trying to save an existing key with the same name" in {
    val secretManagerConnection =
      SecretsManager(endpoint = "http://localhost:4584")
    intercept[ResourceExistsException] {
      val newKey: String = secretManagerConnection.storeNewSecret(
        secretName = "Test",
        description = "Test secret key",
        secretString = """{"TestKey":"testKeyValue"}"""
      )
    }
  }

  it must "delete a valid existing key" in {
    val secretManagerConnection =
      SecretsManager(endpoint = "http://localhost:4584")
    val deletedKey: String = secretManagerConnection.deleteStoredSecret("Test")
    deletedKey should equal("Test")
  }

  it must "throw ResourceNotFoundException when trying to delete an non existing key" in {
    val secretManagerConnection =
      SecretsManager(endpoint = "http://localhost:4584")
    intercept[ResourceNotFoundException] {
      val deletedKey: String =
        secretManagerConnection.deleteStoredSecret("Test")
    }
  }

}
