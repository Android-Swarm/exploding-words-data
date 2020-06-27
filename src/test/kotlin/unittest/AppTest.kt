package unittest

import App
import FILE_PATH
import FileLoader
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.mockkObject
import io.mockk.verify

class AppTest : StringSpec() {

    override fun beforeTest(testCase: TestCase) {
        mockkObject(FileLoader)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {
        "App should load words by calling FileLoader.loadWords(FILE_PATH)" {
            App
            verify { FileLoader.loadWords(FILE_PATH) }
        }
    }
}