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
    registerSolverForPuzzle("day4p1", ::solveDay4P1)
    registerSolverForPuzzle("day4p2", ::solveDay4P2)
    registerSolverForPuzzle("day5p1", ::solveDay5P1)
    registerSolverForPuzzle("day5p2", ::solveDay5P2)
    registerSolverForPuzzle("day6p1", ::solveDay6P1)
    registerSolverForPuzzle("day6p2", ::solveDay6P2)
    registerSolverForPuzzle("day7p1", ::solveDay7P1)
    registerSolverForPuzzle("day7p2", ::solveDay7P2)
    registerSolverForPuzzle("day8p1", ::solveDay8P1)
    registerSolverForPuzzle("day8p2", ::solveDay8P2)
    registerSolverForPuzzle("day9p1", ::solveDay9P1)
    registerSolverForPuzzle("day9p2", ::solveDay9P2)
}

