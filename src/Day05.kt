import kotlin.math.*

fun main() {
    fun getAllMaps(input: List<String>): List<List<Triple<Long, Long, Long>>> {
        val allMaps: MutableList<List<Triple<Long, Long, Long>>> = mutableListOf()

        var i = 3
        while (i < input.size) {
            val maps: MutableList<Triple<Long, Long, Long>> = mutableListOf()
            while (i < input.size && input[i].isNotEmpty()) {
                val (dst, src, len) = input[i].split(' ').map { it.toLong() }
                maps.add(Triple(dst, src, len))
                i += 1
            }
            maps.sortBy { it.second }
            allMaps.add(maps)
            i += 2
        }

        return allMaps
    }

    fun part1(input: List<String>): Long {
        val allMaps = getAllMaps(input)

        return input[0].split(": ")[1].split(' ').asSequence().map { it.toLong() }.minOf { seed ->
            allMaps.fold(seed) { acc, maps ->
                var idx = maps.binarySearchBy(acc) { it.second + it.third - 1 }
                if (idx < 0) idx = -idx - 1

                if (idx < maps.size && maps[idx].second <= acc)
                    acc - maps[idx].second + maps[idx].first
                else
                    acc
            }
        }
    }

    fun part2(input: List<String>): Long {
        val allMaps = getAllMaps(input)

        return input[0].split(": ")[1].split(' ').asSequence()
            .map { it.toLong() }
            .chunked(2)
            .minOf { (start, segLen) ->
                val segs = mutableListOf(Pair(start, start + segLen))

                for (maps in allMaps) {
                    val newSegs: MutableList<Pair<Long, Long>> = mutableListOf()

                    var i = 0
                    var low = 0L
                    for ((dst, src, len) in maps) {
                        while (i < segs.size && segs[i].second <= src + len) {
                            // non-intersecting
                            val l = max(low, segs[i].first)
                            val r = min(src, segs[i].second)
                            if (l < r) newSegs.add(Pair(l, r))

                            // intersecting
                            val lx = max(src, segs[i].first)
                            val rx = segs[i].second
                            if (lx < rx) newSegs.add(Pair(lx - src + dst, rx - src + dst))

                            i += 1
                        }

                        if (i < segs.size) {
                            // non-intersecting
                            val l = max(low, segs[i].first)
                            val r = min(src, segs[i].second)
                            if (l < r) newSegs.add(Pair(l, r))

                            // intersecting
                            val lx = max(src, segs[i].first)
                            val rx = src + len
                            if (lx < rx) newSegs.add(Pair(lx - src + dst, rx - src + dst))
                        }

                        low = src + len
                    }

                    while (i < segs.size) {
                        val l = max(low, segs[i].first)
                        val r = segs[i].second
                        newSegs.add(Pair(l, r))

                        i += 1
                    }

                    newSegs.sortBy { it.first }
                    segs.clear()
                    segs.add(newSegs[0])

                    // merge intersecting segments
                    for (seg in newSegs) {
                        if (seg.first < segs.last().second)
                            segs[segs.lastIndex] = segs.last().copy(second = max(segs.last().second, seg.second))
                        else
                            segs.add(seg)
                    }
                }

                segs[0].first
            }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
