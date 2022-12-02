fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(Pair(0, 0)) { max, curr ->
            val subTotal = if (curr.isEmpty()) max.second else max.second + curr.toInt()
            Pair(if (max.first > subTotal) max.first else subTotal, if (curr.isEmpty()) 0 else subTotal)
        }.first
    }

    fun part2(input: List<String>): Int {
        return input.fold(arrayListOf(0)) { sums, curr ->
            if (curr.isEmpty()) {
                sums.add(0)
                sums
            } else {
                sums[sums.lastIndex] += curr.toInt()
                sums
            }
        }.sorted().takeLast(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
