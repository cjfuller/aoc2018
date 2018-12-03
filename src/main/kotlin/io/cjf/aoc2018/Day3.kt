package io.cjf.aoc2018

data class Claim(
    val id: Int,
    val left: Int,
    val top: Int,
    val width: Int,
    val height: Int
) {
    fun areaCoords(): Iterable<Pair<Int, Int>> {
        val coords = mutableListOf<Pair<Int, Int>>()
        (this.left until (this.left + this.width)).forEach { x ->
            (this.top until (this.top + this.height)).forEach { y ->
                coords.add(x to y)
            }
        }
        return coords
    }
}

val claimRegex = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

fun parseInput(): List<Claim> =
    readInputResource("day3")
        .lines()
        .filter { it.isNotEmpty() }
        .mapNotNull { claimRegex.find(it) }
        .map { it.groupValues.drop(1).map(Integer::parseInt) }
        .map { (id, left, top, width, height) ->
            Claim(id, left, top, width, height)
        }


fun layoutClaims(claims: List<Claim>): Map<Pair<Int, Int>, Int> {
    val layout: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    claims.forEach { claim ->
        claim.areaCoords().forEach { (x, y) ->
            val coord = x to y
            val currVal = layout[coord] ?: 0
            layout[coord] = currVal + 1
        }
    }
    return layout
}

fun solveDay3P1(): Int {
    val layout = layoutClaims(parseInput())
    return layout.values.count { it > 1 }
}

fun solveDay3P2(): Int {
    val claims = parseInput()
    val layout = layoutClaims(claims)
    claims.forEach { claim ->
        if (claim.areaCoords().all { layout[it] == 1 }) {
            return claim.id
        }
    }
    throw IllegalStateException("Did not find a non-overlapping claim.")
}