import com.beust.klaxon.*
import extensions.clean
import kotlin.reflect.KProperty

val exclusionList = listOf("thumbs_up", "thumbs_down")

/**
 * This class holds the data of a word.
 *
 * @property word The word.
 * @property definition The meaning of the word.
 * @property example The example sentence for the word.
 * @property thumbs_up The number of likes.
 * @property thumbs_down The number of dislikes.
 */
data class Word(
    val word: String,
    val definition: String?,
    val example: String?,
    val thumbs_up: Int = 0,
    val thumbs_down: Int = 0
) {
    private val deserializeStrategy = object : PropertyStrategy {
        override fun accept(property: KProperty<*>) = property.name !in exclusionList
    }

    /**
     * Returns the data of of the object in JSON format.
     */
    fun toJsonString() = Klaxon().propertyStrategy(deserializeStrategy)
        .toJsonString(Word(word, definition?.clean(), example?.clean()))
}

/**
 * This class holds the raw response from Urban Dictionary API.
 *
 * @property list The list of results.
 */
data class ApiResponse(val list: List<Word>)

