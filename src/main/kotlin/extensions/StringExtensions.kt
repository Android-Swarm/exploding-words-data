package extensions

/** Returns `true` if the every character of the [String] is a letter */
fun String.isLetterOnly() = this.all { it.isLetter() }

fun String.toTitleCase() = this.toLowerCase().capitalize()

infix fun String.enclosedBy(decorator: Char): String {
    val middle = "$decorator $this $decorator"
    val border = decorator elongateTo middle.length
    return "$border\n$middle\n$border"
}
