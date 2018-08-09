package com.lightbend.gcsplugin

import java.io._
import java.io.File
import java.nio.channels._

import com.google.cloud.storage._
import com.google.cloud.storage.BlobId
import org.apache.ivy.plugins.repository._

object GCSResource {
  def create(storage: Storage, bucketName: String, blobName: String): GCSResource = {
    val blobId = BlobId.of(bucketName, blobName.replace("//", "/"))
    val blob = storage.get(blobId)
    if (blob == null)
      GCSResource(storage, bucketName, blobName, 0, 0, exists = false)
    else
      GCSResource(storage, bucketName, blobName, blob.getUpdateTime(), blob.getSize, exists = true)
  }
  def openStream(storage: Storage, resource: GCSResource): InputStream = {
    val blobId = BlobId.of(resource.bucketName, resource.blobName.replace("//", "/"))
    val blob = storage.get(blobId)
    Channels.newInputStream(blob.reader())
  }
  def toFile(storage: Storage, resource: GCSResource, file: File): Unit = {
    val blobId = BlobId.of(resource.bucketName, resource.blobName.replace("//", "/"))
    val blob = storage.get(blobId)
    blob.downloadTo(file.toPath)
  }
}

case class GCSResource(storage: Storage, bucketName: String, blobName: String, lastModified: Long, contentLength: Long, exists: Boolean) extends Resource {

  override def getName: String = blobName
  override def getLastModified: Long = lastModified
  override def getContentLength: Long = contentLength

  override def openStream(): InputStream = {
    GCSResource.openStream(storage, this)
  }
  override def isLocal = false
  override def clone(cloneName: String): Resource = copy()

}
