fun main() {
    fun getGrids(input: List<String>): List<MutableList<CharArray>> {
        val grids: MutableList<MutableList<CharArray>> = mutableListOf(mutableListOf())
        for (line in input) {
            if (line.isEmpty())
                grids.add(mutableListOf())
            else
                grids.last().add(line.toCharArray())
        }
        return grids
    }

    fun part1(input: List<String>): Int {
        return getGrids(input).sumOf { grid ->
            for (i in 1 ..< grid.size) {
                var k = 0
                outer@ while (i - 1 >= k && i + k < grid.size) {
                    for (j in grid[0].indices)
                        if (grid[i - 1 - k][j] != grid[i + k][j])
                            break@outer
                    k += 1
                }

                if (i - 1 < k || i + k == grid.size)
                    return@sumOf i * 100
            }

            for (j in 1 ..< grid[0].size) {
                var k = 0
                outer@ while (j - 1 >= k && j + k < grid[0].size) {
                    for (i in grid.indices)
                        if (grid[i][j - 1 - k] != grid[i][j + k])
                            break@outer
                    k += 1
                }

                if (j - 1 < k || j + k == grid[0].size)
                    return@sumOf j
            }

            0
        }
    }

    fun part2(input: List<String>): Int {
        return getGrids(input).sumOf { grid ->


            for (i in 1 ..< grid.size) {
                var neq = false
                var k = 0
                outer@ while (i - 1 >= k && i + k < grid.size) {
                    for (j in grid[0].indices) {
                        if (grid[i - 1 - k][j] != grid[i + k][j]) {
                            if (neq) break@outer
                            neq = true
                        }
                    }
                    k += 1
                }

                if ((i - 1 < k || i + k == grid.size) && neq)
                    return@sumOf i * 100
            }

            for (j in 1 ..< grid[0].size) {
                var neq = false
                var k = 0
                outer@ while (j - 1 >= k && j + k < grid[0].size) {
                    for (i in grid.indices) {
                        if (grid[i][j - 1 - k] != grid[i][j + k]) {
                            if (neq) break@outer
                            neq = true
                        }
                    }
                    k += 1
                }

                if ((j - 1 < k || j + k == grid[0].size) && neq)
                    return@sumOf j
            }

            0
        }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}