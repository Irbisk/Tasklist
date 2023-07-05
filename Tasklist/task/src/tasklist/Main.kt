package tasklist

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.DateTimeException
import java.time.format.DateTimeParseException

val taskList = mutableListOf<Task>()

fun main() {
    menu()
}

fun menu() {
    while (true) {
        println("Input an action (add, print, end):")
        when (readln()) {
            "add" -> addTasks()
            "print" -> printTasks()
            "end" -> {
                println("Tasklist exiting!")
                break
            }
            else -> println("The input action is invalid")
        }
    }
}

fun addTasks() {
    val list = mutableListOf<String>()
    lateinit var input: String
    lateinit var priority: String
    lateinit var date: String
    lateinit var time: String

    do {
        println("Input the task priority (C, H, N, L):")
    } while (!readln().also { priority = it }.priorityIsCorrect())

    do {
        println("Input the date (yyyy-mm-dd):")
    } while (!readln().also { date = it }.dateIsCorrect())

    do {
        println("Input the time (hh:mm):")
    } while (!readln().also { time = it }.timeIsCorrect())


    println("Input a new task (enter a blank line to end):")

    while (readln().also { input = it }.isNotBlank()) {
        list.add(input.trim())
    }
    if (list.isEmpty()) println("The task is blank")
    else taskList.add(Task(list, priority, "${date.getCorrectDate()}T${time.getCorrectTime()}".toLocalDateTime()))
}




fun printTasks() {
    if (taskList.isEmpty()) {
        println("No tasks have been input")
    } else {
        taskList.forEachIndexed { indexMain, task -> task.list.forEachIndexed { index, s ->
            val number = (indexMain + 1).toString().padEnd(3, ' ')
            val space = "".padEnd(3, ' ')

            val time = "${task.timeDate.hour}:${task.timeDate.minute}".getCorrectTime()
            println(if (index == 0) "$number${task.timeDate.date} $time ${task.priority.uppercase()}\n" +
                   "$space$s" else "$space$s")
            if (index == task.list.lastIndex) println()
        } }
    }
}

class Task(var list: MutableList<String>, var priority: String, var timeDate: LocalDateTime)


fun String.dateIsCorrect(): Boolean {
    var correct = this.matches("[0-9]{4}-(0?[1-9]|1[012])-(0?[1-9]|1[0-9]|2[0-9]|3[01])".toRegex())
    try {
        LocalDate.parse(this.getCorrectDate())
    } catch (e: Exception) {
        correct = false
    }
    if (!correct) {
        println("The input date is invalid")
    }
    return correct
}

fun String.timeIsCorrect(): Boolean {
    val correct = this.matches("([01]?[0-9]|2[0-3]):((0?[0-9])|([1-5][0-9]))".toRegex())
    if (!correct) {
        println("The input time is invalid")
    }
    return correct
}

fun String.getCorrectDate(): String {
    val date = this.split("-")
    val month = if (date[1].length == 1) { "0${date[1]}" } else date[1]
    val day = if (date[2].length == 1) { "0${date[2]}" } else date[2]
    return "${date[0]}-$month-$day"
}

fun String.getCorrectTime(): String {
    val time = this.split(":")
    val hours = if (time[0].length == 1) { "0${time[0]}" } else time[0]
    val min = if (time[1].length == 1) { "0${time[1]}" } else time[1]
    return "$hours:$min"
}

fun String.priorityIsCorrect(): Boolean {
    return this.lowercase().matches("[chnl]".toRegex())
}