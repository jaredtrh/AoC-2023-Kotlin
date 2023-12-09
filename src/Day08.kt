fun main() {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    fun part1(input: List<String>): Long {
        val map = input.asSequence().drop(2).associateBy(
            { it.substring(0, 3) },
            { Pair(it.substring(7, 10), it.substring(12, 15)) }
        )

        var node = "AAA"
        var i = 0
        var ans = 0L
        while (node != "ZZZ") {
            node = if (input[0][i] == 'L') map[node]!!.first else map[node]!!.second
            i = if (i < input[0].lastIndex) i + 1 else 0
            ans += 1
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        val map = input.asSequence().drop(2).associateBy(
            { it.substring(0, 3) },
            { Pair(it.substring(7, 10), it.substring(12, 15)) }
        )

        return map.keys.asSequence().filter { it.endsWith('A') }
            .map {
                var node = it
                var i = 0
                var steps = 0L
                while (!node.endsWith('Z')) {
                    node = if (input[0][i] == 'L') map[node]!!.first else map[node]!!.second
                    i = if (i < input[0].lastIndex) i + 1 else 0
                    steps += 1
                }
                steps
            }
            .reduce(::lcm)
    }

    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")
    val testInput3 = readInput("Day08_test3")
    check(part1(testInput1) == 2L)
    check(part1(testInput2) == 6L)
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
