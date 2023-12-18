fun main() {
    fun calc(plan: Sequence<Pair<Char, Int>>): Long {
        // assume clockwise and never goes backwards
        // this https://en.wikipedia.org/wiki/Shoelace_formula#Trapezoid_formula but rectangles
        var area = 0L
        var border = 0L // 4x actual border area
        var y = 0
        var prevDir = plan.last().first
        for ((dir, meters) in plan) {
            border += meters * 2 - 2
            when (dir) {
                'U' -> {
                    border += if (prevDir == 'L') 3 else 1
                    y += meters
                }
                'D' -> {
                    border += if (prevDir == 'R') 3 else 1
                    y -= meters
                }
                'L' -> {
                    area -= meters.toLong() * y
                    border += if (prevDir == 'D') 3 else 1
                }
                'R' -> {
                    area += meters.toLong() * y
                    border += if (prevDir == 'U') 3 else 1
                }
            }
            prevDir = dir
        }

        return area + border / 4
    }

    fun part1(input: List<String>): Long {
        return calc(input.asSequence().map {
            val (dir, meters, _) = it.split(' ')
            Pair(dir[0], meters.toInt())
        })
    }

    fun part2(input: List<String>): Long {
        return calc(input.asSequence().map {
            Pair(
                when (it[it.length - 2]) {
                    '0' -> 'R'
                    '1' -> 'D'
                    '2' -> 'L'
                    else -> 'U'
                },
                it.substring(it.length - 7, it.length - 2).toInt(16)
            )
        })
    }

    val testInput1 = readInput("Day18_test")
    check(part1(testInput1) == 62L)
    check(part2(testInput1) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}