package unittest

import ApiFetcher
import Word
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiFetcherTest : StringSpec() {
    private val good = Word("good", "def", "ex", 10, 0)
    private val great = Word("great", "def", "ex", 20, 0)
    private val best = Word("best", "def", "ex", 30, 0)
    private val notFoundWord = Word("word", null, null)

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {
        "getBestResult() should call fetchFromApi()" {
            val spy = spyk<ApiFetcher>(recordPrivateCalls = true)

            every { spy["fetchFromApi"](any<String>()) } returns
                    listOf(Word("word", "def", "example"))

            spy.getBestResult("word")

            verify { spy["fetchFromApi"]("word") }
        }

        "getBestResult() should return the object with the highest gap between likes and dislikes" {
            val spy = spyk<ApiFetcher>(recordPrivateCalls = true)

            forAll(
                row(listOf(), notFoundWord),
                row(listOf(good), good),
                row(listOf(great, good), great),
                row(listOf(best, good, great), best)
            ) { mockReturn, output ->
                every { spy["fetchFromApi"](any<String>()) } returns mockReturn
                spy.getBestResult("word") shouldBe output
            }
        }

        "convertStringToWord() should run in Dispatchers.IO" {
            mockkStatic("kotlinx.coroutines.BuildersKt__Builders_commonKt")
            val spy = spyk<ApiFetcher>()

            coEvery { withContext<Set<Word>>(Dispatchers.IO, capture(CapturingSlot())) } answers {
                setOf(Word("mock", "a mock", "mock with mockk"))
            }

            spy.convertStringsToWord(setOf("mock"))

            coVerify { withContext<Set<Word>>(Dispatchers.IO, block = any()) }

            clearAllMocks()
        }
    }
}