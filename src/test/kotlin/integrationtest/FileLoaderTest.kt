package integrationtest

import LINE_BREAK
import Word
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import java.io.File

class FileLoaderTest : StringSpec() {

    private fun getResourcePath(name: String) = "src/test/resources/$name"

    init {
        "loadWords() should ignore invalid lines and return a set of valid lines" {
            FileLoader.loadWords(getResourcePath("words_test.txt")) shouldBe mutableSetOf("this", "testing")
        }

        "saveWords() should save a sentence into lines" {
            getResourcePath("temp.txt").run {
                FileLoader.saveWords(this, setOf("this", "is", "a", "test"))
                File(this).readText() shouldBe "this${LINE_BREAK}is${LINE_BREAK}a${LINE_BREAK}test"
                File(this).delete()
            }
        }

        "dumpWords() should save list of Word into json array representation" {
            getResourcePath("temp.txt").run {
                FileLoader.dumpWords(
                    this, setOf(
                        Word("this", "this def", "this ex"),
                        Word("unknown", null, null)
                    )
                )

                File(this).readText() shouldBe
                        "[{\"definition\" : \"this def\", \"example\" : \"this ex\", \"word\" : \"this\"}," +
                        "{\"definition\" : null, \"example\" : null, \"word\" : \"unknown\"}]"

                File(this).delete()
            }
        }
    }
}