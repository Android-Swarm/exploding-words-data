package extensions

fun <T> Array<T>.toUnorderedList() = this.fold("") { stringAcc, item ->
    "$stringAcc\u2022\t${item.toString().toTitleCase()}\n"
}