import java.util.*

fun main() {
    data class Cell(
        val i: Int, val j: Int,
        val di: Int, val dj: Int,
        val c: Int
    )

    val deltas = arrayOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun calc(input: List<String>, mn: Int, mx: Int): Int {
        val pq: PriorityQueue<Pair<Cell, Int>> = PriorityQueue { lhs, rhs ->
            lhs.second.compareTo(rhs.second)
        }
        val dist: MutableMap<Cell, Int> = mutableMapOf()
        val right = Cell(0, 1, 0, 1, 1)
        dist[right] = input[0][1].digitToInt()
        pq.add(Pair(right, dist[right]!!))
        val down = Cell(1, 0, 1, 0, 1)
        dist[down] = input[1][0].digitToInt()
        pq.add(Pair(down, dist[down]!!))

        while (!pq.isEmpty()) {
            val (cell, d) = pq.remove()
            if (d > dist[cell]!!) continue

            for ((di, dj) in deltas) {
                if (cell.i + di !in input.indices || cell.j + dj !in input[0].indices ||
                    di == -cell.di && dj == -cell.dj ||
                    if (di == cell.di && dj == cell.dj) cell.c == mx else cell.c < mn) continue

                val nextCell = cell.copy(
                    i = cell.i + di, j = cell.j + dj,
                    di = di, dj = dj,
                    c = if (di == cell.di && dj == cell.dj) cell.c + 1 else 1
                )
                val nd = d + input[nextCell.i][nextCell.j].digitToInt()
                if (nd < dist.getOrDefault(nextCell, Int.MAX_VALUE)) {
                    dist[nextCell] = nd
                    pq.add(Pair(nextCell, dist[nextCell]!!))
                }
            }
        }

        return deltas.minOf { (di, dj) ->
            (mn .. mx).minOf {
                dist.getOrDefault(Cell(input.lastIndex, input[0].lastIndex, di, dj, it), Int.MAX_VALUE)
            }
        }
    }

    fun part1(input: List<String>): Int {
        return calc(input, 1, 3)
    }

    fun part2(input: List<String>): Int {
        return calc(input, 4, 10)
    }

    val testInput1 = readInput("Day17_test1")
    val testInput2 = readInput("Day17_test2")
    check(part1(testInput1) == 102)
    check(part2(testInput1) == 94)
    check(part2(testInput2) == 71)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}