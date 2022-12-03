fun main() {

    fun toPriority(c: Char) = if (c.isUpperCase()) c.code - 'A'.code + 26 + 1 else c.code - 'a'.code + 1

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val c = it.toCharArray(0, it.length / 2).intersect(
                it.toCharArray(it.length / 2, it.length).toSet()
            ).first()
            toPriority(c)
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            val c = it[0].toCharArray().intersect(
                it[1].toCharArray().toSet()
            ).intersect(
                it[2].toCharArray().toSet()
            ).first()
            toPriority(c)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
