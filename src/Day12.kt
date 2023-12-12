fun main() {
    fun calc(records: List<Pair<String, List<Int>>>): Long {
        return records.sumOf { (s, g) ->
            val cnt = IntArray(s.length + 1)
            for (i in 1 .. s.length) {
                cnt[i] = cnt[i - 1]
                if (s[i - 1] == '.') cnt[i] += 1
            }

            var dp = LongArray( s.length + 1)
            var idx = s.indexOf('#')
            if (idx == -1) idx = s.length
            dp.fill(1, 0, idx + 1)
            for (sz in g) {
                val dpn = LongArray(s.length + 1)

                if (s[sz - 1] != '#')
                    dpn[sz] = dpn[sz - 1]
                if (cnt[sz] == 0)
                    dpn[sz] += dp[0]
                for (j in sz + 1 .. s.length) {
                    if (s[j - 1] != '#')
                        dpn[j] = dpn[j - 1]
                    if (cnt[j] == cnt[j - sz] && s[j - sz - 1] != '#')
                        dpn[j] += dp[j - sz - 1]
                }

                dp = dpn
            }
            dp.last()
        }
    }

    fun part1(input: List<String>): Long {
        return calc(
            input.map {
                val (s, g) = it.split(' ')
                Pair(s, g.split(',').map(String::toInt))
            }
        )
    }

    fun part2(input: List<String>): Long {
        return calc(
            input.map { line ->
                val (s, g) = line.split(' ')
                Pair(
                    "$s?$s?$s?$s?$s",
                    "$g,$g,$g,$g,$g".split(',').map(String::toInt)
                )
            }
        )
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}