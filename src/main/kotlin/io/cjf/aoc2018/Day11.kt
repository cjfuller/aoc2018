package io.cjf.aoc2018

const val input = 3214

fun powerLevel(c: Vector2D, serial: Int): Int {
    val rackID = c.x + 10
    val base = (rackID * c.y + serial) * rackID
    val hundredsDigit = ((base % 1000) - (base % 100)) / 100
    return hundredsDigit - 5
}

const val gridSize = 300

fun powerAt(c: Vector2D, squareSize: Int): Int =
    (0 until squareSize).flatMap { xi ->
        (0 until squareSize).map { yi ->
            powerLevel(Vector2D(c.x + xi, c.y + yi), input)
        }
    }.sum()

fun calculateGridPower(squareSize: Int): Collection<Pair<Vector2D, Int>> {
    val calculated = mutableMapOf<Vector2D, Int>()
    for (x in 0..(gridSize - squareSize)) {
        for (y in 0..(gridSize - squareSize)) {
            val coord = Vector2D(x, y)
            val power = when {
                coord.x == 0 && coord.y == 0 -> powerAt(coord, squareSize)
                x == 0 -> {
                    val prev = Vector2D(x, y - 1)
                    val prevResult = calculated[prev]
                        ?: throw IllegalStateException("No previous result")

                    prevResult +
                        (0 until squareSize).map { xi ->
                            powerLevel(Vector2D(x + xi, y + squareSize - 1), input) -
                                powerLevel(Vector2D(x + xi, y - 1), input)
                        }.sum()
                }
                else -> {
                    val prev = Vector2D(x - 1, y)
                    val prevResult = calculated[prev]
                        ?: throw IllegalStateException("No previous result")

                    prevResult +
                        (0 until squareSize).map { yi ->
                            powerLevel(Vector2D(x + squareSize - 1, y + yi), input) -
                                powerLevel(Vector2D(x - 1, y + yi), input)
                        }.sum()
                }
            }
            calculated[coord] = power
        }
    }
    return calculated.map { (c, pow) -> c to pow }
}

fun bestGridPower(squareSize: Int) =
    calculateGridPower(squareSize).maxBy { (_, power) -> power }
        ?: throw IllegalStateException("Did not find a max power")

fun calculateBestSquare() =
    (1..300).map { it to bestGridPower(it) }.maxBy { (_, soln) -> soln.second }
        ?.let { "${it.first}x${it.first} @ ${it.second.first.x}, ${it.second.first.y}" }
        ?: throw IllegalStateException("Did not find a best square")

fun solveDay11P1(): Vector2D = bestGridPower(3).first

fun solveDay11P2(): String = calculateBestSquare()
