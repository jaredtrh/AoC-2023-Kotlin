fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (winning, nums) = line.split(':')[1].split('|')
            val set = winning.trim().split(Regex(" +")).toSet()

            nums.trim().split(Regex(" +")).fold(1) { acc, x ->
                if (set.contains(x)) acc * 2 else acc
            } / 2
        }
    }

    fun part2(input: List<String>): Int {
        var ans = 0
        var cards = 1
        val future = IntArray(input.size) { 0 }

        for ((i, line) in input.withIndex()) {
            ans += cards

            val (winning, nums) = line.split(':')[1].split('|')
            val set = winning.trim().split(Regex(" +")).toSet()

            val f = nums.trim().split(Regex(" +")).count { set.contains(it) }
            future[i + f] += cards
            cards *= 2
            cards -= future[i]
        }

        return ans
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
