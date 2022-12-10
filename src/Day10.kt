import kotlin.math.absoluteValue

class CPU(var cycle: Int = 0, var register: Int = 1, var total: Int = 0) {
    fun tick() {
        cycle++
        maybeAccumulate()
    }

    private fun maybeAccumulate() {
        if (20 == cycle % 40) {
            total += register * cycle
        }
    }

    fun add(toAdd: Int) {
        register += toAdd
    }
}

class CRT(private var pixels: BooleanArray = BooleanArray(240), var cpu: CPU = CPU()) {
    fun render() {
        for (i in 0..pixels.lastIndex) {
            if (pixels[i]) print("█") else print(" ")
            if ((i + 1) % 40 == 0) println()
        }
    }

    fun tick(){
        if(((cpu.register-(cpu.cycle%40))).absoluteValue<=1){
            pixels[cpu.cycle]=true
        }
        cpu.tick()
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

    fun part2(input: List<String>){
        val crt = CRT()

        for (line in input) {
            if (line == "noop") {
                crt.tick()
            } else {
                crt.tick()
                crt.tick()
                crt.cpu.add(line.substringAfter(' ').toInt())
            }
        }

       crt.render()
    }

    var testInput = readInput("Day10_test")
    check(part1(testInput) == 0)

    testInput = readInput("Day10_test2")
    check(part1(testInput) == 13140)

    part2(testInput)

    val input = readInput("Day10")
    val part1 = part1(input)
    println(part1)
    check(part1 == 14540)

    part2(input)
}