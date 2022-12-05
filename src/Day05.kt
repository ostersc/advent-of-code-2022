enum class States { READING_CRATES, READY_FOR_INSTRUCTIONS, READING_INSTRUCTIONS }

fun main() {
    fun processInput(input: List<String>, usePopForMove: Boolean): String {
        val stacks = arrayListOf<ArrayDeque<Char>>()
        var state = States.READING_CRATES

        for (line in input) {
            if (state == States.READING_CRATES && (line.startsWith("[") || line.startsWith("  "))) {
                line.toCharArray().withIndex().chunked(4) { it ->
                    val index = it[0].index / 4
                    val c = it[1].value
                    if (c != ' ') {
                        while (index >= stacks.size) {
                            stacks.add(ArrayDeque<Char>())
                        }
                        stacks[index].addFirst(c)
                    }
                }
            } else if (line.isBlank() && state == States.READY_FOR_INSTRUCTIONS) {
                state = States.READING_INSTRUCTIONS
            } else if (line.startsWith("move") && state == States.READING_INSTRUCTIONS) {
                val (count, from, to) = """move (\d+) from (\d+) to (\d+)""".toRegex()
                    .find(line)!!.destructured.toList().map { it.toInt() - 1 }
                for (i in count downTo 0) {
                    if (usePopForMove) {
                        stacks[to].addLast(stacks[from].removeLast())
                    } else {
                        stacks[to].addLast(stacks[from].removeAt(stacks[from].lastIndex - i))
                    }
                }
            } else if (line.startsWith(" 1 ") && state == States.READING_CRATES) {
                state = States.READY_FOR_INSTRUCTIONS
            } else {
                throw IllegalStateException("In invalid state (${state}) for line:${line}")
            }
        }

        return stacks.fold("") { result, q -> result.plus(q.last()) }
    }

    fun part1(input: List<String>): String {
        return processInput(input, true)
    }

    fun part2(input: List<String>): String {
        return processInput(input, false)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}