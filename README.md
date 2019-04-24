# sbt-lethal-warnings

[![](https://maven-badges.herokuapp.com/maven-central/net.team2xh/sbt-lethal-warnings/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.team2xh/sbt-lethal-warnings)

Selectively escalate warnings to errors: `-Xfatal-warnings` with a whitelist.

## Usage

Add the following to the `project/plugin.sbt`:

```scala
addSbtPlugin("net.team2xh" % "sbt-lethal-warnings" % "[Version in badge above]")
```

In `build.sbt`, configure which warnings need to be escalated to errors:

```scala
import sbt.LethalWarnings._
lethalWarnings := Seq(/* Desired warnings */)
```

## Available warnings

Key                         | Matching warning
----------------------------|--------------------------------------
`NonExhaustivePatternMatch` | Missing cases in pattern match blocks
`UniversalEquality`         | Comparing objects of different types

To lethalize all warnings supported by the plugin, use the following setting:

```scala
lethalWarnings := allWarnings
```

## Testing

Tests are using [scripted SBT](https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html) and [scripted-scalatest](https://github.com/daniel-shuy/scripted-scalatest-sbt-plugin) to run a nested SBT instance compiling the project located at `src/sbt-test/sbt-lethal-warnings/test-warnings/`. To execute the test suite, run the following from the root of the project:

```bash
sbt scripted
```
