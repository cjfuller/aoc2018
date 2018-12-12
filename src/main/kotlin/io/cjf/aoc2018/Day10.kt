package io.cjf.aoc2018

import kravis.SessionPrefs
import kravis.geomPoint
import kravis.plot
import kravis.render.LocalR
import kravis.themeBW

data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(other: Vector2D) =
        Vector2D(x + other.x, y + other.y)
}

data class Particle(
    val pos: Vector2D,
    val velocity: Vector2D
)

val parserRe = Regex("""position=<([^>]+)> velocity=<([^>]+)>""")

fun parseDay10Input(): List<Particle> =
    readInputResource("day10")
        .lines()
        .filter { it.isNotEmpty() }
        .mapNotNull { parserRe.find(it)?.groupValues?.drop(1) }
        .map { (posTuple, velocityTuple) ->
            val (x, y) = posTuple.split(", ").map { it.trim().toInt() }
            val (vx, vy) = velocityTuple.split(", ").map { it.trim().toInt() }
            Particle(Vector2D(x, y), Vector2D(vx, vy))
        }

fun simOneStep(input: List<Particle>): List<Particle> =
    input.map {
        Particle(it.pos + it.velocity, it.velocity)
    }

fun distanceHeuristic(input: Iterable<Particle>) =
    input.map { it.pos.x * it.pos.x + it.pos.y * it.pos.y }
        .sum()

fun display(input: Iterable<Particle>) {
    input.plot(
        x = { it.pos.x },
        y = { -it.pos.y }
    ).geomPoint().themeBW().show()
}

fun solveDay10P1() {
    SessionPrefs.RENDER_BACKEND = LocalR()
    val input = parseDay10Input()
    val possibleSolutions = sequence {
        val initialDistance = distanceHeuristic(input)
        yield(initialDistance to input)
        var currentPositions = input
        var currentDistance = initialDistance
        while (currentDistance <= initialDistance) {
            currentPositions = simOneStep(currentPositions)
            currentDistance = distanceHeuristic(currentPositions)
            yield(currentDistance to currentPositions)
        }
    }.sortedBy { it.first }
    for (sol in possibleSolutions) {
        println("solution has distance ${sol.first}")
        display(sol.second)
        println("press enter to continue")
        System.`in`.read()
    }
}