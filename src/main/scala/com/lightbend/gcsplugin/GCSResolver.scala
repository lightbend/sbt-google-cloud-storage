package com.lightbend.gcsplugin

import org.apache.ivy.plugins.resolver._
import sbt.librarymanagement._

object AccessRights extends Enumeration {
  type AccessRigths = Value
  val PublicRead, InheritBucket = Value
}
import com.lightbend.gcsplugin.AccessRights._

object GCSResolver {
  def forBucket(bucketName: String) = {
    new GCSResolver("Google Cloud Storage Resolver", bucketName, publishPolicy = AccessRights.InheritBucket)
  }
}

object GCSPublisher {
  def forBucket(bucketName: String, publishPolicy: AccessRigths) = {
    new GCSResolver("Google Cloud Storage Publisher", bucketName, publishPolicy)
  }
}

class GCSResolver(name: String, bucketName: String, publishPolicy: AccessRigths) extends RepositoryResolver {
  setName(name)
  setRepository(GCSRepository(bucketName, publishPolicy))
  setM2compatible(false)
  Resolver.ivyStylePatterns.ivyPatterns.foreach { p ⇒ this.addIvyPattern(p) }
  Resolver.ivyStylePatterns.artifactPatterns.foreach { p ⇒ this.addArtifactPattern(p) }
}
