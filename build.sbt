lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-lethal-warnings",
    organization := "net.team2xh",
    version := "0.1.0-SNAPSHOT",
    sbtPlugin := true
  )
