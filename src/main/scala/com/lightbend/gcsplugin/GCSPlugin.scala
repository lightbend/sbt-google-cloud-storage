package com.lightbend.gcsplugin

import sbt.{AutoPlugin, PluginTrigger, Setting}

object GCSPlugin extends AutoPlugin {

  object autoImport {
    val GCSResolver = com.lightbend.gcsplugin.GCSResolver
    val GCSPublisher = com.lightbend.gcsplugin.GCSPublisher
    val AccessRights = com.lightbend.gcsplugin.AccessRights
  }

  import sbt.Keys._

  override def projectSettings: Seq[Setting[_]] = Seq(
    publishMavenStyle := false
  )

  override def trigger: PluginTrigger = allRequirements
}
