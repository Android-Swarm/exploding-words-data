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

    DUMP("Enter the amount of words to process ($INPUT_FOR_STOP to cancel, maximum $MAX_REQUEST): ",
        { input, wordSet ->
            when {
                input.contains(Regex("""\D+""")) -> println(DUMP_INPUT_IS_STRING_MESSAGE)
                input.toInt() > 10_000 -> println(DUMP_EXCEED_MAX_MESSAGE)
                else -> runBlocking {
                    measureTimeMillis {
                        val dumpCount = input.toInt()

                        try {
                            FileLoader.dumpWords(
                                DUMP_PATH,
                                ApiFetcher().convertStringsToWord(wordSet.take(dumpCount).toSet(), dumpCount > 0)
                            )
                        } catch (ioe: IOException) {
                            println("Encountered exception: ${ioe.message}")
                        }

                    }.run {
                        println("\nOperation done in $this ms")
                    }
                }
            }
        }
    )
}


