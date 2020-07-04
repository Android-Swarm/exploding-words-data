package unittest

import Word
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain

class WordTest : StringSpec() {
    private val wordUnderTest = Word("word", "def", "ex", 50, 10)

    init {
        "Word class should not deserialize thumbs_up and thumbs_down" {
            forAll(row("thumbs_up"), row("thumbs_down")) {
                wordUnderTest.toJsonString() shouldNotContain it
            }
        }

        "Word class should serialize word, definition, and example" {
            forAll(row("word"), row("definition"), row("example")) {
                wordUnderTest.toJsonString() shouldContain it
            }
        }

        "toJsonString() should clean the example and definition received from the API" {
            forAll(
                row("""[hello]""", "hello"),
                row("""hello\r\n""", "hello"),
                row("hello", """[hello]"""),
                row("hello", """hello\r\n"""),
                row("hello", "hello"),
                row("hel lo", "hel lo")
            ) { responseDefinition, responseExample ->

                val result = Word("word", responseDefinition, responseExample)
                    .toJsonString()

                result shouldNotContain "\r\n"
                result shouldNotContain "["
                result shouldNotContain "]"
            }
        }
    }
}