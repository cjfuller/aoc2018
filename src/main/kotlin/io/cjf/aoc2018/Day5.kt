package io.cjf.aoc2018

import java.lang.IllegalArgumentException
import java.util.ArrayDeque

fun reducePolymer(input: String): String {
    var changed = true
    var currIter = ArrayDeque(input.toList())
    while (changed) {
        changed = false
        val nextIter = ArrayDeque<Char>()
        while(currIter.isNotEmpty()) {
            val c0 = currIter.removeFirst()
            val c1 = if (currIter.isEmpty()) {
                null
            } else {
                currIter.removeFirst()
            }
            when {
                c1 == null -> nextIter.add(c0)
                c0.isUpperCase() && c1.isLowerCase()
                    && c0.toLowerCase() == c1 -> changed = true
                c0.isLowerCase() && c1.isUpperCase()
                    && c1.toLowerCase() == c0 -> changed = true
                else -> {
                    nextIter.add(c0)
                    currIter.addFirst(c1)
                }
            }
        }
        currIter = nextIter
    }
    return currIter.joinToString("")
}

fun solveDay5P1(): Int {
    val input = readInputResource("day5").trim()
    return reducePolymer(input).length
}

fun removeSubunit(input: String, subunit: Char): String {
    if (!subunit.isLowerCase()) throw IllegalArgumentException(
        "Pass a lowercase character")
    return input
        .filterNot { it == subunit }
        .filterNot { it == subunit.toUpperCase() }
}

fun solveDay5P2(): Int {
    val input = readInputResource("day5").trim()
    val allSubunits = input.toLowerCase().toSet()
    val bestSubunit = allSubunits.minBy { subunit ->
        reducePolymer(removeSubunit(input, subunit)).length
    } ?: throw IllegalStateException("Did not find a best subunit.")
    return reducePolymer(removeSubunit(input, bestSubunit)).length
}