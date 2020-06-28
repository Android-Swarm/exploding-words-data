import extensions.elongateTo
import extensions.enclosedBy
import extensions.toUnorderedList
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

fun main() {
    App.run()
}

object App {
    private val scanner = Scanner(System.`in`)
    private val words = FileLoader.loadWords(FILE_PATH)

    fun run() {
        println(WELCOME_MESSAGE enclosedBy '*')

        while (true) {
            println("Enter one of the following commands")
            println(Commands.values().toUnorderedList())
            println("To exit, enter $INPUT_FOR_EXIT")
            print("Your input > ")
            val command = scanner.nextLine().toLowerCase()

            if (command == INPUT_FOR_EXIT) break

            try {
                println('-' elongateTo 50)
                val commandObject = Commands.valueOf(command.toUpperCase())

                while (true) {
                    println(commandObject.inputMessage)
                    val userInput = scanner.nextLine()

                    if (userInput == INPUT_FOR_STOP) break

                    commandObject.process(userInput, words)
                }

                FileLoader.saveWords(FILE_PATH, words)
            } catch (e: IllegalArgumentException) {
                println("The command $command does not exist!")
            }

            println('-' elongateTo 50)
        }

        println(EXIT_MESSAGE)
    }
}