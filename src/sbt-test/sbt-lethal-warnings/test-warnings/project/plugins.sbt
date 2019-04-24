{
  val pluginVersion = System.getProperty("plugin.version")
  if (pluginVersion == null) {
    throw new RuntimeException("The system property 'plugin.version' is not defined.\nSpecify this property using the scriptedLaunchOpts -D.")
  }
  else {
    addSbtPlugin("net.team2xh" % "sbt-lethal-warnings" % pluginVersion)
  }
}

addSbtPlugin("com.github.daniel-shuy" % "sbt-scripted-scalatest" % "1.1.1")
