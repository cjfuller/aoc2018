package io.cjf.aoc2018

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day7P1Test : StringSpec({
    "given example" {
        parseDay7Input("""
            Step C must be finished before step A can begin.
            Step C must be finished before step F can begin.
            Step A must be finished before step B can begin.
            Step A must be finished before step D can begin.
            Step B must be finished before step E can begin.
            Step D must be finished before step E can begin.
            Step F must be finished before step E can begin.
        """.trimIndent())
            .let(::populateGraph)
            .let(::traverseGraphInOrder)
            .joinToString("") shouldBe "CABDFE"
    }
})
