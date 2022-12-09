fun main() {
    data class Tree(val x: Int, val y: Int)

    fun parseTrees(
        input: List<String>,
        treeMap: MutableList<MutableList<Int>>,
        visibleTrees: MutableSet<Tree>
    ) {
        //LOOK FROM LEFT
        //parsing populates the treemap, and also does the look from left
        for (yIndex in 0..input.lastIndex) {
            var rowMax = -1
            treeMap.add(yIndex, mutableListOf())
            for (xIndex in 0..input[yIndex].lastIndex) {
                val currHeight = input[yIndex][xIndex].digitToInt()
                treeMap[yIndex].add(xIndex, currHeight)
                if (currHeight > rowMax) {
                    //println("Adding visible tree with height $currHeight from LEFT at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    rowMax = currHeight
                }
            }
        }

        //LOOK FROM RIGHT
        //Walk array from right, x n ..0, increasing y
        //Add to set each (x,y) that is > than rowMax and update rowMax
        for (yIndex in 0..treeMap.lastIndex) {
            var rowMax = -1
            for (xIndex in treeMap[yIndex].lastIndex downTo 0) {
                val currHeight = treeMap[yIndex][xIndex]
                if (currHeight > rowMax) {
                    //println("Adding visible tree with height $currHeight from RIGHT at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    rowMax = currHeight
                }
            }
        }

        //LOOK FROM TOP
        //Walk array from top, y 0..n, increasing x
        //Add to set each (x,y) that is > than colMax and update colMax
        for (xIndex in 0..treeMap[0].lastIndex) {
            var colMax = -1
            for (yIndex in 0..treeMap.lastIndex) {
                val currHeight = treeMap[yIndex][xIndex]
                if (currHeight > colMax) {
                    //println("Adding visible tree with height $currHeight from TOP at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    colMax = currHeight
                }
            }
        }


        //LOOK FROM BOTTOM
        //Walk array from bottom , y n ..0, increasing x
        //Add to set each (x,y) that is > than colMax and update colMax
        for (xIndex in 0..treeMap[0].lastIndex) {
            var colMax = -1
            for (yIndex in treeMap.lastIndex downTo 0) {
                val currHeight = treeMap[yIndex][xIndex]
                if (currHeight > colMax) {
                    //println("Adding visible tree with height $currHeight from BOTTOM at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    colMax = currHeight
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val treeMap = MutableList(0) { mutableListOf<Int>() }
        val visibleTrees = mutableSetOf<Tree>()

        parseTrees(input, treeMap, visibleTrees)
        return visibleTrees.size
    }

    fun scenicScoreFor(t: Tree, treeMap: MutableList<MutableList<Int>>): Int {
        var score: Int
        val height = treeMap[t.y][t.x]

        var dirScore = 0
        //UP
        for (y in t.y - 1 downTo 0) {
            if (treeMap[y][t.x] < height) {
                dirScore++
            } else if (treeMap[y][t.x] >= height) {
                dirScore++
                break
            }
        }
        score = dirScore


        //DOWN
        dirScore = 0
        for (y in t.y + 1..treeMap.lastIndex) {
            if (treeMap[y][t.x] < height) {
                dirScore++
            } else if (treeMap[y][t.x] >= height) {
                dirScore++
                break
            }
        }
        score *= dirScore

        //RIGHT
        dirScore = 0
        for (x in t.x + 1..treeMap.lastIndex) {
            if (treeMap[t.y][x] < height) {
                dirScore++
            } else if (treeMap[t.y][x] >= height) {
                dirScore++
                break
            }
        }
        score *= dirScore

        //LEFT
        dirScore = 0
        for (x in t.x - 1 downTo 0) {
            if (treeMap[t.y][x] < height) {
                dirScore++
            } else if (treeMap[t.y][x] >= height) {
                dirScore++
                break
            }
        }
        score *= dirScore

        return score
    }

    fun part2(input: List<String>): Int {
        val treeMap = MutableList(0) { mutableListOf<Int>() }
        val visibleTrees = mutableSetOf<Tree>()

        parseTrees(input, treeMap, visibleTrees)

        var max = -1
        for (t in visibleTrees) {
            val tScore = scenicScoreFor(t, treeMap)
            if (tScore > max) {
                max = tScore
            }
        }

        return max
    }

    val testInput = readInput("Day08_test")
    val part1Test = part1(testInput)
    check(part1Test == 21)
    val part2Test = part2(testInput)
    check(part2Test == 8)

    val input = readInput("Day08")

    val part1 = part1(input)
    println(part1)
    check(part1 == 1798)

    val part2 = part2(input)
    println(part2)
    check(part2==259308)
}