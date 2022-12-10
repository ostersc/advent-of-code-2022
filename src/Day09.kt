import kotlin.math.absoluteValue

enum class Direction { U, D, L, R }

fun main() {
    data class Point(var x: Int, var y: Int)

    val head = Point(0, 0)
    val tail = Point(0, 0)
    val visitedLocations = mutableSetOf(Point(0, 0))

    fun moveHead(d: Direction, distance: Int) {
        println("Tail is at $tail and chasing head at $head, while it moves $d $distance times")
        repeat(distance) {
            when (d) {
                Direction.U -> head.y += 1
                Direction.D -> head.y -= 1
                Direction.L -> head.x -= 1
                Direction.R -> head.x += 1
            }
            print("Head is now at $head\t")

            val origin = tail.copy()
            if (((head.x - origin.x).absoluteValue <= 1 && (head.y - origin.y).absoluteValue <= 1)) {
                print("They are touching, so not moving tail, so ")
            } else {
                if (head.x - origin.x > 1 || (head.x - origin.x == 1 && head.y != origin.y)) tail.x += 1 else if (origin.x - head.x > 1 || (origin.x - head.x == 1 && head.y != origin.y)) tail.x -= 1
                if (head.y - origin.y > 1 || (head.y - origin.y == 1 && head.x != origin.x)) tail.y += 1 else if (origin.y - head.y > 1 || (origin.y - head.y == 1 && head.x != origin.x)) tail.y -= 1
            }
            visitedLocations.add(tail.copy())
            println("Tail is now at $tail")
        }
    }

    fun part1(input: List<String>): Int {
        head.x=0
        head.y=0
        tail.x=0
        tail.y=0
        visitedLocations.clear()
        visitedLocations.add(Point(0,0))
        for (line in input) moveHead(
            Direction.valueOf(line[0].toString()), line.substringAfter(' ').toInt()
        )
        return visitedLocations.size
    }


    fun part2(input: List<String>): Int {
        return input.size
    }

    val part1Test = part1(readInput("Day09_test"))
    println(part1Test)
    check(part1Test == 13)

    val part2Test = part2(readInput("Day09_test2"))
//    check(part2Test == 36)

    val input = readInput("Day09")

    println("\n\n==========")
    val part1 = part1(input)
    println(part1)
    check(part1 == 6314)

    val part2 = part2(input)
    println(part2)
    //check(part2 == ???)
}
