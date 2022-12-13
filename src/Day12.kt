import java.util.*

fun main() {
    // I knew this was a classic dijkstra problem and didn't feel like reimplmenting, so stole from
    // https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Kotlin
    class Edge(val v1: String, val v2: String, val dist: Int)

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    class Vertex(val name: String) : Comparable<Vertex> {

        var dist = Int.MAX_VALUE  // MAX_VALUE assumed to be infinity
        var previous: Vertex? = null
        val neighbours = HashMap<Vertex, Int>()

        fun printPath() {
            if (this == previous) {
                print(name)
            } else if (previous == null) {
                print("$name(unreached)")
            } else {
                previous!!.printPath()
                print(" -> $name($dist)")
            }
        }

        override fun compareTo(other: Vertex): Int {
            if (dist == other.dist) return name.compareTo(other.name)
            return dist.compareTo(other.dist)
        }

        override fun toString() = "($name, $dist)"
    }

    class Graph(
        val edges: List<Edge>, val directed: Boolean, val showAllPaths: Boolean = false
    ) {
        // mapping of vertex names to Vertex objects, built from a set of Edges
        val graph = HashMap<String, Vertex>(edges.size)

        init {
            // one pass to find all vertices
            for (e in edges) {
                if (!graph.containsKey(e.v1)) graph.put(e.v1, Vertex(e.v1))
                if (!graph.containsKey(e.v2)) graph.put(e.v2, Vertex(e.v2))
            }

            // another pass to set neighbouring vertices
            for (e in edges) {
                graph[e.v1]!!.neighbours.put(graph[e.v2]!!, e.dist)
                // also do this for an undirected graph if applicable
                if (!directed) graph[e.v2]!!.neighbours.put(graph[e.v1]!!, e.dist)
            }
        }

        /** Runs dijkstra using a specified source vertex */
        fun dijkstra(startName: String) {
            if (!graph.containsKey(startName)) {
                println("Graph doesn't contain start vertex '$startName'")
                return
            }
            val source = graph[startName]
            val q = TreeSet<Vertex>()

            // set-up vertices
            for (v in graph.values) {
                v.previous = if (v == source) source else null
                v.dist = if (v == source) 0 else Int.MAX_VALUE
                q.add(v)
            }

            dijkstra(q)
        }

        /** Implementation of dijkstra's algorithm using a binary heap */
        private fun dijkstra(q: TreeSet<Vertex>) {
            while (!q.isEmpty()) {
                // vertex with shortest distance (first iteration will return source)
                val u = q.pollFirst()
                // if distance is infinite we can ignore 'u' (and any other remaining vertices)
                // since they are unreachable
                if (u.dist == Int.MAX_VALUE) break

                //look at distances to each neighbour
                for (a in u.neighbours) {
                    val v = a.key // the neighbour in this iteration

                    val alternateDist = u.dist + a.value
                    if (alternateDist < v.dist) { // shorter path to neighbour found
                        q.remove(v)
                        v.dist = alternateDist
                        v.previous = u
                        q.add(v)
                    }
                }
            }
        }

        /** Prints a path from the source to the specified vertex */
        fun printPath(endName: String) {
            if (!graph.containsKey(endName)) {
                println("Graph doesn't contain end vertex '$endName'")
                return
            }
            print(if (directed) "Directed   : " else "Undirected : ")
            graph[endName]!!.printPath()
            println()
            if (showAllPaths) printAllPaths() else println()
        }

        /** Prints the path from the source to every vertex (output order is not guaranteed) */
        private fun printAllPaths() {
            for (v in graph.values) {
                v.printPath()
                println()
            }
            println()
        }
    }

    data class Map(
        val startLocation: String, val endLocation: String, val lowestLocations: List<String>, val edges: List<Edge>
    )

    fun parseMap(input: List<String>): Map {
        val edges = mutableListOf<Edge>()

        val lowestLocations = mutableListOf<String>()
        var startX = -1
        var startY = -1

        var endX = -1
        var endY = -1
        for (lineInd in 0..input.lastIndex) {
            for (charInd in 0..input[lineInd].lastIndex) {
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
                        lowestLocations += "$charInd X $lineInd"
                    }
                }

                //up
                if (lineInd - 1 >= 0) {
                    var other = input[lineInd - 1][charInd]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        edges += Edge("$charInd X $lineInd", "$charInd X ${lineInd - 1}", 1)
                    }
                }

                //down
                if (lineInd + 1 <= input.lastIndex) {
                    var other = input[lineInd + 1][charInd]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        edges += Edge("$charInd X $lineInd", "$charInd X ${lineInd + 1}", 1)
                    }
                }

                //left
                if (charInd - 1 >= 0) {
                    var other = input[lineInd][charInd - 1]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        edges += Edge("$charInd X $lineInd", "${charInd - 1} X $lineInd", 1)
                    }
                }

                //right
                if (charInd + 1 <= input[lineInd].lastIndex) {
                    var other = input[lineInd][charInd + 1]
                    if (other == 'E') other = 'z'
                    if (other.code <= c.code + 1) {
                        edges += Edge("$charInd X $lineInd", "${charInd + 1} X $lineInd", 1)
                    }
                }
            }
        }

        val start = "$startX X $startY"
        val end = "$endX X $endY"

        return Map(start, end, lowestLocations, edges)

    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)

        val graph = Graph(map.edges, true)
        graph.dijkstra(map.startLocation)

        val dist = graph.graph.get(map.endLocation)!!.dist
        //graph.printPath(map.endLocation)

        return dist
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)
        var shortestDistance = Int.MAX_VALUE
        val graph = Graph(map.edges, true)

        for (potentialStart in map.lowestLocations) {
            graph.dijkstra(potentialStart)
            val dist = graph.graph.get(map.endLocation)!!.dist
            //graph.printPath(map.endLocation)
            if (dist <= shortestDistance) shortestDistance = dist
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
    check(part2 == 478)
}