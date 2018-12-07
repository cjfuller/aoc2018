package io.cjf.aoc2018

import java.lang.IllegalStateException
import java.util.PriorityQueue
import java.util.Queue

val parseRe = Regex(
    """Step ([^\s]) must be finished before step ([^\s]) can begin""")

fun parseDay7Input(input: String): List<Pair<String, String>> =
    input
        .lines()
        .filter { it.isNotEmpty() }
        .mapNotNull { parseRe.find(it) }
        .map { it.groupValues }
        .map { it[1] to it[2] }

data class InstructionNode(
    val id: String,
    val priorSteps: MutableList<String> = mutableListOf(),
    val nextSteps: MutableList<String> = mutableListOf()
) : Comparable<InstructionNode> {
    override fun compareTo(other: InstructionNode) =
        this.id.compareTo(other.id)
}

fun populateGraph(
    input: List<Pair<String, String>>
): Map<String, InstructionNode> {
    val nodes = mutableMapOf<String, InstructionNode>()
    for ((upstream, downstream) in input) {
        if (upstream !in nodes) { nodes[upstream] = InstructionNode(upstream) }
        if (downstream !in nodes) { nodes[downstream] = InstructionNode(downstream) }

        nodes[upstream]?.nextSteps?.add(downstream)
        nodes[downstream]?.priorSteps?.add(upstream)
    }
    return nodes
}

fun readyNodes(nodes: Map<String, InstructionNode>) =
    nodes.filter { it.value.priorSteps.isEmpty() }.keys

fun traverseGraphInOrder(
    nodes: Map<String, InstructionNode>
): Sequence<String> = sequence {
    val queue = PriorityQueue<String>()
    val nodesCopy = nodes.toMutableMap()
    queue.addAll(readyNodes(nodesCopy))
    while (queue.isNotEmpty()) {
        val currNode = queue.poll()
        yield(currNode)
        val nextSteps = nodesCopy[currNode]?.nextSteps
            ?: throw IllegalStateException(
                "Ended up without a node for $currNode")
        for (node in nextSteps) {
            nodesCopy[node]?.priorSteps?.remove(currNode)
        }
        nodesCopy.remove(currNode)
        val nextNodes = readyNodes(nodesCopy).filter { it !in queue }
        queue.addAll(nextNodes)
    }
}

fun solveDay7P1(): String = parseDay7Input(readInputResource("day7"))
    .let(::populateGraph)
    .let(::traverseGraphInOrder)
    .joinToString("")

data class Task(
    val node: InstructionNode,
    val startTime: Int
) : Comparable<Task> {
    private val duration = baseTime + 'A'.until(node.id[0]).toList().size + 1
    val endTime = startTime + duration
    override fun compareTo(other: Task): Int {
        val timesCompare = this.endTime.compareTo(other.endTime)
        return if (timesCompare == 0) {
            this.node.compareTo(other.node)
        } else {
            timesCompare
        }
    }

    companion object {
        const val baseTime = 60
    }
}

const val numWorkers = 5

fun enqueueReadyNodes(
    nodes: MutableMap<String, InstructionNode>, queue: Queue<InstructionNode>
) {
    val ready = readyNodes(nodes)
    queue.addAll(ready.map { nodes[it] })
    for (node in ready) { nodes.remove(node) }
}

fun traverseGraphTimed(
    nodes: Map<String, InstructionNode>
): Int {
    var time = 0
    val active = PriorityQueue<Task>()
    val waiting = PriorityQueue<InstructionNode>()
    val nodesCopy = nodes.toMutableMap()
    enqueueReadyNodes(nodesCopy, waiting)
    while (active.isNotEmpty() || waiting.isNotEmpty() || nodesCopy.isNotEmpty()) {
        when {
            readyNodes(nodesCopy).isNotEmpty() -> {
                enqueueReadyNodes(nodesCopy, waiting)
            }
            active.peek()?.endTime == time -> {
                val nextTaskToFinish = active.poll()
                nextTaskToFinish.node.nextSteps.forEach {
                    nodesCopy[it]?.priorSteps?.remove(nextTaskToFinish.node.id)
                }
            }
            active.size < numWorkers && waiting.isNotEmpty() -> {
                val next = waiting.poll()
                active.add(Task(next, time))
            }
            else -> {
                val nextTaskToFinish = active.poll()
                assert(time <= nextTaskToFinish.endTime) { "Time out of sync" }
                time = nextTaskToFinish.endTime
                nextTaskToFinish.node.nextSteps.forEach {
                    nodesCopy[it]?.priorSteps?.remove(nextTaskToFinish.node.id)
                }
            }
        }
    }
    return time
}

fun solveDay7P2(): Int = parseDay7Input(readInputResource("day7"))
    .let(::populateGraph)
    .let(::traverseGraphTimed)
