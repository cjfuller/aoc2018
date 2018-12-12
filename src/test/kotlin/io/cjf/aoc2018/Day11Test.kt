package io.cjf.aoc2018

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day11P1Test : StringSpec({
    "power level" {
        powerLevel(Vector2D(3, 5), 8) shouldBe 4
    }
})
