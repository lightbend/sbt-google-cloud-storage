package com.lightbend.gcsplugin

import java.io.{ FileInputStream, _ }
import java.util

import com.google.cloud.storage.Bucket._
import com.google.cloud.storage.{ Storage, _ }
import com.lightbend.gcsplugin.AccessRights._
import org.apache.ivy.core.module.descriptor._
import org.apache.ivy.plugins.repository._

import scala.collection.JavaConverters._

case class GCSRepository(bucketName: String, publishPolicy: AccessRights) extends AbstractRepository {
  private val storage: Storage = StorageOptions.getDefaultInstance.getService
  private val bucket = storage.get(bucketName)

  override def getResource(source: String): GCSResource = {
    GCSResource.create(storage, bucketName, source)
  }

  override def get(source: String, destination: File): Unit = {

    val extSource = if (destination.toString.endsWith("sha1"))
      source + ".sha1"
    else if (destination.toString.endsWith("md5"))
      source + ".md5"
    else
      source

    GCSResource.download(storage, GCSResource.create(storage, bucketName, extSource), destination)
  }

  override def list(parent: String): util.List[String] = {
    storage.list(bucketName).getValues.asScala.map(_.getName).toList.asJava
  }

  override def put(artifact: Artifact, source: File, destination: String, overwrite: Boolean): Unit = {

    publishPolicy match {
      case AccessRights.PublicRead ⇒
        bucket.create(
          destination.replace("//", "/"),
          new FileInputStream(source),
          getContentType(artifact.getType),
          BlobWriteOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
        )
      case AccessRights.InheritBucket ⇒
        bucket.create(
          destination.replace("//", "/"),
          new FileInputStream(source),
          getContentType(artifact.getType)
        )
    }
  }

  private def getContentType(ext: String): String = {

    ext.toLowerCase match {
      case "jar"  ⇒ "application/java-archive"
      case "xml"  ⇒ "application/xml"
      case "sha1" ⇒ "text/plain"
      case "md5"  ⇒ "text/plain"
      case "ivy"  ⇒ "application/xml"
      case _      ⇒ "application/octet-stream"
    }
  }
}
