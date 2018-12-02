package io.cjf.aoc2018

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Day2P1Test : StringSpec({
    "abcdef" {
        "abcdef".anyRepeatedNTimes(2) shouldBe false
        "abcdef".anyRepeatedNTimes(3) shouldBe false
    }
    "bababc" {
        "bababc".anyRepeatedNTimes(2) shouldBe true
        "bababc".anyRepeatedNTimes(3) shouldBe true
    }
    "abbcde" {
        "abbcde".anyRepeatedNTimes(2) shouldBe true
        "abbcde".anyRepeatedNTimes(3) shouldBe false
    }
    "abcccd" {
        "abcccd".anyRepeatedNTimes(2) shouldBe false
        "abcccd".anyRepeatedNTimes(3) shouldBe true
    }
    "aabcdd" {
        "aabcdd".anyRepeatedNTimes(2) shouldBe true
        "aabcdd".anyRepeatedNTimes(3) shouldBe false
    }
    "abcdee" {
        "abcdee".anyRepeatedNTimes(2) shouldBe true
        "abcdee".anyRepeatedNTimes(3) shouldBe false
    }
    "ababab" {
        "ababab".anyRepeatedNTimes(2) shouldBe false
        "ababab".anyRepeatedNTimes(3) shouldBe true
    }
})

class Day2P2Test : StringSpec({
    "differ: abcde,wvxyz" {
        differByOneCharacterAtSamePos("abcde", "wvxyz") shouldBe false
    }

    "differ: fghij,fguij" {
        differByOneCharacterAtSamePos("fghij", "fguij") shouldBe true
    }

    "provided testcase" {
        val testInput = listOf(
            "abcde",
            "fghij",
            "klmno",
            "pqrst",
            "fguij",
            "axcye",
            "wvxyz"
        )

        findAlmostMatchingStrings(testInput) shouldBe "fgij"
    }
})
