import kotlin.math.*

fun main() {
    data class Rule(val category: Char, val value: Int, val lt: Boolean)

    fun getWorkflows(input: List<String>): Map<String, Pair<List<Pair<Rule, String>>, String>> =
        input.asSequence().takeWhile { it.any() }.map { line ->
            val (name, s) = line.split('{')
            val rules = s.substring(0, s.lastIndex).split(',')
            Pair(
                name,
                Pair(
                    rules.asSequence().take(rules.lastIndex).map {
                        val (rule, go) = it.split(':')
                        Pair(
                            if ('<' in rule) {
                                val (category, value) = rule.split('<')
                                Rule(category[0], value.toInt(), true)
                            } else {
                                val (category, value) = rule.split('>')
                                Rule(category[0], value.toInt(), false)
                            },
                            go
                        )
                    }.toList(),
                    rules.last()
                )
            )
        }.toMap()

    fun part1(input: List<String>): Int {
        val workflows = getWorkflows(input)

        return input.takeLastWhile { it.any() }.sumOf { line ->
            val ratings = line.substring(1, line.lastIndex).split(',').associate {
                val (category, value) = it.split('=')
                Pair(category[0], value.toInt())
            }.toMutableMap()

            var workflow = "in"
            outer@ while (workflow != "A") {
                if (workflow == "R") return@sumOf 0

                val (rules, otherwise) = workflows[workflow]!!
                for ((rule, go) in rules) {
                    val rating = ratings[rule.category]!!
                    if (if (rule.lt) rating < rule.value else rating > rule.value) {
                        workflow = go
                        continue@outer
                    }
                }
                workflow = otherwise
            }

            ratings.values.sum()
        }
    }

    fun part2(input: List<String>): Long {
        val workflows = getWorkflows(input)

        val ratings = mutableMapOf(
            'x' to Pair(1, 4000),
            'm' to Pair(1, 4000),
            'a' to Pair(1, 4000),
            's' to Pair(1, 4000)
        )
        var combinations = 0L
        fun dfs(workflow: String) {
            if (workflow == "A") {
                combinations += ratings.values.fold(1L) { acc, (low, high) ->
                    acc * (high - low + 1)
                }
                return
            }
            if (workflow == "R") return

            val backtrack: MutableMap<Char, Pair<Int, Int>> = mutableMapOf()
            val (rules, otherwise) = workflows[workflow]!!
            for ((rule, go) in rules) {
                val rating = ratings[rule.category]!!
                ratings[rule.category] = if (rule.lt)
                    rating.copy(second = min(rating.second, rule.value - 1))
                else
                    rating.copy(first = max(rating.first, rule.value + 1))
                dfs(go)
                ratings[rule.category] = rating

                backtrack.getOrPut(rule.category) { rating }
                ratings[rule.category] = if (rule.lt)
                    rating.copy(first = max(rating.first, rule.value))
                else
                    rating.copy(second = min(rating.second, rule.value))
            }
            dfs(otherwise)
            ratings.putAll(backtrack)
        }
        dfs("in")

        return combinations
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}