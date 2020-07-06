package extensions

/**
 * Elongates the character to the given length.
 *
 * @param length The desired length.
 */
infix fun Char.elongateTo(length: Int) = this.toString().repeat(length)