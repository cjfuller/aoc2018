package io.cjf.aoc2018

import java.util.ArrayDeque
import java.util.Queue

data class LicenseNode(
    val children: List<LicenseNode>,
    val metadata: List<Int>
) {
    fun nodeValue(): Int = if (children.isEmpty()) {
        metadata.sum()
    } else {
        metadata.map { index ->
            if (index > 0 && index <= children.size) {
                children[index - 1].nodeValue()
            } else {
                0
            }
        }.sum()
    }
}

fun parseDay8Input(): List<Int> =
    readInputResource("day8")
        .lines()
        .first()
        .split(' ')
        .map { it.toInt() }

fun makeTree(inputs: Queue<Int>): LicenseNode {
    val childCount = inputs.poll()
    val metaCount = inputs.poll()
    val children = (0 until childCount).map {
        makeTree(inputs)
    }
    val meta = (0 until metaCount).map { inputs.poll() }
    return LicenseNode(children, meta)
}

fun sumMetadata(node: LicenseNode): Int =
    node.metadata.sum() + node.children.map(::sumMetadata).sum()

fun solveDay8P1() =
    ArrayDeque(parseDay8Input())
        .let(::makeTree)
        .let(::sumMetadata)

fun solveDay8P2() =
    ArrayDeque(parseDay8Input())
        .let(::makeTree)
        .nodeValue()
