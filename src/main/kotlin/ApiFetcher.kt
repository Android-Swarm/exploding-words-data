import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.net.URL

class ApiFetcher {
    private val endpoint = "http://api.urbandictionary.com/v0/define"

    private fun fetchFromApi(word: String) =
        (Klaxon().parse<ApiResponse>(URL("$endpoint?term=$word").readText()) as ApiResponse).list

    fun getBestResult(word: String) =
        fetchFromApi(word).filter { it.word == word }
            .maxBy { it.thumbs_up - it.thumbs_down }
            ?: Word(word, null, null)

    suspend fun convertStringsToWord(words: Set<String>, isLogged: Boolean = false) =
        withContext(Dispatchers.IO) {
            val progressFormatter = if (isLogged) ProgressFormatter(words.size) else null

            words.map { async { ApiFetcher().getBestResult(it) } }
                .map { if (isLogged) it.awaitAndLog(progressFormatter) else it.await() }
                .toSet()
        }
}

private suspend fun Deferred<Word>.awaitAndLog(formatter: ProgressFormatter?): Word {
    val finished = this.await()

    formatter?.let {
        it.finishOne()
        print("\r${formatter.getFormattedProgress()} | Last finished the word ${finished.word}.")
    }

    return finished
}