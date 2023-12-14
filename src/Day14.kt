fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }

        return grid[0].indices.sumOf {
            var load = 0
            var cnt = 0
            for (i in grid.indices.reversed()) {
                when (grid[i][it]) {
                    'O' -> cnt += 1
                    '#' -> {
                        load += cnt * (cnt + 1) / 2 + cnt * (grid.lastIndex - (i + cnt))
                        cnt = 0
                    }
                }
            }
            load + cnt * (cnt + 1) / 2 + cnt * (grid.lastIndex - (cnt - 1))
        }
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toMutableList()

        val grids: MutableList<List<CharArray>> = mutableListOf()
        val idx: MutableMap<String, Int> = mutableMapOf()
        while (!idx.contains(grid.joinToString("") { it.joinToString("") })) {
            idx[grid.joinToString("") { it.joinToString("") }] = grids.size
            grids.add(grid.map { it.copyOf() })

            // roll north
            for (j in grid[0].indices) {
                var k = 0
                for (i in grid.indices) {
                    when (grid[i][j]) {
                        'O' -> {
                            grid[i][j] = '.'
                            grid[k][j] = 'O'
                            k += 1
                        }
                        '#' -> k = i + 1
                    }
                }
            }
            // roll west
            for (i in grid.indices) {
                var k = 0
                for (j in grid[0].indices) {
                    when (grid[i][j]) {
                        'O' -> {
                            grid[i][j] = '.'
                            grid[i][k] = 'O'
                            k += 1
                        }
                        '#' -> k = j + 1
                    }
                }
            }
            // roll south
            for (j in grid[0].indices) {
                var k = grid.lastIndex
                for (i in grid.indices.reversed()) {
                    when (grid[i][j]) {
                        'O' -> {
                            grid[i][j] = '.'
                            grid[k][j] = 'O'
                            k -= 1
                        }
                        '#' -> k = i - 1
                    }
                }
            }
            // roll east
            for (i in grid.indices) {
                var k = grid[0].lastIndex
                for (j in grid[0].indices.reversed()) {
                    when (grid[i][j]) {
                        'O' -> {
                            grid[i][j] = '.'
                            grid[i][k] = 'O'
                            k -= 1
                        }
                        '#' -> k = j - 1
                    }
                }
            }
        }

        val i = idx[grid.joinToString("") { it.joinToString("") }]!!
        return grids[(1000000000 - i) % (grids.size - i) + i].withIndex().sumOf { (i, row) ->
            row.sumOf { if (it == 'O') grid.size - i else 0 }
        }
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}