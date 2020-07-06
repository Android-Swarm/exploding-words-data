import extensions.elongateTo
import kotlin.math.roundToInt

/**
 * This class formats the progress of a number of tasks.
 *
 * @property amount The amount of task to finish.
 * @property length The length of the progress bar. By default is 30 characters.
 * @property fillChar The character that represents completed task.
 * @property spaceChar The character that represents unfinished task.
 */
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

    /**
     * Mark a task as complete.
     */
    fun finishOne() = done++

    /**
     * Returns the progress string of the tasks.
     *
     * @return The formatted progress string.
     */
    fun getFormattedProgress(): String {
        val fillLength = (progress * length).roundToInt()
        val spaceLength = length - fillLength

        return "${fillChar elongateTo fillLength}${spaceChar elongateTo spaceLength} ($done / $amount)"
    }
}