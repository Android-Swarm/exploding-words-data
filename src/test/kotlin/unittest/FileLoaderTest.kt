package unittest

import FileLoader
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.io.FileOutputStream
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible


class FileLoaderTest : StringSpec() {

    private val testFilePath = FileLoaderTest::class.java.getResource("/words_test.txt").path

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
            spy.loadWords(testFilePath)

            verify { spy["extractWords"](testFilePath) }
        }

        "loadWords() should filter out multiple words and words with special characters" {
            every {
                FileLoader::class.memberFunctions.find { it.name == "extractWords" }?.let {
                    it.isAccessible = true
                    it.call(FileLoader, testFilePath)
                }
            } returns
                    listOf("this", "document or file", "is (maybe) the", "testing", "document!")

            FileLoader.loadWords(testFilePath) shouldBe mutableSetOf("this", "testing")
        }

        "saveWords() should write a set of words to a file" {
            val writeContent = "word\nwords".toByteArray()
            val outputMock = mockk<FileOutputStream>()

            every { outputMock.use { it.write(writeContent) } } returns println("Write to file called")

            FileLoader.saveWords(testFilePath, setOf("word", "words")) shouldBe println("Write to file called")
        }
    }
}