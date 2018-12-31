package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val charsInSecret = secret.groupingBy { it }.eachCount().toMutableMap()
    var rightPosition = 0
    var wrongPosition = 0

    guess.forEachIndexed { index, character ->
        if (charInRightPosition(character, secret, index)) {
            rightPosition++
            removeChar(charsInSecret, character)
        }
    }
    guess.forEachIndexed { index, character ->
        if (charInWrongPosition(character,index, secret, charsInSecret)) {
            wrongPosition++
            removeChar(charsInSecret, character)
        }
    }
    return Evaluation(rightPosition, wrongPosition)
}

private fun charInRightPosition(character: Char, secret: String, index: Int) =
        character == secret[index]

private fun charInWrongPosition(c: Char, index: Int, secret: String, charsInSecret: MutableMap<Char, Int>) =
         ((c != secret[index]) && charsInSecret.getOrDefault(c, 0) > 0)

private fun removeChar(charsInSecret: MutableMap<Char, Int>, c: Char) {
    charsInSecret[c] = (charsInSecret[c] ?: 0) - 1
}