package io.cjf.aoc2018

const val inputNPlayers = 419
const val inputFinalMarbleNumber = 72164

data class GameState(
    val lastNumber: Int,
    val marbles: List<Int>,
    val currentMarbleIndex: Int,
    val scores: List<Int>
) {
    fun nextState(): GameState {
        val nextMarble = lastNumber + 1
        val nextPlayer = (nextMarble - 1) % scores.size + 1
        return when {
            nextMarble % 23 == 0 -> {
                val indexToRemove =
                    (currentMarbleIndex - 7).posMod(marbles.size)
                val valOfRemovedMarble = marbles[indexToRemove]
                GameState(
                    lastNumber = nextMarble,
                    marbles = marbles.removeAt(indexToRemove),
                    currentMarbleIndex = indexToRemove % (marbles.size - 1),
                    scores = scores.addAt(
                        nextPlayer - 1, nextMarble + valOfRemovedMarble)
                )
            }
            else -> {
                val insertAfter =
                    (currentMarbleIndex + 1) % marbles.size
                GameState(
                    lastNumber = nextMarble,
                    marbles = marbles.insertAfter(insertAfter, nextMarble),
                    currentMarbleIndex = insertAfter + 1,
                    scores = scores
                )
            }
        }
    }
}

fun <T> List<T>.removeAt(i: Int): List<T> =
    slice(0 until i) + slice(i+1 until size)

fun <T> List<T>.insertAfter(i: Int, e: T): List<T> =
    slice(0..i) + e + slice(minOf(i + 1, size) until size)

fun List<Int>.addAt(i: Int, toAdd: Int): List<Int> {
    val new = toMutableList()
    new[i] += toAdd
    return new
}

fun Int.posMod(n: Int): Int {
    var new = this
    while (new < 0) { new += n }
    return new % n
}

fun simulateGame(players: Int) = sequence {
    val initialState = GameState(
        lastNumber = 0,
        currentMarbleIndex = 0,
        marbles = listOf(0),
        scores = List(players) { 0 }
    )
    yield(initialState)

    var currState = initialState
    while (true) {
        val nextState = currState.nextState()
        yield(nextState)
        currState = nextState
    }
}

fun simulateToLastMarble(players: Int, lastMarble: Int): GameState =
    simulateGame(players).drop(lastMarble).first()

fun solveDay9P1(): Int =
    simulateToLastMarble(inputNPlayers, inputFinalMarbleNumber).scores.max()
    ?: throw IllegalStateException("Did not find a max score")

fun solveDay9P2(): Int =
    simulateToLastMarble(inputNPlayers, inputFinalMarbleNumber * 100).scores.max()
        ?: throw IllegalStateException("Did not find a max score")
