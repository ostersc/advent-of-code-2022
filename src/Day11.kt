import java.util.function.Predicate

data class Throw(val toMonkey: Int, val item: Long)

class Monkey(instructions: List<String>) {
    var items = mutableListOf<Long>()
    var operation: (Long) -> Long = { old -> old }
    var test: Predicate<Long> = Predicate { item -> true }
    var trueTestMonkeyIndex = -1
    var falseTestMonkeyIndex = -1
    var inspectionCount = 0L
    var divisor = 1L

    init {
        check(instructions.size == 5)
        //starting items 0
        items += instructions[0].substringAfter(':').split(',').toMutableList().map { it.trim().toLong() }
        //operation 1
        val opsSplit = instructions[1].substringAfter('=').trim().split(' ')

        operation = { old ->
            val left = if (opsSplit.first() == "old") old else opsSplit.first().trim().toLong()
            val operation = opsSplit[1]
            val right = if (opsSplit.last() == "old") old else opsSplit.last().trim().toLong()
            when (operation) {
                "+" -> left + right
                "*" -> left * right
                else -> throw IllegalStateException("Unsupported operation ${opsSplit[1]}")
            }
        }
        divisor = instructions[2].substringAfterLast(' ').toLong()
        //test 2
        test = Predicate { item ->
            //println("    comparing ${item} with divisor ${divisor}, returning ${item%divisor==0L}")
            item % divisor == 0L
        }

        //  if true 3
        trueTestMonkeyIndex = instructions[3].substringAfterLast(' ').toInt()
        //  if false 4
        falseTestMonkeyIndex = instructions[4].substringAfterLast(' ').toInt()
    }

    fun takeTurn(shouldDivide: Boolean, productOfDivisors: Long = 1L): List<Throw> {
        val throwList = mutableListOf<Throw>()

        // On a single monkey's turn, it inspects and throws
        // all of the items it is holding one  at a time and in the order listed.
        for (i in 0..items.lastIndex) {
            inspectionCount++
            //println("  Monkey inspects an item with a worry level of ${items.first()}.")
            //After each monkey inspects an item
            var newWorry = operation(items.first())
            //println("    Worry level is now $newWorry")
            //before it tests your worry level, your worry level to be
            // divided by three and rounded down to the nearest integer
            if (shouldDivide) {
                newWorry = newWorry / 3
            } else {
                newWorry = newWorry % productOfDivisors
            }
            //println("    Monkey gets bored with item. Worry level is divided by 3 to $newWorry.")
            var toMonkey = falseTestMonkeyIndex
            if (test.test(newWorry)) {
                toMonkey = trueTestMonkeyIndex
            }

            items.removeFirst()
            //println("    Item with worry level $newWorry is thrown to monkey $toMonkey.")
            throwList.add(Throw(toMonkey, newWorry))
        }
        return throwList
    }
}


fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()
        input.windowed(7, 7, true) {
            monkeys += Monkey(it.subList(1, 6))
        }

        for (round in 1..20) {
            for (m in monkeys) {
                //println("Monkey ")
                m.takeTurn(true).forEach { t -> monkeys[t.toMonkey].items.add(t.item) }
                //println()
            }
        }
        //sort by inspection count take(2).multiply
        return monkeys.sortedByDescending { it.inspectionCount }.let { it[0].inspectionCount * it[1].inspectionCount }
    }

    fun part2(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()
        input.windowed(7, 7, true) {
            monkeys += Monkey(it.subList(1, 6))
        }
        val productOfDivisors = monkeys.fold(1L) { result, m -> result * m.divisor }

        for (round in 1..10_000) {
            for (m in monkeys) {
                //println("Monkey ")
                m.takeTurn(false, productOfDivisors).forEach { t -> monkeys[t.toMonkey].items.add(t.item) }
                //println()
                //println("Monkey inspected ${m.inspectionCount} times")
            }
        }
        //sort by inspection count take(2).multiply
        return monkeys.sortedByDescending { it.inspectionCount }.let { it[0].inspectionCount * it[1].inspectionCount }
    }

    val testInput = readInput("Day11_test")
    var testResult = part1(testInput)
    check(testResult == 10605L)
    testResult = part2(testInput)
    check(testResult == 2713310158)

    val input = readInput("Day11")

    val part1 = part1(input)
    println(part1)
    check(part1 == 50172L)

    val part2 = part2(input)
    println(part2)
    check(part2 == 11614682178)
}