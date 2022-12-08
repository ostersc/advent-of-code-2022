fun main() {
    data class Tree(val x: Int, val y: Int)

    fun part1(input: List<String>): Int {
        val treeMap = MutableList(0) { mutableListOf<Int>() }
        val visibleTrees = mutableSetOf<Tree>()

        //LOOK FROM LEFT
        //parsing populates the treemap, and also does the look from left
        for (yIndex in 0..input.lastIndex) {
            var rowMax = -1
            treeMap.add(yIndex, mutableListOf())
            for (xIndex in 0..input[yIndex].lastIndex) {
                val currHeight = input[yIndex][xIndex].digitToInt()
                treeMap[yIndex].add(xIndex, currHeight)
                if (currHeight > rowMax) {
                    println("Adding visible tree with height $currHeight from LEFT at $xIndex,$yIndex")
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
                    println("Adding visible tree with height $currHeight from RIGHT at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    rowMax = currHeight
                }
            }
        }

        //LOOK FROM TOP
        //Walk array from top, y 0..n, increasing x
        //Add to set each (x,y) that is > than colMax and update colMax
        for (xIndex in 0 .. treeMap[0].lastIndex) {
            var colMax = -1
            for (yIndex in 0..treeMap.lastIndex) {
                val currHeight = treeMap[yIndex][xIndex]
                if (currHeight > colMax) {
                    println("Adding visible tree with height $currHeight from TOP at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    colMax = currHeight
                }
            }
        }


        //LOOK FROM BOTTOM
        //Walk array from bottom , y n ..0, increasing x
        //Add to set each (x,y) that is > than colMax and update colMax
        for (xIndex in 0 .. treeMap[0].lastIndex) {
            var colMax = -1
            for (yIndex in treeMap.lastIndex downTo 0) {
                val currHeight = treeMap[yIndex][xIndex]
                if (currHeight > colMax) {
                    println("Adding visible tree with height $currHeight from BOTTOM at $xIndex,$yIndex")
                    visibleTrees.add(Tree(xIndex, yIndex))
                    colMax = currHeight
                }
            }
        }
        return visibleTrees.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day08_test")
    val part1Test = part1(testInput)
    check(part1Test == 21)
    val part2Test = part2(testInput)
    //check(part2Test == ?)

    val input = readInput("Day08")

    val part1 = part1(input)
    println(part1)
    //check(part1 == ?)

    val part2 = part2(input)
    //println(part2)
    //check(part2==?)
}