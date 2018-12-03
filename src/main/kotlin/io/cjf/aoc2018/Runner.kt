package io.cjf.aoc2018

val puzzleRegistry = mutableMapOf<String, () -> Any>()
fun registerSolverForPuzzle(puzzle: String, solver: () -> Any) {
    if (puzzle in puzzleRegistry) {
        throw IllegalArgumentException(
            "Cannot register another solver for puzzle $puzzle")
    }
    puzzleRegistry[puzzle] = solver
}

fun main(args: Array<String>) {
    registerAll()
    val puzzle = args[0]
    val solution = puzzleRegistry[puzzle]?.invoke()
    println("Solution for $puzzle: $solution")
}

fun registerAll() {
    registerSolverForPuzzle("day1p1", ::solveDay1P1)
    registerSolverForPuzzle("day1p2", ::solveDay1P2)
    registerSolverForPuzzle("day2p1", ::solveDay2P1)
    registerSolverForPuzzle("day2p2", ::solveDay2P2)
    registerSolverForPuzzle("day3p1", ::solveDay3P1)
    registerSolverForPuzzle("day3p2", ::solveDay3P2)
}

