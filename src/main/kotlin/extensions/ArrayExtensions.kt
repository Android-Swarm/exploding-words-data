package extensions

/**
 * Returns the items in an array which are displayed in bullet list.
 */
fun <T> Array<T>.toUnorderedList() = this.fold("") { stringAcc, item ->
    "$stringAcc\u2022\t${item.toString().toTitleCase()}\n"
}