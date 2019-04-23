package sbt

import sbt.LethalWarnings.LethalWarning
import xsbti.{Position, Problem, Reporter, Severity}

class LethalWarningsReporter(underlying: Reporter, lethalWarnings: Seq[LethalWarning], logger: Logger) extends Reporter {
  var errorEncountered = false

  override def log(problem: Problem): Unit = {
    val maybeWarning = findWarning(problem.message)

    val newProblem = (problem.severity, maybeWarning) match {

      case (Severity.Warn, Some(warning)) =>
        errorEncountered = true
        logger.error(s"Compiler warning matches configured pattern `${warning.getClass.getSimpleName.stripSuffix("$")}', escalating to error:")

        new Problem {
          override def severity: Severity = Severity.Error

          override def category: String = problem.category
          override def message: String = problem.message
          override def position: Position = problem.position
          override def rendered: java.util.Optional[String] = problem.rendered
        }

      case _ => problem
    }

    // By invoking `Reporter.log()` with a Problem whose severity has been modified to Error,
    // builds will automatically fail, because the implementation of Reporters have mutable
    // array yof Problems that `.log()` maintains. However, for the sake of correctness,
    // this Reporter will also keep track of errors using the `errorEncountered` flag,
    // in case the underlying reporter has a pure implementation.
    underlying.log(newProblem)
  }

  private def findWarning(message: String): Option[LethalWarning] = {
    lethalWarnings.find(_.matches(message))
  }

  override def reset(): Unit = {
    errorEncountered = false
    underlying.reset _
  }
  override def hasErrors: Boolean = underlying.hasErrors || errorEncountered

  override def hasWarnings: Boolean = underlying.hasWarnings
  override def printSummary(): Unit = underlying.printSummary _
  override def problems: Array[Problem] = underlying.problems
  override def comment(pos: Position, msg: String): Unit = underlying.comment _

}
