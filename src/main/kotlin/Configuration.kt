import javax.security.auth.login.Configuration

private val resourcePath = Configuration::class.java.getResource("/words_alpha.txt")

val FILE_PATH = resourcePath.path as String

const val WELCOME_MESSAGE = "Welcome to Exploding Words Data Processor"
const val EXIT_MESSAGE = "Thank you for using this service!"

const val INPUT_FOR_EXIT = "!exit"