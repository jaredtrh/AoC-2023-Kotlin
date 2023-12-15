fun main() {
    fun hash(step: String): Int =
        step.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }

    fun part1(input: List<String>): Int {
        return input[0].split(',').sumOf(::hash)
    }

    fun part2(input: List<String>): Int {
        val boxes: Array<MutableList<Pair<String, Int>>> = Array(256) { mutableListOf() }
        for (step in input[0].split(',')) {
            if (step.last() == '-') {
                val label = step.substring(0, step.lastIndex)
                boxes[hash(label)].removeIf { it.first == label }
            } else {
                val label = step.substring(0, step.lastIndex - 1)
                val box = boxes[hash(label)]

                val idx = box.indexOfFirst { it.first == label }
                if (idx != -1)
                    box[idx] = box[idx].copy(second = step.last().digitToInt())
                else
                    box.add(Pair(label, step.last().digitToInt()))
            }
        }

        return boxes.withIndex().sumOf { (i, box) ->
            box.withIndex().sumOf { (j, lens) ->
                (i + 1) * (j + 1) * lens.second
            }
        }
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}