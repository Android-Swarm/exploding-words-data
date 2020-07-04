package unittest

import Commands
import DUMP_PATH
import DUMP_EXCEED_MAX_MESSAGE
import DUMP_INPUT_INVALID_MESSAGE
import DUMP_INVALID_RANGE_MESSAGE
import FileLoader
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream

private val originalOut = System.out

fun <T, R, U> ((T, U) -> R).captureConsoleOut(a: T, b: U): String {
    val dataHolder = ByteArrayOutputStream()
    System.setOut(PrintStream(dataHolder))

    this(a, b)

    System.out.flush()
    System.setOut(originalOut)
    return dataHolder.toString()
}

class CommandsTest : StringSpec() {

    private val dummy = mutableSetOf("word", "words", "testing")

    override fun beforeTest(testCase: TestCase) {
        mockkObject(FileLoader)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {
        "CHECK should tell if the word is in the set" {
            forAll(
                row("hello", "hello -> NO"),
                row("word", "word -> Yes")
            ) { word, consoleOutput ->

                Commands.CHECK.process
                    .captureConsoleOut(word, dummy) shouldContain consoleOutput
            }
        }

        "ADD should add a word in lowercase if it is a valid word and is not in the set" {
            forAll(
                row("", mutableSetOf(), mutableSetOf()),
                row("word", mutableSetOf(), mutableSetOf("word")),
                row("WoRd", mutableSetOf(), mutableSetOf("word")),
                row("word!", mutableSetOf(), mutableSetOf()),
                row("two words", mutableSetOf(), mutableSetOf()),
                row("word", mutableSetOf("word"), mutableSetOf("word")),
                row("WoRd", mutableSetOf("word"), mutableSetOf("word"))
            ) { wordToAdd, initial, result ->
                Commands.ADD.process(wordToAdd, initial)
                initial shouldBe result
            }
        }

        "DELETE should remove word if it exists in the set case-insensitive" {
            forAll(
                row("", mutableSetOf("hello"), mutableSetOf("hello")),
                row("word", mutableSetOf("word"), mutableSetOf()),
                row("WoRd", mutableSetOf("word"), mutableSetOf()),
                row("two word", mutableSetOf("word"), mutableSetOf("word")),
                row("word!", mutableSetOf("hello"), mutableSetOf("hello")),
                row("word", mutableSetOf("hello"), mutableSetOf("hello"))
            ) { wordToDelete, initial, result ->
                Commands.DELETE.process(wordToDelete, initial)
                initial shouldBe result
            }
        }

        "DUMP should handle input error" {
            forAll(
                row("hi", DUMP_INPUT_INVALID_MESSAGE),
                row("100 0", DUMP_INVALID_RANGE_MESSAGE),
                row("0 10001", DUMP_EXCEED_MAX_MESSAGE)
            ) { input, partialErrorMessage ->
                Commands.DUMP.process.captureConsoleOut(input, mutableSetOf()) shouldContain partialErrorMessage
            }
        }

        "DUMP should call dumpWords() method" {
            every { FileLoader.dumpWords(DUMP_PATH, any()) } just Runs

            Commands.DUMP.process("0 1", mutableSetOf("hello"))
            verify { FileLoader.dumpWords(DUMP_PATH, any()) }

        }

        "Dump should handle IOException error" {
            every { FileLoader.dumpWords(DUMP_PATH, any()) } throws
                    IOException("Error 429: Too many requests!")

            Commands.DUMP.process
                .captureConsoleOut("0 2", mutableSetOf("hello", "world")) shouldContain "Encountered exception:"

        }
    }
}