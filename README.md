# sbt-lethal-warnings

[![](https://maven-badges.herokuapp.com/maven-central/net.team2xh/sbt-lethal-warnings_2.12_1.0/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.team2xh/sbt-lethal-warnings_2.12_1.0)

Selectively escalate warnings to errors: `-Xfatal-warnings` with a whitelist.

## Usage

Add the following to the `project/plugin.sbt`:

```scala
addSbtPlugin("net.team2xh" % "sbt-lethal-warnings" % "0.1.0")
```

In `build.sbt`, configure which warnings need to be escalated to errors:

```scala
import sbt.LethalWarnings._
lethalWarnings := Seq(<DesiredWarnings>)
```

## Available warnings

Key                         | Matching warning
----------------------------|--------------------------------------
`NonExhaustivePatternMatch` | Missing cases in pattern match blocks
`UniversalEquality`         | Comparing objects of different types