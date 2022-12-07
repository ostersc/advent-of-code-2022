typealias File = Triple<Dir, String, Int>

class Dir(private var name: String, var parent:Dir?) {
    //name->size
    var files = mutableMapOf<String, File>()
    //name->Dir
    var dirs = mutableMapOf<String, Dir>()

    fun calcTotals(maxSize: Int, matchingDirs: MutableList<Pair<Int, Dir>>): Int {
        var total = files.values.sumOf(File::third)
        if (dirs.isEmpty()) {
            if (total <= maxSize) {
                if (total == 0) {
                    println("Skipping empty directory: $name ")
                } else {
                    println("MATCH: Added $name to result in $total")
                    matchingDirs.add(Pair(total, this))
                }
            }
            return total
        }
        for ((_, d) in dirs) {
            println("Traversing into child ${d.name} from $name")
            val c = d.calcTotals(maxSize, matchingDirs)
            total += c
            println("Added $c to running total for $name to result in $total")
        }

        if (total <= maxSize) {
            println("MATCH: Including local files of size $total for $name to get $total")
            matchingDirs.add((Pair(total, this)))
        } else {
            println("Excluding local $name based on total of $total")
        }

        return total
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val root = Dir("/",null)
        var currDir = root
        //read in files
        input.forEach { line ->
            //$ command     run command
            val split = line.split(" ")
            if (split.first() == "$") {
                // we don't care about "ls" and are just assuming if we cant find the dir, then its /
                if (split[1] == "cd") {
                    if (split.last() == "..") {
                        currDir= currDir.parent!!
                    } else {
                        currDir = currDir.dirs.getOrDefault(split.last(), root)
                    }
                }
            } else if (split.first() == "dir") {
                //dir <name>    create new dir
                currDir.dirs.putIfAbsent(split.last(), Dir(split.last(),currDir))
            } else {
                //\d <name>     create new file of size \d
                if (currDir.files.containsKey(split.last())) {
                    var existing = currDir.files.get(split.last())
                    if (existing!!.third != split.first().toInt()) {
                        throw IllegalStateException("Trying to add a file that already exists and got a different size")
                    }
                }
                //currDir.files.putIfAbsent(split.last(), File(currDir, split.last(), split.first().toInt()))
                currDir.files.put(split.last(), File(currDir, split.last(), split.first().toInt()))
            }
        }

//        val matchesTest = mutableListOf<Pair<Int, Dir>>()
//        println("TOTAL: "+root.calcTotals(Int.MAX_VALUE,matchesTest)+"\n\n")

        val matches = mutableListOf<Pair<Int, Dir>>()
        root.calcTotals(100000, matches)
        return matches.sumOf { it.first }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    //check(part2(testInput) == ?)

    val input = readInput("Day07")
    println()
    println(part1(input))
    println(part2(input))
}