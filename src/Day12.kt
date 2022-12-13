fun main() {

    //so this was clearly a dijkstra's problem and I didn't feel like reimplementing that, so grabbed the
    // first basic kotlin impl I found of that and copy/pasted just simplifying some syntax
    // https://www.atomiccommits.io/dijkstras-algorithm-in-kotlin
    fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(): Set<T> = this.map { (a, b) -> listOf(a, b) }.flatten().toSet()

    fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(predicate: (T) -> Boolean): Set<T> =
        this.map { (a, b) -> listOf(a, b) }.flatten().filter(predicate).toSet()

    data class Graph<T>(
        val vertices: Set<T>, val edges: Map<T, Set<T>>, val weights: Map<Pair<T, T>, Int>
    ) {
        constructor(weights: Map<Pair<T, T>, Int>) : this(
            vertices = weights.keys.toList().getUniqueValuesFromPairs(),
            edges = weights.keys.groupBy { it.first }
                .mapValues { it.value.getUniqueValuesFromPairs { x -> x !== it.key } }.withDefault { emptySet() },
            weights = weights
        )
    }

    fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
        val sub: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

        /*
         * delta represents the length of the shortest distance paths
         * from start to v, for v in vertices.
         *
         * The values are initialized to infinity, as we'll be getting the key with the min value
         */
        val delta = graph.vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        delta[start] = 0

        val previous: MutableMap<T, T?> = graph.vertices.associateWith { null }.toMutableMap()

        while (sub != graph.vertices) {
            // let v be the closest vertex that has not yet been visited
            val v: T = delta.filter { !sub.contains(it.key) }.minBy { it.value }.key

            graph.edges.getValue(v).minus(sub).forEach { neighbor ->
                val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

                if (newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = v
                }
            }

            sub.add(v)
        }

        return previous.toMap()
    }

    fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
        fun pathTo(start: T, end: T): List<T> {
            if (shortestPathTree[end] == null) return listOf(end)
            return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
        }

        return pathTo(start, end)
    }

    data class Map(val startLocation:String, val endLocation:String, val lowestLocations:List<String>,val weights: MutableMap<Pair<String, String>, Int> )

    fun parseMap(input: List<String>): Map {
        val weights = mutableMapOf<Pair<String, String>, Int>()
        val lowestLocations= mutableListOf<String>()
        var startX = -1
        var startY = -1

        var endX = -1
        var endY = -1
        for (lineInd in 0..input.lastIndex) {
            for (charInd in 0..input[lineInd].lastIndex) {
                //why is this needed? graph alg above didn't handle undefined self-paths
                weights[Pair("$charInd X $lineInd", "$charInd X $lineInd")] = 1

                //build edges from c
                var c = input[lineInd][charInd]
                when (c) {
                    'S' -> {
                        startX = charInd
                        startY = lineInd
                        c = 'a'
                    }
                    'E' -> {
                        endX = charInd
                        endY = lineInd
                    }
                    'a' -> {
                        lowestLocations+="$charInd X $lineInd"
                    }
                }

                //up
                if (lineInd - 1 >= 0) {
                    var other = input[lineInd - 1][charInd]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        weights[Pair("$charInd X $lineInd", "$charInd X ${lineInd - 1}")] = 1
                    }
                }

                //down
                if (lineInd + 1 <= input.lastIndex) {
                    var other = input[lineInd + 1][charInd]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        weights[Pair("$charInd X $lineInd", "$charInd X ${lineInd + 1}")] = 1
                    }
                }

                //left
                if (charInd - 1 >= 0) {
                    var other = input[lineInd][charInd - 1]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        weights[Pair("$charInd X $lineInd", "${charInd - 1} X $lineInd")] = 1
                    }
                }

                //right
                if (charInd + 1 <= input[lineInd].lastIndex) {
                    var other = input[lineInd][charInd + 1]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        weights[Pair("$charInd X $lineInd", "${charInd + 1} X $lineInd")] = 1
                    }
                }
            }
        }

        val start = "$startX X $startY"
        val end = "$endX X $endY"

        return Map(start, end, lowestLocations, weights)

    }

    fun part1(input: List<String>): Int {
        val map= parseMap(input)
        val path = shortestPath(dijkstra(Graph(map.weights), map.startLocation), map.startLocation, map.endLocation)

        //want the steps in the path, not counting the start
        return path.size-1
    }

    fun part2(input: List<String>): Int {
        val map= parseMap(input)
        var shortestDistance=Int.MAX_VALUE

        for(potentialStart in map.lowestLocations){

            val shortestPathTree = dijkstra(Graph(map.weights), potentialStart)
            val path = shortestPath(shortestPathTree, potentialStart, map.endLocation)
            if(path.size-1<=shortestDistance) shortestDistance=path.size-1
        }

        return shortestDistance
    }

    val testInput = readInput("Day12_test")
    var testResult = part1(testInput)
    check(testResult == 31)
    testResult = part2(testInput)
    check(testResult == 29)

    val input = readInput("Day12")

    val part1 = part1(input)
    println(part1)
    check(part1 == 484)

    val part2 = part2(input)
    println(part2)
    //check(part2 == ??)
}