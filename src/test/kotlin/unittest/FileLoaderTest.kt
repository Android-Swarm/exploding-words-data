package unittest

import FileLoader
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.*


class FileLoaderTest : StringSpec() {

    override fun beforeTest(testCase: TestCase) {
        mockkObject(FileLoader)
        mockkStatic("kotlin.io.FilesKt__UtilsKt")
        mockkStatic("java.io.OutputStream")
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {
        "Canary test should pass" {
            true shouldBe true
        }

        "loadWords() should call extractWords() method" {

            val spy = spyk<FileLoader>(recordPrivateCalls = true)

            every { spy["extractWords"](any<String>()) } returns
                    listOf<String>()
            spy.loadWords("")

            verify { spy["extractWords"]("") }
        }

        "loadWords() should filter out multiple words and words with special characters" {
            val spy = spyk<FileLoader>(recordPrivateCalls = true)
            every { spy["extractWords"](any<String>()) } returns
                    listOf("this", "document or file", "is (maybe) the", "testing", "document!")

            spy.loadWords("") shouldBe mutableSetOf("this", "testing")
        }
    }
}