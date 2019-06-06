object FullOfWarnings {
  val testNonExhaustivePatternMatch = {
    sealed trait Foo
    case class Foo1(s: String) extends Foo
    case class Foo2(s: String) extends Foo

    val foo: Foo = Foo2("foo")
    foo match {
      case Foo1(s) => s
    }
  }
  val testUniversalEquality: Boolean = Some("foo") == "foo"

  val testMatchOnErased = {
    trait TraitWithDependantType {
      type T
    }

    val test: TraitWithDependantType = null

    test match {
      case subject: test.T => ???
    }
  }
}
