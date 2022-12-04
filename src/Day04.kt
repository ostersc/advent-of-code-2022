fun main() {
    class Assignment() {
        var aLow = 0
        var aHigh = 0
        var bLow = 0
        var bHigh = 0

        constructor(line: String) : this() {
            val d = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex().find(line)!!.destructured
            aLow = d.component1().toInt()
            aHigh = d.component2().toInt()
            bLow = d.component3().toInt()
            bHigh = d.component4().toInt()
        }

        fun containsStrictOverlap(): Boolean {
            return aLow <= bLow && aHigh >= bHigh || bLow <= aLow && bHigh >= aHigh
        }

        fun containsAnyOverlap(): Boolean {
            return bLow >= aLow && bLow <= aHigh || bLow <= aLow && bHigh >= aLow
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