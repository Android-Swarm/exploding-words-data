package integrationtest

import ApiFetcher
import Word
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ApiFetcherTest : StringSpec() {

    private val Word.score
        get() = thumbs_up - thumbs_down

    init {
        "getBestResult() should fetch the internet for the (hopefully) best result" {
            ApiFetcher().getBestResult("hello").word shouldBe "hello"
        }

        "getBestResult() should fetch a gap between thumbs up and thumbs down of minimal zero" {
            ApiFetcher().getBestResult("hello").score shouldBeGreaterThanOrEqual 0
        }

        "convertStringToWord() should convert set of strings to set of words" {
            forAll(
                row(listOf(), listOf()),
                row(listOf("hello"), listOf("hello")),
                row(listOf("backs", "zkczljsdkajsdkajsdk"), listOf("backs", "zkczljsdkajsdkajsdk"))
            ) { input, output ->
                runBlocking {
                    ApiFetcher().convertStringsToWord(input).map { it.word } shouldBe output
                }
            }
        }

        "convertStringToWord() should display log if enabled" {
            val sysOut = System.out
            val outputHolder = ByteArrayOutputStream()
            System.setOut(PrintStream(outputHolder))

            runBlocking {
                ApiFetcher().convertStringsToWord(listOf("hello", "neighbour", "hi"), true)
            }

            System.out.flush()
            System.setOut(sysOut)

            outputHolder.toString() shouldContain "(3 / 3)"
        }
    }
}