package io.cjf.aoc2018

fun readInputResource(puzzle: String) =
    String::class.java.getResource("/${puzzle}_input.txt").readText()
