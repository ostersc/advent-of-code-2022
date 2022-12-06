fun main() {
    fun part1(input: List<String>): Int {
        input.first().toCharArray().toList().windowed(4).forEachIndexed { index, chars ->
            if (chars.toSet().size >= 4) {
                return index+4
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        input.first().toCharArray().toList().windowed(14).forEachIndexed { index, chars ->
            if (chars.toSet().size >= 14) {
                return index+14
            }
        }
        return 0
    }

    val testInput = readInput("Day06_test")
    //check(part1(testInput) == 5)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
