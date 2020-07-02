package unittest

import ProgressFormatter
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import java.lang.IllegalArgumentException

class ProgressFormatterTest : StringSpec() {
    private val fillChar = '\u2588'
    private val spaceChar = '\u2591'

    init {
        "ProgressFormatter should handle invalid amount inputs" {
            forAll(row(0), row(-1)) {
                shouldThrow<IllegalArgumentException> { ProgressFormatter(it) }
            }
        }

        "ProgressFormatter should handle invalid bar length inputs" {
            forAll(row(0), row(-1)) {
                shouldThrow<IllegalArgumentException> { ProgressFormatter(10, length = it) }
            }
        }

        "ProgressFormatter should display the correct bar length" {
            val formatter = ProgressFormatter(30)
            formatter.getFormattedProgress() shouldMatch
                    Regex("^[$fillChar$spaceChar]{30}.*$")
        }

        "finishOne() should increase the progress by one" {
            ProgressFormatter(30).run {
                finishOne()
                done shouldBe 1
            }
        }

        "getFormattedProgress() should return the correct progress" {
            forAll(
                row(0, "^$spaceChar{30} \\(0 \\/ 30\\)$"),
                row(15, "^$fillChar{15}$spaceChar{15} \\(15 \\/ 30\\)$"),
                row(20, "^$fillChar{20}$spaceChar{10} \\(20 \\/ 30\\)$"),
                row(30, "^$fillChar{30} \\(30 \\/ 30\\)$")
            ) { done, pattern ->
                val formatter = ProgressFormatter(30)
                repeat(done) { formatter.finishOne() }
                formatter.getFormattedProgress() shouldMatch Regex(pattern)
            }

        }
    }
}