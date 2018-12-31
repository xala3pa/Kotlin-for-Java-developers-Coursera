package nicestring

fun String.isNice(): Boolean {
    return listOf(nonBuBaBe(), atLeastThreeVowel(), doubleLetter()).filter { it }.count() > 1
}

fun String.nonBuBaBe(): Boolean = !this.contains(Regex("bu|ba|be"))
fun String.atLeastThreeVowel(): Boolean = this.filter { it in listOf('a', 'e', 'i', 'o', 'u') }.count() > 2
fun String.doubleLetter(): Boolean {
    val tail = this.drop(1)
    val pair = this.zip(tail)

    return pair.filter { (firstChar, secondChar) -> firstChar == secondChar }.count() > 0
}


