package io.cjf.aoc2018

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day9P1Test : StringSpec({
    "insertAfter" {
        listOf(0, 1).insertAfter(0, 2) shouldBe listOf(0, 2, 1)
        listOf(0, 1).insertAfter(1, 2) shouldBe listOf(0, 1, 2)
        listOf(0).insertAfter(0, 1) shouldBe listOf(0, 1)
    }
    "removeAt" {
        listOf(0, 1, 2).removeAt(0) shouldBe listOf(1, 2)
        listOf(0, 1, 2).removeAt(1) shouldBe listOf(0, 2)
        listOf(0, 1, 2).removeAt(2) shouldBe listOf(0, 1)
    }

    "posMod" {
        2.posMod(3) shouldBe 2
        3.posMod(2) shouldBe 1
        (-5).posMod(3) shouldBe 1
    }
    "9, 32" {
        val finalState = simulateToLastMarble(9, 25)
        finalState.scores.max() shouldBe 32
        finalState.marbles shouldBe
            listOf(
                0, 16, 8, 17, 4, 18, 19, 2, 24, 20, 25, 10, 21, 5, 22, 11, 1,
                12, 6, 13, 3, 14, 7, 15
            )
    }
    "10, 1618" {
        simulateToLastMarble(10, 1618).scores.max() shouldBe 8317
    }
    "13, 7999" {
        simulateToLastMarble(13, 7999).scores.max() shouldBe 146373
    }
    "17, 1104" {
        simulateToLastMarble(17, 1104).scores.max() shouldBe 2764
    }
})
