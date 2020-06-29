package extensions

/** Returns `true` if the every character of the [String] is a letter */
fun String.areLetters() = this.isNotEmpty() && this.all { it.isLetter() }

/**
 * Converts every first character of a word into uppercase, and the rest into lowercase.
 *
 * @param delimiter The character that separates the word
 */
fun String.toTitleCase(delimiter: String = " ") =
    this.split(delimiter).joinToString(delimiter) { it.toLowerCase().capitalize() }

/**
 * Boxes the string with the decorator character.
 *
 * @param decorator The character to use to box the string.
 * @return The boxed string.
 */
infix fun String.enclosedBy(decorator: Char): String {
    val middle = "$decorator $this $decorator"
    val border = decorator elongateTo middle.length
    return "$border\n$middle\n$border"
}

/** Filters out unwanted special characters received from Urban Dictionary API */
fun String.clean() = this
    .filter {
        it.isLetterOrDigit() || it.isWhitespace() || it !in "[]"
    }
    .replace("\r\n", "")
    .replace("""\"""", "\"")
