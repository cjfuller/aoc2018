package io.cjf.aoc2018

typealias SubteranneanFarmState = Map<Int, Boolean>

const val rawInitialState = "#..#####.#.#.##....####..##.#.#.##.##.#####..####.#.##.....#..#.#.#...###..#..###.##.#..##.#.#.....#"
val rawRules = """
    .#.## => #
    .###. => #
    #..#. => .
    ...## => .
    #.##. => #
    ....# => .
    ..##. => #
    .##.. => .
    ##..# => .
    .#..# => #
    #.#.# => .
    #.... => .
    .#### => #
    .##.# => .
    ..#.. => #
    ####. => #
    #.#.. => .
    .#... => .
    ###.# => .
    ..### => .
    #..## => #
    ...#. => #
    ..... => .
    ###.. => #
    #...# => .
    ..#.# => #
    ##... => #
    ##.## => .
    ##.#. => .
    ##### => .
    .#.#. => #
    #.### => #
""".trimIndent()
const val ruleSize = 5

typealias Rules = Map<String, Boolean>

fun advanceGeneration(
    state: SubteranneanFarmState,
    rules: Map<String, Boolean>
): SubteranneanFarmState {
    val maxRuleReach = (ruleSize - 1) / 2
    val indexesToCheck =
        state.keys.flatMap { (it - maxRuleReach)..(it + maxRuleReach) }.toSet()
    val nextState = mutableMapOf<Int, Boolean>()
    indexesToCheck.forEach { potNumber ->
        val stateString = ((potNumber - maxRuleReach)..(potNumber + maxRuleReach)).map {
            if (state[it] == true) '#' else '.'
        }.joinToString("")
        val ruleResult = rules[stateString] ?: throw IllegalArgumentException(
            "No rule found for $stateString")
        if (ruleResult) {
            nextState[potNumber] = ruleResult
        }
    }
    return nextState
}

fun parseRules(rules: String): Rules =
    rules.lines().filter { it.isNotEmpty() }
        .map { it.split(" => ") }
        .map { parts ->
            val prevState = parts[0]
            val nextState = parts[1]
            prevState to (nextState == "#")
        }.toMap()

fun printState(state: SubteranneanFarmState) {
    ((state.keys.min() ?: 0)..(state.keys.max() ?: 1)).joinToString("") {
        if (state[it] == true) { "#" } else { "." }
    }.also { println(it) }
}

fun solveDay12(): Int {
    val rules = parseRules(rawRules)
    val initialState = rawInitialState.mapIndexed { i, state ->
        i to (state == '#')
    }.toMap()
    // Enough to get to steady state pattern
    val numIters = 2000L
    var currState = initialState
    (0L until numIters).forEach {
        if (it % 10000 == 0L) { println(it); printState(currState) }
        currState = advanceGeneration(currState, rules)
    }
    printState(currState)
    val oneMore = advanceGeneration(currState, rules)
    val twoMore = advanceGeneration(oneMore, rules)
    // Difference should be the same between each step now.
    println(currState.keys.sum())
    println(oneMore.keys.sum())
    println(twoMore.keys.sum())
    return currState.entries.filter { (_, state) -> state }.map { it.key }.sum()
}