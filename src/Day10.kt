class Sprite(var index: Int = 0)

class CPU(var cycle: Int = 0, var register: Int = 1, var total: Int = 0) {
    fun tick() {
        cycle++
        maybeAccumulate()
    }

    fun maybeAccumulate() {
        if (20 == cycle % 40) {
            total += register * cycle
        }
    }

    fun add(toAdd: Int) {
        register += toAdd
    }
}

class CRT(var pixels: BooleanArray = BooleanArray(240)) {
    fun render() {
        for (i in 0..pixels.lastIndex) {
            if (pixels[i]) print("#") else print(".")
            if ((i + 1) % 40 == 0) println()
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cpu = CPU()

        for (line in input) {
            if (line == "noop") {
                cpu.tick()
            } else {
                cpu.tick()
                cpu.tick()
                cpu.add(line.substringAfter(' ').toInt())
            }
        }
        return cpu.total
    }

    fun part2(input: List<String>): Int {
        val crt = CRT()
        crt.render()
        return input.size
    }

    var testInput = readInput("Day10_test")
    check(part1(testInput) == 0)
    testInput = readInput("Day10_test2")
    check(part1(testInput) == 13140)
    //check(part2(testInput) == ?)

    val input = readInput("Day10")

    val part1 = part1(input)
    println(part1)
    check(part1 == 14540)

    val part2 = part2(input)
    println(part2)
    //check(part2==?)
}