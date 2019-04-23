import xerial.sbt.Sonatype._
import ReleaseTransformations._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-lethal-warnings",
    organization := "net.team2xh",
    sbtPlugin := true,
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint"),
    scalaVersion := "2.12.8",
  )
  .settings(publishingSettings)

lazy val publishingSettings = Seq(
  useGpg := true,
  publishTo := sonatypePublishTo.value,
  publishMavenStyle := true,
  sonatypeProfileName := "net.team2xh",
  sonatypeProjectHosting := Some(GitHubHosting(user="Tenchi2xh", repository="sbt-lethal-warnings", email="tenchi@team2xh.net")),
  developers := List(
    Developer(id = "tenchi", name = "Hamza Haiken", email = "tenchi@team2xh.net", url = url("http://tenchi.me"))
  ),
  licenses := Seq("MIT" -> url("https://github.com/Tenchi2xh/sbt-lethal-warnings/blob/master/LICENSE")),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    setNextVersion,
    commitNextVersion,
    releaseStepCommandAndRemaining("+sonatypeRelease"),
    pushChanges,
  ),
)
