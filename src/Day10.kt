import kotlin.math.*

fun main() {
    fun getStart(input: List<String>): Pair<Int, Int> {
        for ((i, row) in input.withIndex())
            for ((j, c) in row.withIndex())
                if (c == 'S')
                    return Pair(i, j)
        return Pair(0, 0) // unreachable
    }

    fun move(i: Int, j: Int, pi: Int, pj: Int, c: Char): Pair<Int, Int> =
        when (c) {
            '|' -> if (i > pi) Pair(i + 1, j) else Pair(i - 1, j)
            '-' -> if (j > pj) Pair(i, j + 1) else Pair(i, j - 1)
            'L' -> if (j < pj) Pair(i - 1, j) else Pair(i, j + 1)
            'J' -> if (i > pi) Pair(i, j - 1) else Pair(i - 1, j)
            '7' -> if (j > pj) Pair(i + 1, j) else Pair(i, j - 1)
            else -> if (i < pi) Pair(i, j + 1) else Pair(i + 1, j)
        }

    fun isValid(i: Int, j: Int, pi: Int, pj: Int, c: Char): Boolean =
        when (c) {
            '|' -> j == pj
            '-' -> i == pi
            'L' -> i > pi || j < pj
            'J' -> i > pi || j > pj
            '7' -> i < pi || j > pj
            'F' -> i < pi || j < pj
            else -> false
        }

    fun part1(input: List<String>): Int {
        val (si, sj) = getStart(input)
        val grid = input.map { it.toCharArray() }

        var ans = 0
        for (c in "|-LJ7F") {
            grid[si][sj] = c
            var i = si
            var j = sj
            var pi = -1
            var pj = -1
            var steps = 0
            while (true) {
                steps += 1

                val (ni, nj) = move(i, j, pi, pj, grid[i][j])
                if (ni < 0 || ni >= grid.size || nj < 0 || nj >= grid[0].size
                    || !isValid(ni, nj, i, j, grid[ni][nj])) break
                if (ni == si && nj == sj) {
                    ans = max(ans, steps / 2)
                    break
                }
                pi = i
                pj = j
                i = ni
                j = nj
            }
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        val (si, sj) = getStart(input)
        val grid = input.map { it.toCharArray() }

        var maxLoop: MutableList<Pair<Int, Int>> = mutableListOf()
        var maxChar = '.'
        for (c in "|-LJ7F") {
            grid[si][sj] = c

            var i = si
            var j = sj
            var pi = -1
            var pj = -1
            val loop: MutableList<Pair<Int, Int>> = mutableListOf()
            while (true) {
                loop.add(Pair(i, j))

                val (ni, nj) = move(i, j, pi, pj, grid[i][j])
                if (ni < 0 || ni >= grid.size || nj < 0 || nj >= grid[0].size
                    || !isValid(ni, nj, i, j, grid[ni][nj])) break
                if (ni == si && nj == sj) {
                    if (loop.size > maxLoop.size) {
                        maxLoop = loop
                        maxChar = c
                    }
                    break
                }
                pi = i
                pj = j
                i = ni
                j = nj
            }
        }
        grid[si][sj] = maxChar

        val idx = maxLoop.withIndex().minWith { (_, lhs), (_, rhs) ->
            if (lhs.first != rhs.first)
                lhs.first.compareTo(rhs.first)
            else
                lhs.second.compareTo(rhs.second)
        }.index
        if (maxLoop[idx].first < maxLoop[(idx + 1) % maxLoop.size].first)
            maxLoop.reverse()

        maxLoop.add(maxLoop[0])
        for ((p, np) in maxLoop.asSequence().zipWithNext()) {
            grid[p.first][p.second] = when (grid[p.first][p.second]) {
                '|' -> if (np.first < p.first) '*' else '*'
                'L' -> if (np.first < p.first) '.' else '*'
                'J' -> if (np.first < p.first) '*' else '.'
                '7' -> if (np.first > p.first) '.' else '*'
                'F' -> if (np.first > p.first) '*' else '.'
                else -> '.'
            }
        }

        return grid.sumOf { row ->
            var cnt = 0
            var inside = false
            for (c in row) {
                if (c == '*')
                    inside = !inside
                else if (inside)
                    cnt += 1
            }
            cnt
        }
    }

    val testInput1 = readInput("Day10_test1")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
    val testInput5 = readInput("Day10_test5")
    check(part1(testInput1) == 4)
    check(part1(testInput2) == 8)
    check(part2(testInput3) == 4)
    check(part2(testInput4) == 8)
    check(part2(testInput5) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}