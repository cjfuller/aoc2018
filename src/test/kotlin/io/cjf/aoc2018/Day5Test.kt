package io.cjf.aoc2018

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day5P1Test : StringSpec({
    "given example" {
        reducePolymer("dabAcCaCBAcCcaDA") shouldBe "dabCBAcaDA"
    }
})
