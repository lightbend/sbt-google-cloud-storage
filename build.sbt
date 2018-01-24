import sbt._
import sbt.Keys._

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

lazy val commonSettings = Seq(
  version in ThisBuild := "0.0.9",
  organization in ThisBuild := "Lightbend Inc"
)

lazy val sbtGCSResolver  =
  Project("sbt-google-cloud-storage", file("."))
    .settings(
      version := "0.0.9",
      organization := "com.lightbend",
      scalaVersion := "2.12.4",

      libraryDependencies ++= Vector(
        "org.apache.ivy" % "ivy" % "2.4.0",
	      "com.google.cloud" % "google-cloud-storage" % "1.7.0"
      ),

      sbtPlugin := true,
      crossSbtVersions := Vector("1.0.2"),

      name := "sbt-google-cloud-storage",
      description := "A SBT resolver and publisher for Google Cloud Storage",

      bintrayOrganization := Some("sbt"),
      bintrayRepository := "sbt-plugin-releases",

      publishMavenStyle := false,

      licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
      publishMavenStyle := false,

      scalacOptions ++= Seq(
        "-encoding", "UTF-8",
        "-Xlog-reflective-calls",
        "-Xlint",
        "-deprecation",
        "-feature",
        "-language:_",
        "-unchecked"
      )
    )
    .settings(
      scalariformSettings(true) ++
      Seq(
        ScalariformKeys.preferences := ScalariformKeys.preferences.value
          .setPreference(AlignParameters, false)
          .setPreference(AlignSingleLineCaseStatements, true)
          .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 90)
          .setPreference(DoubleIndentConstructorArguments, true)
          .setPreference(DoubleIndentMethodDeclaration, true)
          .setPreference(RewriteArrowSymbols, true)
          .setPreference(DanglingCloseParenthesis, Preserve)
          .setPreference(NewlineAtEndOfFile, true)
      )
    )


