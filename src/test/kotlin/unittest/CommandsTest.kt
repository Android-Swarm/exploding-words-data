package unittest

import Commands
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CommandsTest : StringSpec() {
    private val originalOut = System.out
    private val dummy = mutableSetOf("word", "words", "testing")

    private fun <T, R, U> ((T, U) -> R).captureConsoleOut(a: T, b: U): String {
        val dataHolder = ByteArrayOutputStream()
        System.setOut(PrintStream(dataHolder))

        this(a, b)

        System.out.flush()
        System.setOut(originalOut)
        return dataHolder.toString()
    }

    init {
        "CHECK should tell if the word is in the set" {
            forAll(
                row("hello", "hello -> NO? -- consider adding to the database"),
                row("word", "word -> Yes?")
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
                row("word!", mutableSetOf("hello"), mutableSetOf("hello")),
                row("word", mutableSetOf("hello"), mutableSetOf("hello"))
            ) { wordToDelete, initial, result ->
                Commands.DELETE.process(wordToDelete, initial)
                initial shouldBe result
            }
        }
    }


}