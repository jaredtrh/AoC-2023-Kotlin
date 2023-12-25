fun main() {
    fun part1(input: List<String>): Int {
        // initially got the answer by visualizing with pygraphviz
        // fix a source and enumerate sinks
        // min-cut with ford-fulkerson O(m * F) where F = 3
        // total O(n * m)
        data class Edge(val to: String, var cap: Boolean)

        val compSet: MutableSet<String> = mutableSetOf()
        val edges: MutableList<Pair<String, String>> = mutableListOf()
        for (line in input) {
            val (comp, other) = line.split(": ")
            compSet.add(comp)
            for (otherComp in other.split(' ')) {
                compSet.add(otherComp)
                edges.add(Pair(comp, otherComp))
            }
        }
        val comps = compSet.toList()

        val source = comps[0]
        for (sink in comps.asSequence().drop(1)) {
            val adj: MutableMap<String, MutableList<Edge>> = mutableMapOf()
            for ((u, v) in edges) {
                adj.getOrPut(u) { mutableListOf() }.add(Edge(v, true))
                adj.getOrPut(v) { mutableListOf() }.add(Edge(u, true))
            }

            val vis: MutableSet<String> = mutableSetOf()
            fun dfs(comp: String): Boolean {
                if (comp in vis) return false
                if (comp == sink) return true
                vis.add(comp)

                for (edge in adj[comp]!!) {
                    if (edge.cap && dfs(edge.to)) {
                        edge.cap = false
                        return true
                    }
                }
                return false
            }

            var phases = 0
            while (phases < 3 && dfs(source)) {
                vis.clear()
                phases += 1
            }
            if (phases == 3 && !dfs(source)) {
                vis.clear()
                fun dfsCut(comp: String) {
                    if (comp in vis) return
                    vis.add(comp)

                    for (edge in adj[comp]!!)
                        if (edge.cap)
                            dfsCut(edge.to)
                }
                dfsCut(source)

                return vis.size * (comps.size - vis.size)
            }
        }

        return 0 // unreachable
    }

    fun part2(): String {
        return "MERRY CHRISTMAS!"
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == 54)
    check(part2() == "MERRY CHRISTMAS!")

    val input = readInput("Day25")
    part1(input).println()
    part2().println()
}