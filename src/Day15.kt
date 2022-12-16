import kotlin.math.absoluteValue

fun log(s: String) {
    //println(s)
}

sealed class LocationContent
class Beacon : LocationContent()
class Sensor(var distanceFromBeacon: Int) : LocationContent()

data class Location(var x: Int, var y: Int) {
    fun distanceFrom(l: Location): Int {
        return (x - l.x).absoluteValue + (y - l.y).absoluteValue
    }
}

class BeaconGrid(var contents: MutableMap<Location, LocationContent>) {
    companion object {
        fun of(input: List<String>): BeaconGrid {
            val g = BeaconGrid(contents = mutableMapOf<Location, LocationContent>())
            for (line in input) {
                val (a, b, c, d) =
                    """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
                        .find(line)!!.destructured.toList().map { it.toInt() }
                val sensor = Location(a, b)
                val beacon = Location(c, d)
                g.contents[beacon] = Beacon()
                g.contents[sensor] = Sensor(sensor.distanceFrom(beacon))
            }
            return g
        }
    }

}

fun main() {
    fun part1(input: List<String>, row: Int): Int {
        var count = 0
        val grid = BeaconGrid.of(input)

        val minX =
            grid.contents.minOf { if (it.value is Sensor) it.key.x - (it.value as Sensor).distanceFromBeacon else 0 }
        val maxX =
            grid.contents.maxOf { if (it.value is Sensor) it.key.x + (it.value as Sensor).distanceFromBeacon else 0 }

        for (i in minX..maxX) {
            val l = Location(i, row)
            log("At location $l")
            if (grid.contents[l] is Sensor) {
                /** if the location is a Sensor, it can't contain a Beacon, so include **/
                count++
                log("\t its a sensor, so upped count to $count")
                continue
            } else if (grid.contents[l] is Beacon) {
                /** if the location is a Beacon, it can't not contain a Beacon, so exclude **/
                log("\t its a beacon")
                continue
            } else if (grid.contents.count { it.value is Sensor && l.distanceFrom(it.key) <= (it.value as Sensor).distanceFromBeacon } > 0) {
                /** there's at least 1 sensor that this location is closer to than that sensor's nearest beacon, so include it **/
                count++
                log("\t there's a nearby sensor, so upped count to $count")
            }
        }

        return count
    }

    fun part2(input: List<String>, max:Long): Long {
        val grid = BeaconGrid.of(input)

        // generate a list of locations that are on the edge of each sensors vision
        val locations = mutableListOf<Location>()
        for ((l, s) in grid.contents.filter { it.value is Sensor }) {
            val dist = (s as Sensor).distanceFromBeacon
            for (i in l.x - dist - 1..l.x + dist + 1) {
                var h:Int
                if(i<=l.x){
                     h = (l.x-dist-i-1).absoluteValue
                }else{
                     h = (l.x-dist+i+1).absoluteValue
                }

                //up
                val up = Location(i, l.y + h)
                if (up.x > 0 && up.y >= 0 && up.x <= max && up.y <= max) {
                    locations.add(up)
                }
                //down
                val down = Location(i, l.y - h)
                if (down.x > 0 && down.y >= 0 && down.x <= max && down.y <= max) {
                    locations.add(down)
                }
            }
        }

        //find the location that is beyond all sensors vision
        var beacon = Location(0, 0)
        for (l in locations) {
            if (grid.contents[l] is Sensor) {
                continue
            } else if (grid.contents[l] is Beacon) {
                continue
            } else if (grid.contents.count { it.value is Sensor && l.distanceFrom(it.key) > (it.value as Sensor).distanceFromBeacon } == input.size) {
                beacon = l
            }
        }

        return beacon.x * 4_000_000L + beacon.y
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")

    val part1 = part1(input, 2000000)
    println(part1)
    check(part1 == 5112034)

    val part2 = part2(input, 4_000_000L)
    println(part2)
    check(part2==13172087230812L)
}