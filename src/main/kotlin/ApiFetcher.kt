import com.beust.klaxon.Klaxon
import kotlinx.coroutines.*
import java.net.URL

/**
 * This class is used to communicate with the Urban Dictionary API and parse the returned response.
 */
class ApiFetcher {
    private val endpoint = "http://api.urbandictionary.com/v0/define"

    /**
     * Fetches from Urban Dictionary API and parse the response list to a `list` of [Word] objects.
     *
     * @param word the term to search for.
     */
    private fun fetchFromApi(word: String) =
        (Klaxon().parse<ApiResponse>(URL("$endpoint?term=$word").readText()) as ApiResponse).list

    /**
     * Return the best result from the response list returned by the Urban Dictionary API.
     * The best result is determined by the highest difference between the `thumbs_up` and `thumbs_down` field.
     *
     * @param word the term to search for.
     */
    fun getBestResult(word: String) =
        fetchFromApi(word).filter { it.word == word }
            .maxBy { it.thumbs_up - it.thumbs_down }
            ?: Word(word, null, null)

    /**
     * Returns a list of `Word` object which contains the best result from the Urban Dictionary API.
     *
     * @param words the list of terms to search for.
     * @param isLogged `true` if the progress should be printed.
     */
    suspend fun convertStringsToWord(words: List<String>, isLogged: Boolean = false) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            val progressFormatter = if (isLogged) ProgressFormatter(words.size) else null

            words.map { async { ApiFetcher().getBestResult(it) } }
                .map { it.awaitAndLog(progressFormatter) }
                .toSet()
        }
}

/**
 * This extension function logs the progress when the [async] has completed its work.
 *
 * @param formatter The [ProgressFormatter] to use.
 * @return The `Word` object.
 */
private suspend fun Deferred<Word>.awaitAndLog(formatter: ProgressFormatter?): Word {
    val finished = this.await()

    formatter?.let {
        it.finishOne()
        print("\r${it.getFormattedProgress()} | Last finished the word ${finished.word}.")
    }

    return finished
}