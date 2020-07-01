package integrationtest

import EXIT_MESSAGE
import INPUT_FOR_EXIT
import INPUT_FOR_STOP
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
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
    private val dummyInput = "$INPUT_FOR_EXIT\r\n" +
            "$INPUT_FOR_EXIT\r\n" +
            "$INPUT_FOR_EXIT\r\n" +
            "check\r\n$INPUT_FOR_STOP\r\n$INPUT_FOR_EXIT\n" +
            "add\r\n$INPUT_FOR_STOP\n$INPUT_FOR_EXIT\n" +
            "delete\r\n$INPUT_FOR_STOP\n$INPUT_FOR_EXIT\n" +
            "dump\r\r$INPUT_FOR_STOP\n$INPUT_FOR_EXIT\n" +
            "invalid\r\n$INPUT_FOR_STOP\n$INPUT_FOR_EXIT\n"

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
            captureConsoleOutput() shouldEndWith "$EXIT_MESSAGE\r\n"
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
    }
}

