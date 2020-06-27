import com.beust.klaxon.Json

data class Word(
    @Json val word: String,
    @Json val definition: String,
    @Json val example: String
)