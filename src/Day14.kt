import kotlin.math.sign

enum class ContentType { Air, Rock, Sand, Hole }
data class Point(var x: Int, var y: Int)

class CaveGrid(var hole: Point = Point(500, 0), var contents: MutableMap<Point, ContentType>) {
    var minX = contents.keys.minOf { p -> p.x }
    var maxX = contents.keys.maxOf { p -> p.x }
    var maxY = contents.keys.maxOf { p -> p.y }

    init {
        contents.put(hole, ContentType.Hole)
    }

    fun isInVoid(point: Point): Boolean {
        return point.x < minX || point.x > maxX || point.y > maxY || point.y < 0
    }

    override fun toString(): String {
        var result = ""
        for (y in 0..maxY) {
            for (x in minX..maxX) {
                val p = Point(x, y)
                if (contents.contains(p)) {
                    when (contents.get(p)) {
                        ContentType.Air -> result += "."
                        ContentType.Rock -> result += "#"
                        ContentType.Sand -> result += "o"
                        ContentType.Hole -> result += "+"
                        null -> {}
                    }
                } else {
                    result += "."
                }
            }
            result += "\n"
        }
        return result
    }

    companion object {
        fun of(input: List<String>): CaveGrid {
            val contents = mutableMapOf<Point, ContentType>()
            for (line in input) {
                val points = line.split("->")
                points.zipWithNext().forEach {
                    val start = Point(
                        it.first.split(",").first().trim().toInt(),
                        it.first.split(",").last().trim().toInt()
                    )
                    val end = Point(
                        it.second.split(",").first().trim().toInt(),
                        it.second.split(",").last().trim().toInt()
                    )
                    contents.put(start, ContentType.Rock)
                    contents.put(end, ContentType.Rock)
                    val dx = (end.x - start.x).sign
                    val dy = (end.y - start.y).sign

                    var next = start.copy().apply { x += dx; y += dy }
                    while (next != end) {
                        contents.put(next, ContentType.Rock)
                        next = next.copy().apply { x += dx; y += dy }
                    }
                }
            }

            return CaveGrid(contents = contents)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cave = CaveGrid.of(input)
        println(cave)
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    //check(part2(testInput) == ??)

    val input = readInput("Day14")

    val part1 = part1(input)
    println(part1)
    //check(part1 == ???)

    val part2 = part2(input)
    println(part2)
    //check(part2==???)
}