package io.cjf.aoc2018

fun inputIntegerCollection(): Collection<Int> =
    readInputResource("day1")
        .lines()
        .filter { it.isNotEmpty() }
        .map { Integer.parseInt(it) }

fun solveDay1P1(): Int = inputIntegerCollection().sum()

fun solveDay1P2(): Int {
    val visitedFrequencies = mutableSetOf<Int>()
    var frequency = 0
    while (true) {
        inputIntegerCollection().forEach {
            visitedFrequencies.add(frequency)
            frequency += it
            if (frequency in visitedFrequencies) {
                return frequency
            }
        }
    }
}