import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }

        var longest = 0
        fun dfs(i: Int, j: Int, steps: Int) {
            if (i !in grid.indices || j !in grid[0].indices || grid[i][j] == '#')
                return
            if (i == grid.lastIndex && j == grid[0].lastIndex - 1) {
                longest = max(longest, steps)
                return
            }
            val c = grid[i][j]
            grid[i][j] = '#'
            when (c) {
                '^' -> dfs(i - 1, j, steps + 1)
                'v' -> dfs(i + 1, j, steps + 1)
                '<' -> dfs(i, j - 1, steps + 1)
                '>' -> dfs(i, j + 1, steps + 1)
                else -> {
                    dfs(i - 1, j, steps + 1)
                    dfs(i + 1, j, steps + 1)
                    dfs(i, j - 1, steps + 1)
                    dfs(i, j + 1, steps + 1)
                }
            }
            grid[i][j] = c
        }
        dfs(0, 1, 0)

        return longest
    }

    fun part2(input: List<String>): Int {
        // got onto the leaderboard by pure luck by printing the current longest hike while the program was running :D
        // noticed intersections and optimized it so that it doesn't take forever now
        val deltas = arrayOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

        val intersections = mutableListOf(Pair(0, 1), Pair(input.lastIndex, input[0].lastIndex - 1))
        val idx = Array(input.size) { IntArray(input[0].length) { -1 } }
        idx[0][1] = 0
        idx[input.lastIndex][input[0].lastIndex - 1] = 1
        for (i in 1 ..< input.lastIndex) {
            for (j in 1 ..< input[0].lastIndex) {
                if (input[i][j] != '#' &&
                    deltas.sumOf { (di, dj) -> (if (input[i + di][j + dj] != '#') 1 else 0).toInt() } > 2) {
                    idx[i][j] = intersections.size
                    intersections.add(Pair(i, j))
                }
            }
        }

        val adj: Array<MutableList<Pair<Int, Int>>> = Array(intersections.size) { mutableListOf() }
        for ((k, p) in intersections.withIndex()) {
            val (pi, pj) = p
            idx[pi][pj] = -2

            for ((pdi, pdj) in deltas) {
                var i = pi + pdi
                var j = pj + pdj
                if (i !in input.indices || j !in input[0].indices || input[i][j] == '#' || idx[i][j] != -1)
                    continue

                var steps = 1
                while (idx[i][j] == -1) {
                    idx[i][j] = -2
                    for ((di, dj) in deltas) {
                        val ni = i + di
                        val nj = j + dj
                        if (ni in input.indices && nj in input[0].indices &&
                            input[ni][nj] != '#' && idx[ni][nj] != -2) {
                            i = ni
                            j = nj
                            break
                        }
                    }
                    steps += 1
                }

                adj[k].add(Pair(idx[i][j], steps))
                adj[idx[i][j]].add(Pair(k, steps))
            }

            idx[pi][pj] = k
        }

        val vis = BooleanArray(intersections.size)
        var longest = 0
        fun dfs(i: Int, steps: Int) {
            if (vis[i]) return
            if (i == 1) {
                longest = max(longest, steps)
                return
            }
            vis[i] = true
            for ((j, s) in adj[i]) dfs(j, steps + s)
            vis[i] = false
        }
        dfs(0, 0)

        return longest
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}