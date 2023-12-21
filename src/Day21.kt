import kotlin.math.*
import kotlin.collections.*

fun main() {
    fun calc(grid: List<CharArray>, steps: Int): Int {
        val queue = ArrayDeque(listOf(Pair(grid.size / 2, grid.size / 2)))
        var cnt = if (steps % 2 == 0) 1 else 0
        repeat(steps) {
            repeat(queue.size) {
                val (i, j) = queue.removeFirst()
                for ((di, dj) in arrayOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                    if (i + di in grid.indices && j + dj in grid.indices && grid[i + di][j + dj] == '.') {
                        grid[i + di][j + dj] = '*'
                        queue.add(Pair(i + di, j + dj))

                        if ((i + di + j + dj) % 2 == steps % 2)
                            cnt += 1
                    }
                }
            }
        }

        return cnt
    }

    fun part1(input: List<String>): Int {
        return calc(input.map { it.toCharArray() }, 64)
    }

    fun part2(input: List<String>): Long {
        // had to look for hints in r/adventofcode again.. because I missed that there can be unreachable cells
        // grid is an odd length square, S is always in the center
        // middle row and column and border have no rocks
        // rocks are sparse enough that reachable cells expand like a diamond
        // we need to count the reachable cells with the same parity as the edges
        // grid can be split into 5 parts: diamond, top left, top right, bottom left, bottom right
        // 111D222
        // 11DDD22
        // 1DDDDD2
        // DDDDDDD
        // 3DDDDD4
        // 33DDD44
        // 333D444
        // answer is made up of a combination of these 5 parts
        // assume 26501365 - n / 2 is divisible by n so the diamond stops exactly at the border
        // let x = (26501365 - n / 2) / n = (26501365 - 131 / 2) / 131 = 202300 (2023 + 00 easter egg?)
        // first count diamonds: x^2 * (odd cells in diamond) + (x + 1)^2 * (even cells in diamond)
        // then count top left: x * (x + 1) * (odd cells in top left) + (x + 1) * x * (even cells in top left)
        // since other corners are rotation symmetric, it turns out we can just calculate x * (x + 1) * other
        // where other is the sum of odd and even cells for all 4 corners or cells not in the diamond
        val grid = input.map { it.toCharArray() }
        grid[grid.size / 2][grid.size / 2] = '.'
        val diamond = arrayOf(0, 0)
        var other = 0
        val dfs = DeepRecursiveFunction<Pair<Int, Int>, Unit> { (i, j) ->
            if (i !in grid.indices || j !in grid.indices || grid[i][j] != '.')
                return@DeepRecursiveFunction
            grid[i][j] = '*'

            if (abs(i - grid.size / 2) + abs(j - grid.size / 2) <= grid.size / 2)
                diamond[(i + j) % 2] += 1
            else
                other += 1

            callRecursive(Pair(i - 1, j))
            callRecursive(Pair(i + 1, j))
            callRecursive(Pair(i, j - 1))
            callRecursive(Pair(i, j + 1))
        }
        dfs(Pair(grid.size / 2, grid.size / 2))

        val x = 26501365L / grid.size
        return (x + 1) * (x + 1) * diamond[1] +
                x * x * diamond[0] +
                x * (x + 1) * other
    }

    val testInput = readInput("Day21_test")
    check(calc(testInput.map { it.toCharArray() }, 6) == 16)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}