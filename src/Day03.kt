import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        return input.withIndex().sumOf { (i, line) ->
            Regex("\\d+").findAll(line).sumOf {
                if ((max(0, i - 1) .. min(input.size - 1, i + 1)).any { ii ->
                    (max(0, it.range.first - 1) .. min(line.length - 1, it.range.last + 1)).any { jj ->
                        input[ii][jj] != '.' && !input[ii][jj].isDigit()
                    }
                }) it.value.toInt() else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val nums = mutableListOf<Int>()
        val id = Array(input.size) { IntArray(input[0].length) { -1 } }

        for ((i, line) in input.withIndex()) {
            for (match in Regex("\\d+").findAll(line)) {
                for (j in match.range)
                    id[i][j] = nums.size
                nums.add(match.value.toInt())
            }
        }

        return input.withIndex().sumOf { (i, line) ->
            line.withIndex().sumOf inner@ { (j, c) ->
                if (c != '*') return@inner 0

                val set = mutableSetOf<Int>()
                for (ii in max(0, i - 1) .. min(input.size - 1, i + 1))
                    for (jj in max(0, j - 1) .. min(line.length - 1, j + 1))
                        if (id[ii][jj] != -1)
                            set.add(id[ii][jj])

                if (set.size == 2)
                    set.map { nums[it] }.reduce(Int::times)
                else
                    0
            }
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
