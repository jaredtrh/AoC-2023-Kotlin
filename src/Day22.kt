fun main() {
    data class Brick(
        val x0: Int, val y0: Int, val z0: Int,
        val x1: Int, val y1: Int, val z1: Int
    )

    fun getBricks(input: List<String>): List<Brick> =
        input.map {
            val (l, r) = it.split('~')
            var c0 = l.split(',').map(String::toInt)
            var c1 = r.split(',').map(String::toInt)
            if (c1[0] < c0[0] || c1[1] < c0[1] || c1[2] < c0[2])
                c0 = c1.also { c1 = c0 }
            Brick(c0[0], c0[1], c0[2], c1[0], c1[1], c1[2])
        }.sortedBy { it.z0 }

    fun getGraph(bricks: List<Brick>): Pair<Array<MutableList<Int>>, IntArray> {
        val maxX = bricks.maxOf { it.x1 }
        val maxY = bricks.maxOf { it.y1 }
        val height = Array(maxX + 1) { IntArray(maxY + 1) }
        val idx = Array(maxX + 1) { IntArray(maxY + 1) { -1 } }
        val adj: Array<MutableList<Int>> = Array(bricks.size) { mutableListOf() }
        val cnt = IntArray(bricks.size)
        for ((i, brick) in bricks.withIndex()) {
            if (brick.x0 < brick.x1) {
                val mx: MutableSet<Int> = mutableSetOf()
                for (x in brick.x0 .. brick.x1) {
                    if (mx.isEmpty() || height[x][brick.y0] >= height[mx.first()][brick.y0]) {
                        if (mx.isNotEmpty() && height[x][brick.y0] > height[mx.first()][brick.y0])
                            mx.clear()
                        mx.add(x)
                    }
                }

                val seq = mx.asSequence()
                    .map { idx[it][brick.y0] }
                    .filter { it != -1 }
                    .distinct()
                for (j in seq) adj[j].add(i)
                cnt[i] = seq.count()

                val h = height[mx.first()][brick.y0]
                for (x in brick.x0 .. brick.x1) {
                    height[x][brick.y0] = h + 1
                    idx[x][brick.y0] = i
                }
            } else if (brick.y0 < brick.y1) {
                val mx: MutableSet<Int> = mutableSetOf()
                for (y in brick.y0 .. brick.y1) {
                    if (mx.isEmpty() || height[brick.x0][y] >= height[brick.x0][mx.first()]) {
                        if (mx.isNotEmpty() && height[brick.x0][y] > height[brick.x0][mx.first()])
                            mx.clear()
                        mx.add(y)
                    }
                }

                val seq = mx.asSequence()
                    .map { idx[brick.x0][it] }
                    .filter { it != -1 }
                    .distinct()
                for (j in seq) adj[j].add(i)
                cnt[i] = seq.count()

                val h = height[brick.x0][mx.first()] + 1
                for (y in brick.y0 .. brick.y1) {
                    height[brick.x0][y] = h
                    idx[brick.x0][y] = i
                }
            } else {
                if (idx[brick.x0][brick.y0] >= 0) {
                    adj[idx[brick.x0][brick.y0]].add(i)
                    cnt[i] = 1
                }

                height[brick.x0][brick.y0] += brick.z1 - brick.z0 + 1
                idx[brick.x0][brick.y0] = i
            }
        }

        return Pair(adj, cnt)
    }

    fun part1(input: List<String>): Int {
        val (adj, cnt) = getGraph(getBricks(input))
        return adj.sumOf { tos -> (if (tos.all { cnt[it] > 1 }) 1 else 0).toInt() }
    }

    fun part2(input: List<String>): Int {
        val bricks = getBricks(input)
        val (adj, cnt) = getGraph(bricks)

        var fall = -bricks.size
        for (i in bricks.indices) {
            val c = cnt.toMutableList()
            c[i] = 1
            fun dfs(i: Int) {
                c[i] -= 1
                if (c[i] > 0) return
                fall += 1
                for (j in adj[i]) dfs(j)
            }
            dfs(i)
        }
        return fall
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}