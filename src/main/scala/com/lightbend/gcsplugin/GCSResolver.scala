package com.lightbend.gcsplugin

import org.apache.ivy.plugins.resolver._
import sbt.librarymanagement._

import scala.language.implicitConversions

object AccessRights extends Enumeration {
  type AccessRights = Value
  val PublicRead, InheritBucket = Value
}

import com.lightbend.gcsplugin.AccessRights._

object GCSResolver extends Implicits {
  def forBucket(bucketName: String): GCSResolver = {
    new GCSResolver("Google Cloud Storage Resolver", bucketName, publishPolicy = AccessRights.InheritBucket)
  }
}

object GCSPublisher extends Implicits {
  def forBucket(bucketName: String, publishPolicy: AccessRights): GCSResolver = {
    new GCSResolver("Google Cloud Storage Publisher", bucketName, publishPolicy)
  }
}

trait Implicits {
  implicit def toSbtResolver(resolver: GCSResolver): sbt.Resolver = {
    new sbt.RawRepository(resolver, resolver.getName)
  }
}

class GCSResolver(name: String, bucketName: String, publishPolicy: AccessRights) extends RepositoryResolver {
  setName(name)
  setRepository(GCSRepository(bucketName, publishPolicy))
  setM2compatible(false)
  Resolver.ivyStylePatterns.ivyPatterns.foreach { p ⇒ this.addIvyPattern(p) }
  Resolver.ivyStylePatterns.artifactPatterns.foreach { p ⇒ this.addArtifactPattern(p) }
}
