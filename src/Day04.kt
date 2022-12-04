fun main() {
    class Assignment() {
        var range1 = IntRange(0, 0)
        var range2 = IntRange(0, 0)

        constructor(line: String) : this() {
            val (a, b, c, d) = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex().find(line)!!.destructured.toList()
                .map { it.toInt() }
            range1 = IntRange(a, b)
            range2 = IntRange(c, d)
        }

        fun containsStrictOverlap(): Boolean {
            return range1.intersect(range2).size == range2.count { true } ||
                    range2.intersect(range1).size == range1.count { true }
        }

        fun containsAnyOverlap(): Boolean {
            return range1.intersect(range2).isNotEmpty()
        }
    }

    fun part1(input: List<String>): Int {
        return input.count { line -> Assignment(line).containsStrictOverlap() }
    }

    fun part2(input: List<String>): Int {
        return input.count { line -> Assignment(line).containsAnyOverlap() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}