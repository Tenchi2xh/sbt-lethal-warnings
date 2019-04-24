import com.github.daniel.shuy.sbt.scripted.scalatest.ScriptedScalaTestSuiteMixin
import org.scalatest.Matchers.{compile => _, _}
import org.scalatest.WordSpec
import sbt.BuiltinCommands.lastLogFile
import sbt.LethalWarnings._

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.8",
  )
  .settings(
    scriptedScalaTestSpec := {
      class WarningsSpec extends WordSpec with ScriptedScalaTestSuiteMixin {
        override val sbtState: State = state.value

        private def warningName(warning: LethalWarning): String = warning.getClass.getSimpleName.stripSuffix("$")

        /**
          * Triggers a `compile` command and asserts that the given warning was escalated to an error.
          * @param warning Warning to test
          */
        private def testWarningEscalation(warning: LethalWarning) = {
          val extracted: Extracted = Project.extract(sbtState)
          val newState = extracted.appendWithoutSession(Seq(lethalWarnings := Seq(warning)), sbtState)

          val run = Project.runTask(compile in Compile, newState)
          val maybeFailedState = run match {
            case Some((state, Inc(_))) => Some(state)
            case _                     => None
          }

          maybeFailedState shouldBe defined
          val failedState = maybeFailedState.get

          // Following two lines obtained through reverse-engineering the built-in command `last` from SBT
          val logFile = lastLogFile(failedState).get
          val lines = IO.readLines(logFile)

          assert(lines.exists(_.contains(s"matches configured pattern `${warningName(warning)}'")))
        }

        "sbt-lethal-warnings" should {
          "not escalate warnings when warnings list is empty" in {
            Project.runTask(compile in Compile, sbtState)
          }
          allWarnings.foreach { warning =>
            s"escalate compiler warnings matching pattern '${warningName(warning)}`" in {
              testWarningEscalation(warning)
            }
          }
        }
      }
      Some(new WarningsSpec)
    }
  )

