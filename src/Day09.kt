fun main() {
    fun part1(input: List<String>): Long {
        return input.map { line -> line.split(' ').map { it.toInt() } }.sumOf { history ->
            var list = history
            var last = 0L
            while (list.any { it != 0 }) {
                last += list.last()
                list = list.zipWithNext { a, b -> b - a }
            }
            last
        }
    }

    fun part2(input: List<String>): Long {
        return input.map { line -> line.split(' ').map { it.toInt() } }.sumOf { history ->
            var list = history
            var first = 0L
            var odd = false
            while (list.any { it != 0 }) {
                if (odd)
                    first -= list.first()
                else
                    first += list.first()
                odd = !odd
                list = list.zipWithNext { a, b -> b - a }
            }
            first
        }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
