package io.cjf.aoc2018

fun readInput(): List<String> =
    readInputResource("day2")
        .lines()
        .filter { it.isNotEmpty() }


fun String.anyRepeatedNTimes(n: Int): Boolean =
    this.groupBy { it }
        .values
        .any { it.size == n }


fun checksum(input: List<String>) =
    input.count { it.anyRepeatedNTimes(2) } *
    input.count { it.anyRepeatedNTimes(3) }

fun solveDay2P1() = checksum(readInput())

fun differByOneCharacterAtSamePos(s0: String, s1: String) =
    s0.zip(s1).count { (c0, c1) -> c0 != c1 } == 1

fun findAlmostMatchingStrings(input: List<String>): String {
    for (s0 in input) {
        for (s1 in input) {
            if (differByOneCharacterAtSamePos(s0, s1)) {
                return s0.zip(s1)
                    .filter { (c0, c1) -> c0 == c1 }
                    .map { it.first }
                    .joinToString("")
            }
        }
    }
    throw IllegalStateException("Did not find a matching set of strings.")
}

fun solveDay2P2(): String = findAlmostMatchingStrings(readInput())
