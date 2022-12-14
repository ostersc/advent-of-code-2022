class Packet(var packetList: List<Packet> = mutableListOf<Packet>(), var intVal: Int = -1) : Comparable<Packet> {
    companion object {
        private fun Regex.splitWithDelimiter(input: CharSequence) =
            Regex("((?<=%1\$s)|(?=%1\$s))".format(this.pattern)).split(input)

        fun of(input: String): Packet {
            val split = """[,\[\]]""".toRegex().splitWithDelimiter(input).filter { it.isNotBlank() && it != "," }
            return of(split.iterator())
        }

        private fun of(it: Iterator<String>): Packet {
            val packets = mutableListOf<Packet>()
            while (it.hasNext()) {
                packets += when (val s = it.next()) {
                    "[" -> of(it)
                    "]" -> return Packet(packets)
                    else -> Packet(intVal = s.toInt())
                }
            }
            return Packet(packets)
        }
    }

    override fun compareTo(other: Packet): Int {
        if (intVal != -1 && other.intVal != -1) {
            return intVal.compareTo(other.intVal)
        } else if (intVal == -1 && other.intVal == -1) {
            val nonEqualPairs =
                packetList.zip(other.packetList).map { (mine, theirs) -> mine.compareTo(theirs) }.filter { it != 0 }
            if (nonEqualPairs.isEmpty()) {
                return packetList.size.compareTo(other.packetList.size)
            } else {
                return nonEqualPairs.first()
            }
        } else if (intVal == -1) {
            return this.compareTo(Packet(listOf(Packet(intVal = other.intVal))))
        } else {
            return Packet(listOf(Packet(intVal = intVal))).compareTo(other)
        }
    }

    override fun toString(): String {
        var result = ""
        if (this.packetList.isEmpty()) result += intVal.toString()
        else {
            for (p in packetList) {
                result += p.toString()
            }
        }
        return result
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val packets = input.filter { it.isNotBlank() }.map { Packet.of(it) }
        return packets.chunked(2).mapIndexed { ind, (left, right) -> if (left < right) ind + 1 else 0 }.sum()
    }

    fun part2(input: List<String>): Int {
        val packets = input.filter { it.isNotBlank() }.map { Packet.of(it) }.toMutableList()
        val ducePacket=Packet.of("[[2]]")
        val sixerPacket=Packet.of("[[6]]")

        packets+=ducePacket
        packets+=sixerPacket

        packets.sort()

        return (packets.indexOf(ducePacket)+1)* (packets.indexOf(sixerPacket)+1)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")

    val part1 = part1(input)
    println(part1)
    check(part1 == 5390)

    val part2 = part2(input)
    println(part2)
    check(part2==19261)
}