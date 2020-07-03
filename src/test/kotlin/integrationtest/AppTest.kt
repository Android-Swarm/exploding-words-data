package integrationtest

import EXIT_MESSAGE
import INPUT_FOR_EXIT
import INPUT_FOR_STOP
import LINE_BREAK
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class AppTest : StringSpec() {
    private val sysIn = System.`in`
    private val sysOut = System.out

    private val quit = "$INPUT_FOR_EXIT$LINE_BREAK"
    private val stopAndQuit = "$INPUT_FOR_STOP$LINE_BREAK$quit$LINE_BREAK"

    private val dummyInput = quit + quit + quit +
            "check$LINE_BREAK$stopAndQuit" +
            "add$LINE_BREAK$stopAndQuit" +
            "delete$LINE_BREAK$stopAndQuit" +
            "dump\r\r$stopAndQuit" +
            "invalid$LINE_BREAK$stopAndQuit" +
            "check${LINE_BREAK}hi$LINE_BREAK$stopAndQuit" +
            "add${LINE_BREAK}hi$LINE_BREAK$stopAndQuit" +
            "delete${LINE_BREAK}amsdlasldasd$LINE_BREAK$stopAndQuit" +
            "dump${LINE_BREAK}all$LINE_BREAK$stopAndQuit"

    override fun beforeSpec(spec: Spec) {
        System.setIn(ByteArrayInputStream(dummyInput.toByteArray()))
    }

    override fun afterSpec(spec: Spec) {
        System.setIn(sysIn)
        System.setOut(sysOut)
    }

    private fun captureConsoleOutput(): String {
        val dataStream = ByteArrayOutputStream()
        System.setOut(PrintStream(dataStream))

        App.run()

        System.out.flush()

        return dataStream.toString()
    }

    init {
        "The application should display a boxed title message on start" {
            captureConsoleOutput() shouldStartWith
                    "*********************************************\n" +
                    "* Welcome to Exploding Words Data Processor *\n" +
                    "*********************************************"
        }

        "The application should display message on quit" {
            captureConsoleOutput() shouldEndWith "$EXIT_MESSAGE$LINE_BREAK"
        }

        "The application should display the number of words loaded" {
            captureConsoleOutput() shouldContain Regex("""-Loaded \d+ words-""")
        }

        "The application should process commands" {
            Commands.values().forEach {
                captureConsoleOutput() shouldContain it.inputMessage
            }
        }

        "The application handles invalid command" {
            captureConsoleOutput() shouldContain Regex("""The command .* does not exist!""")
        }

        "The application can handle CHECK command" {
            captureConsoleOutput() shouldContain "hi -> Yes"
        }

        "The application can handle ADD command" {
            captureConsoleOutput() shouldContain "ALREADY EXISTS"
        }

        "The application can handle DELETE command" {
            captureConsoleOutput() shouldContain "NOT FOUND"
        }

        "The application can handle DUMP command" {
            captureConsoleOutput() shouldContain "Input cannot be a string!"
        }
    }
}

