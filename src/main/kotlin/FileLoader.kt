import extensions.areLetters
import java.io.File

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
     * Serializes a list of words to the text file in the specified [path].
     *
     * @param path The target file path.
     * @param words The list of words to serialize.
     */
    fun saveWords(path: String, words: Set<String>) = File(path)
        .writeText(words.joinToString("\r\n"))

    fun dumpWords(path: String, words: Set<Word>) =
        File(path).writeText("[${words.joinToString(separator = ",") { it.toJsonString() }}]")
}
