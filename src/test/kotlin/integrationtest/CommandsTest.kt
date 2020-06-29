package integrationtest

import DUMP_PATH
import FILE_PATH
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith
import unittest.captureConsoleOut
import java.io.File

class CommandsTest : StringSpec() {
    init {
        "CHECK should tell if the word is in the original file" {
            forAll(
                row("impozible", "impozible -> NO? -- consider adding to the database"),
                row("word", "word -> Yes?")
            ) { word, consoleOutput ->

                Commands.CHECK.process
                    .captureConsoleOut(word, FileLoader.loadWords(FILE_PATH)) shouldContain consoleOutput
            }
        }

        "ADD should add a word in lowercase if it is a valid word and is not in the set" {
            val data = FileLoader.loadWords(FILE_PATH)
            val initialSize = data.size

            forAll(
                row("", initialSize),
                row("word", initialSize),
                row("WoRd", initialSize),
                row("word!", initialSize),
                row("two words", initialSize),
                row("IMPOZIBLE", initialSize + 1),
                row("impozible", initialSize + 1)
            ) { wordToAdd, resultSize ->
                val dataCopy = data.map { it }.toMutableSet()
                Commands.ADD.process(wordToAdd, dataCopy)
                dataCopy.size shouldBe resultSize
            }
        }

        "DELETE should remove word if it exists in the set case-insensitive" {
            val data = FileLoader.loadWords(FILE_PATH)
            val initialSize = data.size

            forAll(
                row("", initialSize),
                row("word", initialSize - 1),
                row("WoRd", initialSize - 1),
                row("two word", initialSize),
                row("word!", initialSize),
                row("impozible", initialSize)
            ) { wordToDelete, resultSize ->
                val dataCopy = data.map { it }.toMutableSet()
                Commands.DELETE.process(wordToDelete, dataCopy)
                dataCopy.size shouldBe resultSize
            }
        }

        "DUMP should fetch from urban dictionary API and serialize to file" {
            forAll(
                row(mutableSetOf("adbkasndkaskd")),
                row(mutableSetOf("impossible"))
            ) { word ->
                Commands.DUMP.process("continue", word)
                File(DUMP_PATH).readText().run {
                    this shouldStartWith "[{"
                    this shouldContain word.first()
                    this shouldEndWith "}]"
                }
            }
        }
    }
}