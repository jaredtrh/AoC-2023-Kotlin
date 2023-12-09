import kotlin.math.*

fun main() {
    fun roots(a: Long, b: Long, c: Long): Pair<Double, Double> =
        Pair(
            (-b + sqrt((b * b - 4 * a * c).toDouble())) / (2 * a),
            (-b - sqrt((b * b - 4 * a * c).toDouble())) / (2 * a)
        )

    fun part1(input: List<String>): Long {
        return input[0].split(':')[1].trim().split(Regex(" +")).asSequence().map { it.toLong() }
            .zip(input[1].split(':')[1].trim().split(Regex(" +")).asSequence().map { it.toLong() })
            .map { (time, dist) ->
                val (l, r) = roots(-1L, time, -dist)
                ceil(r).toLong() - floor(l).toLong() - 1
            }
            .reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(':')[1].filter { it != ' ' }.toLong()
        val dist = input[1].split(':')[1].filter { it != ' ' }.toLong()

        val (l, r) = roots(-1L, time, -dist)
        return ceil(r).toLong() - floor(l).toLong() - 1
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
