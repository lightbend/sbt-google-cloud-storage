package com.lightbend.gcsplugin

import sbt.PluginTrigger

object GCSPlugin extends AutoPlugin {

  trait Keys {
    implicit def toSbtResolver(resolver: GCSResolver): sbt.Resolver = {
      new sbt.RawRepository(resolver, resolver.getName)
    }
  }

  object Keys extends Keys
  object autoImport extends Keys {
    val GCSResolver = com.lightbend.gcsplugin.GCSResolver
    val GCSPublisher = com.lightbend.gcsplugin.GCSPublisher
    val AccessRights = com.lightbend.gcsplugin.AccessRights
  }

  import sbt.Keys._

  override def projectSettings = Seq(
    publishMavenStyle := false
  )

  override def trigger: PluginTrigger = allRequirements
}
