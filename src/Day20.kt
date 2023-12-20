fun main() {
    data class Modules(
        val map: Map<String, List<String>>,
        val flips: MutableMap<String, Boolean>,
        val cons: MutableMap<String, MutableSet<String>>,
        val conCnts: Map<String, Int>
    )

    fun getModules(input: List<String>): Modules {
        val map: MutableMap<String, List<String>> = mutableMapOf()
        val flips: MutableMap<String, Boolean> = mutableMapOf()
        val cons: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val conCnts: MutableMap<String, Int> = mutableMapOf()

        val seq = input.map {
            val (lhs, rhs) = it.split(" -> ")
            Pair(lhs, rhs.split(", "))
        }.asSequence()

        for ((from, tos) in seq) {
            val module = when (from[0]) {
                '%' -> {
                    val name = from.substring(1)
                    flips[name] = false
                    name
                }
                '&' -> {
                    val name = from.substring(1)
                    cons[name] = mutableSetOf()
                    conCnts[name] = 0
                    name
                }
                else -> from
            }

            map[module] = tos
        }

        for ((from, tos) in seq)
            if (from[0] in "%&")
                for (to in tos)
                    conCnts[to]?.let { conCnts[to] = it + 1 }

        return Modules(map, flips, cons, conCnts)
    }

    fun part1(input: List<String>): Int {
        val (map, flips, cons, conCnts) = getModules(input)

        var lows = 0
        var highs = 0
        repeat(1000) {
            lows += 1
            val queue = ArrayDeque(map["broadcaster"]!!.map {
                Triple(it, "broadcaster", false)
            })
            while (queue.any()) {
                val (module, prev, high) = queue.removeFirst()
                if (high)
                    highs += 1
                else
                    lows += 1

                map[module]?.let { tos ->
                    if (module in flips) {
                        if (!high) {
                            flips[module] = !flips[module]!!
                            for (to in tos)
                                queue.add(Triple(to, module, flips[module]!!))
                        }
                    } else {
                        if (high)
                            cons[module]!!.add(prev)
                        else
                            cons[module]!!.remove(prev)
                        val sendHigh = cons[module]!!.size < conCnts[module]!!
                        for (to in tos)
                            queue.add(Triple(to, module, sendHigh))
                    }
                }
            }
        }

        return lows * highs
    }

    fun part2(input: List<String>): Long {
        val (map, flips, _, _) = getModules(input)

        // took hints from r/adventofcode
        // this helped https://www.reddit.com/r/adventofcode/comments/18mogoy/2023_day_20_visualization_of_the_input_couldnt/
        // assumptions/thoughts:
        // - 4 disjoint parts made from a path of flip-flops and a conjunction
        // - each part reaches a conjunction that happens to be inverted with another conjunction then fed together into
        // a final conjunction going into rx
        // - for rx to receive a low pulse the conjunction for each part have to all produce a low pulse together
        // - each flip-flop has an edge going either in/out from/to the conjunction except the first which has both
        // - for the conjunction to output a low pulse all flip-flops with outward edges have to be on which i will
        // call "important" flip-flops
        // - while the important flip-flops are not all on, the conjunction always produces a high pulse which will be
        // ignored by the flip-flops
        // - think of flip-flops as bits, if there is an outward edge the bit is 1 else 0
        // - each button press increases the integer represented by those bits by 1
        // - so the first time all important flip-flops are on, the non-important flip-flops will be off
        // (smallest integer with all important bits on)
        // - on the press that activates the conjunction, low pulses are sent to the first and all off flip-flops
        // - sending a low pulse to the ith flip-flop is like adding 2^i
        // - important bits plus non-important bits gives 2^n-1 with n as the number of bits which when incremented
        // becomes 0 (unsigned overflow)
        // (eg: 11011001 + 00100110 + 00000001 = 00000000)
        // - since it became 0 we have reached the initial state (all flip-flops off) and this is just a cycle
        // - so the answer is the lcm of cycle lengths of each part (apparently the cycle lengths are all prime so their
        // product would work too)
        return map["broadcaster"]!!.map { start ->
            var cycle = 0
            var bit = 1
            var cur = start
            while (true) {
                val tos = map[cur]!!
                if (tos.size == 2) {
                    cycle = cycle or bit
                    cur = tos[if (tos[0] in flips) 0 else 1]
                } else {
                    if (tos[0] !in flips) break
                    cur = tos[0]
                }
                bit = bit shl 1
            }

            (cycle or bit).toLong()
        }.reduce(Long::times)
    }

    val testInput1 = readInput("Day20_test1")
    val testInput2 = readInput("Day20_test2")
    check(part1(testInput1) == 32000000)
    check(part1(testInput2) == 11687500)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}