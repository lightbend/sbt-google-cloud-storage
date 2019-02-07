package com.lightbend.gcsplugin

import java.io.{ File, _ }
import java.nio.channels._

import com.google.cloud.storage.{ BlobId, _ }
import org.apache.ivy.plugins.repository._

object GCSResource {
  def create(storage: Storage, bucketName: String, blobName: String): GCSResource = {
    val blob = storage.get(blobIdFromName(bucketName, blobName))
    if (blob == null)
      GCSResource(storage, bucketName, blobName, 0, 0, exists = false)
    else
      GCSResource(storage, bucketName, blobName, blob.getUpdateTime(), blob.getSize, exists = true)
  }

  def openStream(storage: Storage, resource: GCSResource): InputStream = {
    val blob = storage.get(blobIdFromName(resource.bucketName, resource.blobName))
    Channels.newInputStream(blob.reader())
  }

  def download(storage: Storage, resource: GCSResource, file: File): Unit = {
    val blob = storage.get(blobIdFromName(resource.bucketName, resource.blobName))
    blob.downloadTo(file.toPath)
  }

  def blobIdFromName(bucketName: String, name: String): BlobId = {
    BlobId.of(bucketName, name.replace("//", "/"))
  }
}

case class GCSResource(
    storage: Storage,
    bucketName: String,
    blobName: String,
    lastModified: Long,
    contentLength: Long,
    exists: Boolean) extends Resource {

  override def getName: String = blobName

  override def getLastModified: Long = lastModified

  override def getContentLength: Long = contentLength

  override def openStream(): InputStream = {
    GCSResource.openStream(storage, this)
  }

  override def isLocal = false

  override def clone(cloneName: String): Resource = copy()

}
