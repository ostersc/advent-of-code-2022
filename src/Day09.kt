import kotlin.math.absoluteValue

enum class Direction { U, D, L, R }

fun main() {
    data class Point(var x: Int, var y: Int)

    fun moveHead(rope: List<Point>, d: Direction, distance: Int, visitedLocations: MutableSet<Point>) {
        repeat(distance) {
            var head = rope[0]
            when (d) {
                Direction.U -> head.y += 1
                Direction.D -> head.y -= 1
                Direction.L -> head.x -= 1
                Direction.R -> head.x += 1
            }
            print("Head is now at $head\t")

            rope.indices.windowed(2, 1) { (hInd, tInd) ->
                head = rope[hInd]
                val tail = rope[tInd]
                val origin = tail.copy()
                if (((head.x - origin.x).absoluteValue <= 1 && (head.y - origin.y).absoluteValue <= 1)) {
                    print("They are touching, so not moving tail, so ")
                } else {
                    if (head.x - origin.x > 1 || (head.x - origin.x == 1 && head.y != origin.y)) tail.x += 1 else if (origin.x - head.x > 1 || (origin.x - head.x == 1 && head.y != origin.y)) tail.x -= 1
                    if (head.y - origin.y > 1 || (head.y - origin.y == 1 && head.x != origin.x)) tail.y += 1 else if (origin.y - head.y > 1 || (origin.y - head.y == 1 && head.x != origin.x)) tail.y -= 1
                }
            }

            visitedLocations.add(rope.last().copy())
            println("Tail is now at ${rope.last()}")
        }
    }

    fun moveRope(input: List<String>, rope: List<Point>): Int {

        val visitedLocations = mutableSetOf(Point(0, 0))

        for (line in input) moveHead(
            rope, Direction.valueOf(line[0].toString()), line.substringAfter(' ').toInt(), visitedLocations
        )
        return visitedLocations.size
    }

    fun part1(input: List<String>): Int {
        return moveRope(input, listOf(Point(0, 0), Point(0, 0)))
    }


    fun part2(input: List<String>): Int {
        return moveRope(input, generateSequence { Point(0, 0) }.take(10).toList())
    }

    val part1Test = part1(readInput("Day09_test"))
    println(part1Test)
    check(part1Test == 13)

    val part2Test = part2(readInput("Day09_test2"))
    check(part2Test == 36)

    val input = readInput("Day09")

    println("\n\n==========")
    val part1 = part1(input)
    println(part1)
    check(part1 == 6314)

    val part2 = part2(input)
    println(part2)
    check(part2 == 2504)
}
