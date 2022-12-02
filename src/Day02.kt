fun main() {
    //A for Rock, B for Paper, and C for Scissors.
    // X for Rock, Y for Paper, and Z for Scissors.

    //The score for a single round is the score for the shape you selected
    // (1 for Rock, 2 for Paper, and 3 for Scissors)
    // plus the score for the outcome of the round
    // (0 if you lost, 3 if the round was a draw, and 6 if you won)

    fun scoreRoundPart1(round: String): Int {
        val s = round.split(" ")
        when (s.first()) {
            "A" -> when (s.last()) {
                "X" -> return 3+1
                "Y" -> return 6+2
                "Z" -> return 0+3
            }
            "B" -> when (s.last()) {
                "X" -> return 0+1
                "Y" -> return 3+2
                "Z" -> return 6+3
            }
            "C" -> when (s.last()) {
                "X" -> return 6+1
                "Y" -> return 0+2
                "Z" -> return 3+3
            }
        }
        return 0
    }
    //A for Rock, B for Paper, and C for Scissors.
    //X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win. Good luck!"
    fun scoreRoundPart2(round: String): Int {
        val s = round.split(" ")
        when (s.first()) {
            "A" -> when (s.last()) {
                "X" -> return 0+3
                "Y" -> return 3+1
                "Z" -> return 6+2
            }
            "B" -> when (s.last()) {
                "X" -> return 0+1
                "Y" -> return 3+2
                "Z" -> return 6+3
            }
            "C" -> when (s.last()) {
                "X" -> return 0+2
                "Y" -> return 3+3
                "Z" -> return 6+1
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { scoreRoundPart1(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { scoreRoundPart2(it) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
