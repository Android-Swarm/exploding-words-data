import extensions.elongateTo
import kotlin.math.roundToInt

class ProgressFormatter(
    val amount: Int,
    val length: Int = 30,
    val fillChar: Char = '\u2588',
    val spaceChar: Char = '\u2591'
) {
    init {
        require(amount > 0) { "Progress bar should track at least one item" }
        require(length > 0) { "Progress bar must have a non-zero positive length" }
    }

    var done = 0
        private set

    val progress: Float
        get() = done / amount.toFloat()

    fun finishOne() = done++

    fun getFormattedProgress(): String {
        val fillLength = (progress * length).roundToInt()
        val spaceLength = length - fillLength

        return "${fillChar elongateTo fillLength}${spaceChar elongateTo spaceLength} ($done / $amount)"
    }
}