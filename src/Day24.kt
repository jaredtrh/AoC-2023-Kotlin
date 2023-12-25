import kotlin.math.*

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
        // took hints from r/adventofcode again and learned about the python z3 solver (yeah i cheated :/)
        // p + v * t[i] = p[i] + v[i] * t[i], t[i] >= 0 (not linear!!)
        // but turns out there is a way to make this linear by getting rid of t[i] thanks to a comment
        // all credit to: https://www.reddit.com/r/adventofcode/comments/18pnycy/comment/kepu26z/
        // p - p[i] = (v[i] - v) * t[i] (vector = vector * scalar)
        // so they are parallel vectors whose cross product is the zero vector
        // (p - p[i]) x (v[i] - v) = 0
        // (p.y - p[i].y) * (v[i].z - v.z) - (p.z - p[i].z) * (v[i].y - v.y) = 0
        // (p.y * v[i].z - p.y * v.z - p[i].y * v[i].z + p[i].y * v.z) - (p.z * v[i].y - p.z * v.y - p[i].z * v[i].y + p[i].z * v.y) = 0
        // v[i].z * p.y - v[i].y * p.z - p[i].z * v.y + p[i].y * v.z = p[i].y * v[i].z - p[i].z * v[i].y + p.y * v.z - p.z * v.y
        // still non-linear but we can eliminate the non-linear terms using different hailstones
        // do this for 2 pairs to get 6 equations with 6 unknowns then use gaussian elimination O(n^3)
        val hailstones = getHailstones(input)

        val a = hailstones[0]
        val b = hailstones[1]
        val c = hailstones[2]
        val mat = arrayOf(
            doubleArrayOf(
                0.0, (a.vz - b.vz).toDouble(), (b.vy - a.vy).toDouble(),
                0.0, (b.z - a.z).toDouble(), (a.y - b.y).toDouble(),
                (a.y * a.vz - a.z * a.vy - b.y * b.vz + b.z * b.vy).toDouble()
            ),
            doubleArrayOf(
                (b.vz - a.vz).toDouble(), 0.0, (a.vx - b.vx).toDouble(),
                (a.z - b.z).toDouble(), 0.0, (b.x - a.x).toDouble(),
                (a.z * a.vx - a.x * a.vz - b.z * b.vx + b.x * b.vz).toDouble()
            ),
            doubleArrayOf(
                (a.vy - b.vy).toDouble(), (b.vx - a.vx).toDouble(), 0.0,
                (b.y - a.y).toDouble(), (a.x - b.x).toDouble(), 0.0,
                (a.x * a.vy - a.y * a.vx - b.x * b.vy + b.y * b.vx).toDouble()
            ),
            doubleArrayOf(
                0.0, (a.vz - c.vz).toDouble(), (c.vy - a.vy).toDouble(),
                0.0, (c.z - a.z).toDouble(), (a.y - c.y).toDouble(),
                (a.y * a.vz - a.z * a.vy - c.y * c.vz + c.z * c.vy).toDouble()
            ),
            doubleArrayOf(
                (c.vz - a.vz).toDouble(), 0.0, (a.vx - c.vx).toDouble(),
                (a.z - c.z).toDouble(), 0.0, (c.x - a.x).toDouble(),
                (a.z * a.vx - a.x * a.vz - c.z * c.vx + c.x * c.vz).toDouble()
            ),
            doubleArrayOf(
                (a.vy - c.vy).toDouble(), (c.vx - a.vx).toDouble(), 0.0,
                (c.y - a.y).toDouble(), (a.x - c.x).toDouble(), 0.0,
                (a.x * a.vy - a.y * a.vx - c.x * c.vy + c.y * c.vx).toDouble()
            )
        )

        // gaussian elimination
        for (i in mat.indices) {
            // find non-zero in column and swap it
            for (j in i ..< mat.size) {
                if (mat[j][i] != 0.0) {
                    mat[i] = mat[j].also { mat[j] = mat[i] }
                    break
                }
            }

            // scale to 1
            for (j in mat[0].lastIndex downTo i)
                mat[i][j] /= mat[i][i]

            // make under 0
            for (j in i + 1 ..< mat.size)
                for (k in mat[0].lastIndex downTo i)
                    mat[j][k] -= mat[j][i] * mat[i][k]
        }

        // backwards substitution
        for (i in mat.indices.reversed()) {
            for (j in 0 ..< i) {
                mat[j][mat[0].lastIndex] -= mat[j][i] * mat[i].last()
                mat[j][i] = 0.0 // not necessary but i wanted to print the matrix
            }
        }
        // println(mat.joinToString("\n") { it.joinToString(" ") })

        return mat.asSequence().take(3).sumOf { it.last().roundToLong() }
    }

    val testInput = readInput("Day24_test")
    check(calcIntersections(getHailstones(testInput), 7.0, 27.0) == 2)
    check(part2(testInput) == 47L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}