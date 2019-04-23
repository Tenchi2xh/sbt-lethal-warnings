package sbt

object LethalWarnings {
  sealed abstract class LethalWarning(regex: String) {
    def matches(message: String) = message.matches(s"(?sm)${regex}")
  }

  case object NonExhaustivePatternMatch extends LethalWarning("^match may not be exhaustive.*$")
  case object UniversalEquality extends LethalWarning("^comparing values of types \\S+ and \\S+ using \\S+ will always yield false")
}
