import kotlin.math.*

fun main() {
    fun calc(input: List<String>, expand: Long): Long {
        val vertical = LongArray(input.size) { expand }
        val horizontal = LongArray(input[0].length) { expand }
        val galaxies: MutableList<Pair<Int, Int>> = mutableListOf()

        for ((i, row) in input.withIndex()) {
            for ((j, c) in row.withIndex()) {
                if (c == '#') {
                    vertical[i] = 1
                    horizontal[j] = 1
                    galaxies.add(Pair(i, j))
                }
            }
        }

        for (i in 1 .. vertical.lastIndex) vertical[i] += vertical[i - 1]
        for (j in 1 .. horizontal.lastIndex) horizontal[j] += horizontal[j - 1]

        var ans = 0L
        for (i in galaxies.indices)
            for (j in i + 1 .. galaxies.lastIndex)
                ans += abs(vertical[galaxies[i].first] - vertical[galaxies[j].first]) +
                        abs(horizontal[galaxies[i].second] - horizontal[galaxies[j].second])
        return ans
    }

    fun part1(input: List<String>): Long {
        return calc(input, 2L)
    }

    fun part2(input: List<String>): Long {
        return calc(input, 1000000L)
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(calc(testInput, 10L) == 1030L)
    check(calc(testInput, 100L) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}