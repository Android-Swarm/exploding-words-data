import extensions.areLetters
import java.io.File

/** This class handles interactions with the text file. */
object FileLoader {

    /**
     * Reads a text file located in the [path] and load each line into a [List].
     *
     * @param path The file location.
     * @return List of file's content.
     */
    private fun extractWords(path: String): List<String> = File(path)
        .readText()
        .split("\r\n")

    /**
     * Read from a file of words separated in line
     * and takes words which consist entirely of alphabets and is a single word.
     *
     * @param path The file path to read from.
     * @return The eligible words.
     */
    fun loadWords(path: String): MutableSet<String> = extractWords(path)
        .filter { it.areLetters() }
        .toMutableSet()

    /**
     * Serializes a list of string to the text file in the specified [path].
     *
     * @param path The target file path.
     * @param words The list of [String] to serialize.
     */
    fun saveWords(path: String, words: Set<String>) = File(path)
        .writeText(words.joinToString("\r\n"))

    /**
     * Serializes the [Word]'s word, definition, and meaning into a JSON array representation.
     *
     * @param path The destination file path.
     * @param words The list of [Word] to serialize
     */
    fun dumpWords(path: String, words: Set<Word>) =
        File(path).writeText("[${words.joinToString(separator = ",") { it.toJsonString() }}]")
}
