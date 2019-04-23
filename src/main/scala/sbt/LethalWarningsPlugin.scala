package sbt

import sbt.Keys.{compile, compilerReporter, maxErrors, printWarnings, streams}
import sbt.LethalWarnings.LethalWarning
import sbt.plugins.JvmPlugin
import xsbti.Severity

object LethalWarningsPlugin extends AutoPlugin {
  override def requires = JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    val lethalWarnings = settingKey[Seq[LethalWarning]]("Warnings to fail on")
  }
  import autoImport._

  override def globalSettings = Seq(
    lethalWarnings := Seq(),
  )

  override def projectSettings = inConfig(Compile)(settings) ++ inConfig(Test)(settings)

  val settings = Seq(
    compilerReporter in compile := new LethalWarningsReporter(
      underlying = (compilerReporter in compile).value,
      lethalWarnings = (lethalWarnings in compile).value,
      logger = (streams.value.log),
    ),
    printWarnings := {
      val reporter = (compilerReporter in compile).value
      val analysis = compile.value.asInstanceOf[sbt.internal.inc.Analysis]
      val problems = analysis.infos.allInfos.values.flatMap(i => i.getReportedProblems ++ i.getUnreportedProblems)
      val maximumErrors = maxErrors.value

      problems.foldLeft(0) { (errorCount, problem) =>
        if (problem.severity != Severity.Error || errorCount < maximumErrors) {
          reporter.log(problem)
          errorCount + (if (problem.severity == Severity.Error) 1 else 0)
        } else {
          errorCount
        }
      }

      reporter.printSummary()
    }
  )
}
