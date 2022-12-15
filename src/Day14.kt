import kotlin.math.sign

enum class ContentType { Air, Rock, Sand, Hole }
data class Point(var x: Int, var y: Int){
    fun down(): Point{
        return this.copy(y=y+1)
    }
    fun diagLeft(): Point{
        return this.copy(x=x-1,y=y+1)
    }
    fun diagRigth(): Point{
        return this.copy(x=x+1,y=y+1)
    }
}

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

    fun dropSand(): Boolean {
        var s = hole.copy()

        while (!isInVoid(s)) {
            if(contents.getOrDefault(s.down(), ContentType.Air)==ContentType.Air){
                s=s.down()
            }else if(contents.getOrDefault(s.diagLeft(), ContentType.Air)==ContentType.Air){
                s=s.diagLeft()
            }else if (contents.getOrDefault(s.diagRigth(), ContentType.Air)==ContentType.Air){
                s=s.diagRigth()
            }else{
                break
            }
        }

        if(!isInVoid(s)) {
            contents.put(s, ContentType.Sand)
            return true
        }

        return false
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
        while (cave.dropSand()) {
            //println(cave)
        }
        return cave.contents.filterValues { it.equals(ContentType.Sand) }.size
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
    check(part1 == 1406)

    val part2 = part2(input)
    println(part2)
    //check(part2==???)
}