fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { "${it.find(Char::isDigit)}${it.findLast(Char::isDigit)}".toInt() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            var li = it.indexOfFirst(Char::isDigit)
            var ri = it.indexOfLast(Char::isDigit)
            var l = if (li != -1) it[li].digitToInt() else -1
            var r = if (ri != -1) it[ri].digitToInt() else -1

            for ((i, digit) in arrayOf(
                    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
            ).withIndex()) {
                val dli = it.indexOf(digit)
                val dri = it.lastIndexOf(digit)

                if (dli != -1) {
                    if (li == -1 || dli < li) {
                        li = dli
                        l = i + 1
                    }
                }
                if (dri != -1) {
                    if (ri == -1 || dri > ri) {
                        ri = dri
                        r = i + 1
                    }
                }
            }

            l * 10 + r
        }
    }

    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
