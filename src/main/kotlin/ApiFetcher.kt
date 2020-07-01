import com.beust.klaxon.Klaxon
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

    suspend fun convertStringsToWord(words: Set<String>) =
        withContext(Dispatchers.IO) {
            words.map { async { ApiFetcher().getBestResult(it) } }
                .map { it.await() }
                .toSet()
        }
}