import kotlin.math.*

fun main() {
    fun beam(input: List<String>, si: Int, sj: Int, sdi: Int, sdj: Int): Int {
        val vis: Array<Array<Array<BooleanArray>>> =
            Array(input.size) { Array(input[0].length) { Array(3) { BooleanArray(3) } } }
        fun dfs(i: Int, j: Int, di: Int, dj: Int) {
            if (i !in input.indices || j !in input[0].indices || vis[i][j][di + 1][dj + 1]) return
            vis[i][j][di + 1][dj + 1] = true

            when (input[i][j]) {
                '/' -> dfs(i - dj, j - di, -dj, -di)
                '\\' -> dfs(i + dj, j + di, dj, di)
                '|' -> if (di == 0) {
                    dfs(i - 1, j, -1, 0)
                    dfs(i + 1, j, 1, 0)
                } else {
                    dfs(i + di, j, di, 0)
                }
                '-' -> if (dj == 0) {
                    dfs(i, j - 1, 0, -1)
                    dfs(i, j + 1, 0, 1)
                } else {
                    dfs(i, j + dj, 0, dj)
                }
                else -> dfs(i + di, j + dj, di, dj)
            }
        }
        dfs(si, sj, sdi, sdj)

        return vis.sumOf { row ->
            row.sumOf { a ->
                (if (a.any { b -> b.any { it } }) 1 else 0).toInt()
            }
        }
    }

    fun part1(input: List<String>): Int {
        return beam(input, 0, 0, 0, 1)
    }

    fun part2(input: List<String>): Int {
        return max(
            input.indices.maxOf {
                max(
                    beam(input, it, 0, 0, 1),
                    beam(input, it, input.lastIndex, 0, -1)
                )
            },
            input[0].indices.maxOf {
                max(
                    beam(input, 0, it, 1, 0),
                    beam(input, input[0].lastIndex, it, -1, 0)
                )
            }
        )
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}