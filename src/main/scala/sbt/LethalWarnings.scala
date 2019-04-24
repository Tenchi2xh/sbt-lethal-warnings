package sbt

import scala.reflect.internal.Symbols

object LethalWarnings {

  sealed abstract class LethalWarning(regex: String) {
    def matches(message: String) = message.matches(s"(?sm)${regex}")
  }

  case object NonExhaustivePatternMatch extends LethalWarning("^match may not be exhaustive.*$")
  case object UniversalEquality extends LethalWarning("^comparing values of types \\S+ and \\S+ using \\S+ will always yield false")

  val allWarnings = {
    import scala.reflect.runtime._
    import scala.reflect.runtime.universe._

    val symbol = typeOf[LethalWarning].typeSymbol
    val internal = symbol.asInstanceOf[Symbols#Symbol]
    val descendants = internal.sealedDescendants.map(_.asInstanceOf[Symbol]) - symbol

    descendants
      .map(symbol => symbol.owner.typeSignature.member(symbol.name.toTermName))
      .map(module => currentMirror.reflectModule(module.asModule).instance)
      .map(_.asInstanceOf[LethalWarning])
      .toSeq
  }

}
