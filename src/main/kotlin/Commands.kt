import extensions.areLetters
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import kotlin.text.Regex
import java.io.IOException

enum class Commands(
    val inputMessage: String,
    val process: (userInput: String, words: MutableSet<String>) -> Unit
) {
    CHECK("Enter a word to be checked ($INPUT_FOR_STOP to stop): ",
        { requestedWord, wordSet ->
            println(
                "$requestedWord -> ${if (requestedWord in wordSet) "Yes\u2713" else "NO\u2717 -- consider " +
                        "adding to the database"}"
            )
        }
    ),
    ADD("Enter a word to add ($INPUT_FOR_STOP to stop): ",
        { newWord, wordSet ->
            if (!newWord.areLetters()) {
                println(INVALID_WORD_MESSAGE)
            } else {
                if (wordSet.add(newWord.toLowerCase())) {
                    println("$newWord -- ADDED!")
                } else {
                    println("$newWord -- ALREADY EXISTS!")
                }
            }
        }
    ),
    DELETE("Enter a word to delete ($INPUT_FOR_STOP to stop): ",
        { target, wordSet ->
            if (wordSet.remove(target.toLowerCase())) {
                println("$target -- DELETED!")
            } else {
                println("$target -- NOT FOUND!")
            }
        }
    ),

    DUMP(
        "Enter the start (inclusive) and ending index (exclusive) " +
                "to process separated by space ($INPUT_FOR_STOP to cancel, maximum range $MAX_REQUEST): ",
        { input, wordSet ->
            if (!Regex("""^\d+ \d+$""").matches(input)) {
                println(DUMP_INPUT_INVALID_MESSAGE)
            } else {
                val (start, end) = extractNumbers(input)

                when {
                    end < start -> println(DUMP_INVALID_RANGE_MESSAGE)
                    end - start > MAX_REQUEST -> println(DUMP_EXCEED_MAX_MESSAGE)
                    else ->
                        runBlocking {
                            measureTimeMillis {
                                try {
                                    val resultSet = ApiFetcher().convertStringsToWord(
                                        wordSet.toList().slice(start until end),
                                        end - start > 0
                                    )

                                    FileLoader.dumpWords(DUMP_PATH, resultSet)
                                } catch (ioe: IOException) {
                                    println("Encountered exception: ${ioe.message}")
                                }

                            }.run {
                                println("\nOperation done in $this ms")
                            }
                        }
                }
            }
        }
    )
}

private fun extractNumbers(input: String) =
    input.split(" ")
        .map { it.toInt() }
        .run { Pair(this[0], this[1]) }



