import com.beust.klaxon.*
import extensions.clean
import kotlin.reflect.KProperty

val exclusionList = listOf("thumbs_up", "thumbs_down")

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

    fun toJsonString() = Klaxon().propertyStrategy(deserializeStrategy)
        .toJsonString(Word(word, definition?.clean(), example?.clean()))
}

data class ApiResponse(val list: List<Word>)

