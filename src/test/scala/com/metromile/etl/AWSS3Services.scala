package com.metromile.etl

import java.net.URI
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{ListBucketsRequest}
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest
import org.scalatest.FlatSpec

class AWSS3Services extends FlatSpec {

  "A AWS S3" should "have only one bucket" in {
    val s3: S3Client = S3Client.builder().endpointOverride(URI.create("http://localhost:4572")).build()

    val bucket = "bucket" + System.currentTimeMillis
    println(bucket)

    // Create bucket
    val createBucketRequest = CreateBucketRequest
      .builder()
      .bucket(bucket)
      .createBucketConfiguration(CreateBucketConfiguration.builder()
        .build())
      .build();
    s3.createBucket(createBucketRequest)

    val listBucketsRequest = ListBucketsRequest.builder.build
    val listBucketsResponse = s3.listBuckets(listBucketsRequest)
    assertResult(1)(listBucketsResponse.buckets().size())

    val deleteBucketRequest = DeleteBucketRequest.builder.bucket(bucket).build
    s3.deleteBucket(deleteBucketRequest)
  }

}