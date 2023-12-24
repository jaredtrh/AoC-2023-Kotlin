fun main() {
    data class Hailstone(
        val x: Long, val y: Long, val z: Long,
        val vx: Int, val vy: Int, val vz: Int
    )

    fun getHailstones(input: List<String>): List<Hailstone> =
        input.map {
            val (l, r) = it.split(" @ ")
            val (x, y, z) = l.split(", ").map(String::trim).map(String::toLong)
            val (vx, vy, vz) = r.split(", ").map(String::trim).map(String::toInt)
            Hailstone(x, y, z, vx, vy, vz)
        }

    fun calcIntersections(hailstones: List<Hailstone>, low: Double, high: Double): Int {
        var intersections = 0
        for ((i, hi) in hailstones.withIndex()) {
            // y = mi * x + ci
            val mi = hi.vy.toDouble() / hi.vx
            val ci = hi.y - mi * hi.x
            for (hj in hailstones.asSequence().drop(i)) {
                // y = mj * x + cj
                val mj = hj.vy.toDouble() / hj.vx
                val cj = hj.y - mj * hj.x
                // mi * x + ci = mj * x + cj
                // x = (cj - ci) / (mi - mj)
                val x = (cj - ci) / (mi - mj)
                if ((if (hi.vx > 0) x >= hi.x else x <= hi.x) && (if (hj.vx > 0) x >= hj.x else x <= hj.x)) {
                    val y = mi * x + ci
                    if (x in low .. high && y in low .. high)
                        intersections += 1
                }
            }
        }
        return intersections
    }

    fun part1(input: List<String>): Int {
        val hailstones = getHailstones(input)
        return calcIntersections(hailstones, 200000000000000.0, 400000000000000.0)
    }

    fun part2(input: List<String>): Long {
        // took hints from r/adventofcode again and learned about the python z3 solver
        return 0L
    }

    val testInput = readInput("Day24_test")
    check(calcIntersections(getHailstones(testInput), 7.0, 27.0) == 2)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}