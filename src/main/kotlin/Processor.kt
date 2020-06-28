import extensions.areLetters

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
    ADD("Enter a word to add ($INPUT_FOR_STOP to stop):",
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
    DELETE("Enter a word to delete ($INPUT_FOR_STOP to stop):",
        { target, wordSet ->
            if (wordSet.remove(target.toLowerCase())) {
                println("$target -- DELETED!")
            } else {
                println("$target -- NOT FOUND!")
            }
        }
    )
}