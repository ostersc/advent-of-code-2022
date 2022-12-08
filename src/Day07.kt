const val MAX_SIZE =  70_000_000
const val FREE_SIZE = 30_000_000

data class File(val dir:Dir, val name:String, val size:Int)

class Dir(var name: String, var parent: Dir?) {
    //name->size
    var files = mutableMapOf<String, File>()
    //name->Dir
    var dirs = mutableMapOf<String, Dir>()

    fun calcTotals(sizePredicate: (Int) -> Boolean, matchingDirs: MutableList<Pair<Int, Dir>>): Int {
        var total = files.values.sumOf(File::size)
        if (dirs.isEmpty()) {
            if (sizePredicate(total)) matchingDirs.add(Pair(total, this))
            return total
        }
        for ((_, d) in dirs) {
            total += d.calcTotals(sizePredicate, matchingDirs)
        }
        if (sizePredicate(total)) {
            matchingDirs.add((Pair(total, this)))
        }
        return total
    }
}

fun main() {

    fun parseInput(input: List<String>): Dir {
        val root = Dir("/", null)
        var currDir = root
        //read in files
        input.forEach { line ->
            //$ command     run command
            val split = line.split(" ")
            if (split.first() == "$") {
                // we don't care about "ls" and are just assuming if we cant find the dir, then its /
                if (split[1] == "cd") {
                    currDir = if (split.last() == "..") {
                        currDir.parent!!
                    } else {
                        currDir.dirs.getOrDefault(split.last(), root)
                    }
                }
            } else if (split.first() == "dir") {
                //dir <name>    create new dir
                currDir.dirs.putIfAbsent(split.last(), Dir(split.last(), currDir))
            } else {
                //\d <name>     create new file of size \d
                currDir.files.putIfAbsent(split.last(), File(currDir, split.last(), split.first().toInt()))
            }
        }
        return root
    }

    fun part1(input: List<String>): Int {
        val root = parseInput(input)

        val matches = mutableListOf<Pair<Int, Dir>>()
        root.calcTotals({ size -> size <= 100000 }, matches)
        return matches.sumOf { it.first }
    }

    fun part2(input: List<String>): Int {
        val root = parseInput(input)

        val freeSpace = MAX_SIZE -  root.calcTotals({ true }, mutableListOf<Pair<Int, Dir>>())
        val toDelete=FREE_SIZE-freeSpace

        val candidates = mutableListOf<Pair<Int, Dir>>()
        root.calcTotals({ size -> size >= toDelete }, candidates)

        return candidates.minOf{ it.first }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")

    val part1 = part1(input)
    println(part1)
    check(part1 == 1454188)

    val part2 = part2(input)
    println(part2)
    check(part2==4183246)
}