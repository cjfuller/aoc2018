package io.cjf.aoc2018

import kotlin.math.absoluteValue

typealias Coord = Pair<Int, Int>
typealias Grid = MutableList<MutableList<Int?>>

fun parseDay6Input(): List<Coord> =
    readInputResource("day6")
        .lines()
        .filter { it.isNotEmpty() }
        .map { it.split(", ") }
        .map { (x, y) -> x.toInt() to y.toInt() }

fun createEmptyGrid(input: List<Coord>): Grid {
    val xMax = input.map { it.first }.max()
        ?: throw IllegalStateException("No max x-coord")
    val yMax = input.map { it.second }.max()
        ?: throw IllegalStateException("No max y-coord")
    return MutableList(xMax + 2) { MutableList<Int?>(yMax + 2) { null } }
}

fun manhattanDistance(a: Coord, b: Coord) =
    (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue

fun findClosestCoordIndex(coords: List<Coord>, target: Coord): Int? {
    val distances = coords.withIndex().map { (index, coord) ->
        index to manhattanDistance(coord, target)
    }.groupBy { it.second }
    val minDistance = distances.keys.min()
        ?: throw IllegalStateException("No min distance")
    val closest = distances[minDistance]
        ?: throw IllegalStateException("No coord at min distance")
    return if (closest.size > 1) {
        null
    } else {
        closest[0].first
    }
}

fun layoutGrid(input: List<Coord>, grid: Grid) {
    for (x in 0 until grid.size) {
        for (y in 0 until grid[x].size) {
            grid[x][y] = findClosestCoordIndex(input, x to y)
        }
    }
}

fun discardEdges(grid: Grid) {
    val edgeValues = grid.first().toSet() +
        grid.last().toSet() +
        grid.map { it.first() }.toSet() +
        grid.map { it.last() }.toSet()

    for (x in 0 until grid.size) {
        for (y in 0 until grid[x].size) {
            if (grid[x][y] in edgeValues) {
                grid[x][y] = null
            }
        }
    }
}

fun findMaxArea(grid: Grid): Int =
    grid.flatten()
        .groupBy { it }
        .map { (k, vs) -> k to vs.size }
        .filter { (k, _) -> k != null }
        .toMap()
        .maxBy { (_, count) -> count }
        ?.value
        ?: throw IllegalStateException("No largest area found")

fun solveDay6P1(): Int {
    val input = parseDay6Input()
    return createEmptyGrid(input)
        .also { layoutGrid(input, it) }
        .also(::discardEdges)
        .let(::findMaxArea)
}

fun sumDistancesOnGrid(input: List<Coord>, grid: Grid) {
    for (x in 0 until grid.size) {
        for (y in 0 until grid[x].size) {
            grid[x][y] = input.map { coord -> manhattanDistance(coord, x to y) }
                .sum()
        }
    }
}

fun solveDay6P2(): Int {
    val input = parseDay6Input()
    return createEmptyGrid(input)
        .also { sumDistancesOnGrid(input, it) }
        .flatten()
        .count { it != null && it < 10000 }
}