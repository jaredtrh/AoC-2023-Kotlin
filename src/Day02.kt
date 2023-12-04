fun main() {
    fun part1(input: List<String>): Int {
        return input.withIndex().sumOf { (i, line) ->
            if (line.split(": ")[1].split("; ", ", ")
                .all {
                    val (xs, color) = it.split(' ')
                    val x = xs.toInt()

                    when (color) {
                        "red" -> x <= 12
                        "green" -> x <= 13
                        else -> x <= 14
                    }
                }) i + 1 else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            line.split(": ")[1].split("; ", ", ")
                .map { it.split(' ') }
                .groupBy { it[1] }
                .map { entry -> entry.value.maxOf { it[0].toInt() } }
                .reduce(Int::times)
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
