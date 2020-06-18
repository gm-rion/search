package search

import java.io.File
import java.util.*

private lateinit var arr: MutableList<String>
private lateinit var indexingMap: MutableMap<String, MutableList<Int>>

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    // add data about people
    addDataAboutPeople(args[1])

    // print menu and check input
    checkInputs(scanner)

}

fun checkInputs(scanner: Scanner) {

    do {
        // menu
        println("\n=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")

        val item = scanner.nextInt()

        when (item) {
            1 -> findAPerson(scanner)
            2 -> printAllPeople()
            0 -> println("\nBye!")
            else -> println("\nIncorrect option! Try again.")
        }
    } while (item != 0)
}

fun printAllPeople() {
    println("\n=== List of people ===")
    arr.forEach { println(it) }
}

fun findAPerson(scanner: Scanner) {

    println("\nSelect a matching strategy: ALL, ANY, NONE")
    scanner.nextLine()
    val searchAlgoritm = scanner.nextLine().trim()

    var arrOfIndex = getIndexToPrint(scanner, searchAlgoritm)

    if (arrOfIndex != null) {
        if (arrOfIndex.size == 0) {
            println("\nNot found.")
        } else {
            println("\n${arrOfIndex.size} persons found:")
            for (r in arrOfIndex) {
                println(arr[r])
            }
        }

    } else {
        println("\nIncorrect searching strategy! Try again.")
    }

}

fun getIndexToPrint(scanner: Scanner, searchAlgoritm: String): IntArray? {
    println("\nEnter a name or email to search all suitable people.")

    val search = scanner.nextLine().toLowerCase()

    val listOfword = search.split(" ") // лист слов в строке
    var listOfOneIndex: MutableList<Int>? // лист для одного элемента

    if (searchAlgoritm == "ALL") {
        // То что необходимо выводить
        val numberOfIndex = mutableMapOf<Int, Int>()

        for (word in listOfword) {
            listOfOneIndex = indexOfOneWord(word)
            if (listOfOneIndex != null) {

                for (i in listOfOneIndex) { // loop for index of single word
                    if (numberOfIndex.containsKey(i)) {
                        var y = numberOfIndex.get(i) ?: 0
                        numberOfIndex.put(i, ++y)
                    } else {
                        numberOfIndex.put(i, 1)
                    }
                }
            }
        }

        return numberOfIndex.filter { it.value >= listOfword.size }.keys.toIntArray()

    } else if (searchAlgoritm == "ANY") {

        val setOfIndex: MutableSet<Int> = mutableSetOf()
        for (word in listOfword) {
            listOfOneIndex = indexOfOneWord(word)
            if (listOfOneIndex != null) {
                for (i in listOfOneIndex) {
                    setOfIndex.add(i)
                }
            }
        }

        return setOfIndex.toIntArray()

    } else if (searchAlgoritm == "NONE") {
        val listOfAllIndex = mutableListOf<Int>()
        for (i in 0..arr.lastIndex) {
            listOfAllIndex.add(i)
        }

        for (word in listOfword) {
            listOfOneIndex = indexOfOneWord(word)
            if (listOfOneIndex != null) {
                listOfAllIndex.removeAll(listOfOneIndex)
            }
        }
        return listOfAllIndex.toIntArray()
    } else {
        return null
    }

}

fun indexOfOneWord(search: String): MutableList<Int>? {
    if (indexingMap.containsKey(search)) {
        return indexingMap.get(search)
    } else {
        return null
    }
}

fun addDataAboutPeople(fullN: String) {

    arr = mutableListOf<String>()
    indexingMap = mutableMapOf<String, MutableList<Int>>()
    val fileName = fullN
    val lines = File(fileName).readLines()
    var countLine = 0
    for (line in lines) {
        arr.add(line)
        val splitArr = line.split(" ")

        for (item in splitArr) {
            val st = item.trim().toLowerCase()
            if (indexingMap.containsKey(st)) {
                indexingMap[st]!!.add(countLine)
            } else {
                //add index in lower case
                indexingMap.put(st, mutableListOf<Int>(countLine))
            }
        }
        countLine++
    }
}
