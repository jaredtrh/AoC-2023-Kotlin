fun main() {
    fun handStrength(handMap: Map<Int, Int>): Int =
        if (6 in handMap) {
            6
        } else if (4 in handMap) {
            5
        } else if (3 in handMap) {
            if (2 in handMap) 4 else 3
        } else if (2 in handMap) {
            if (handMap[2] == 2) 2 else 1
        } else {
            0
        }

    fun calc(seq: Sequence<Pair<List<Int>, Int>>, getHandMap: (List<Int>) -> Map<Int, Int>): Long {
        return seq.sortedWith { lhs, rhs ->
                val lhsStrength = handStrength(getHandMap(lhs.first))
                val rhsStrength = handStrength(getHandMap(rhs.first))

                if (lhsStrength != rhsStrength)
                    return@sortedWith lhsStrength.compareTo(rhsStrength)
                for ((l, r) in lhs.first.zip(rhs.first))
                    if (l != r)
                        return@sortedWith l.compareTo(r)
                0
            }
            .withIndex()
            .sumOf { (i, p) -> (i + 1) * p.second.toLong() }
    }

    fun part1(input: List<String>): Long {
        val labels = "23456789TJQKA"

        return calc(
            input.asSequence().map { it.split(' ') }
                .map { (hand, bid) -> Pair(hand.map { labels.indexOf(it) }, bid.toInt()) }
        ) { hand ->
            hand.groupingBy { it }.eachCount().entries
                .groupingBy { it.value }.eachCount()
        }
    }

    fun part2(input: List<String>): Long {
        val labels = "J23456789TQKA"

        return calc(
            input.asSequence().map { it.split(' ') }
                .map { (hand, bid) -> Pair(hand.map { labels.indexOf(it) }, bid.toInt()) }
        ) { hand ->
            val freq = hand.groupingBy { it }.eachCount().toMutableMap()
            val joker = freq.getOrDefault(0, 0)
            freq.remove(0)
            freq.maxByOrNull { it.value }?.key?.let {
                freq[it] = freq[it]!! + joker
            } ?: run {
                freq[1] = joker
            }
            freq.entries.groupingBy { it.value }.eachCount()
        }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
