package extensions

infix fun Char.elongateTo(length: Int) = this.toString().repeat(length)