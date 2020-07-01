package integrationtest

import ApiFetcher
import Word
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

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
                row(setOf(), listOf()),
                row(setOf("hello"), listOf("hello")),
                row(setOf("backs", "zkczljsdkajsdkajsdk"), listOf("backs", "zkczljsdkajsdkajsdk"))
            ) { input, output ->
                runBlocking {
                    ApiFetcher().convertStringsToWord(input).map { it.word } shouldBe output
                }
            }
        }
    }
}