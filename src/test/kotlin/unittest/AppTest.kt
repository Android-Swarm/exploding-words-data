package unittest

import App
import io.kotest.core.Tuple2
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.*
import main

class AppTest : StringSpec() {
    override fun beforeTest(testCase: TestCase) {
        mockkObject(App)
    }

    override fun afterTest(f: suspend (Tuple2<TestCase, TestResult>) -> Unit) {
        clearAllMocks()
    }

    init {
        "App.run() should be called in the main function" {
            every { App.run() } just Runs

            main()

            verify { App.run() }
        }
    }

}