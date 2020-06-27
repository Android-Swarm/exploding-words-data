import java.util.*

val scanner = Scanner(System.`in`)

enum class ProcessEnum(val process: (words: MutableSet<String>) -> String) {
    CHECK({
        while (true) {
            print("Enter a word to be checked (!stop to stop): ")

            val requestedWord = scanner.next().toLowerCase()

            if (requestedWord == "!stop") {
                break
            } else {
                println("$requestedWord -> ${if (requestedWord in it) "yes" else "NO"}")
            }
        }

        "Stopped searching for word"
    })
}