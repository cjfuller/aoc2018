package io.cjf.aoc2018

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class GuardEventType {
    BEGIN,
    SLEEP,
    WAKE
}

sealed class Event {
    abstract val ts: LocalDateTime
    abstract val type: GuardEventType
}

data class UnknownGuardEvent(
    override val ts: LocalDateTime,
    override val type: GuardEventType
) : Event()

data class GuardEvent(
    val guardID: Int,
    override val ts: LocalDateTime,
    override val type: GuardEventType
) : Event()

val beginRE = Regex("""\[(?<ts>[^]]+)] Guard #(?<id>\d+) begins shift""")
val sleepRE = Regex("""\[(?<ts>[^]]+)] falls asleep""")
val wakeRE = Regex("""\[(?<ts>[^]]+)] wakes up""")

fun parseByRegex(
    line: String, eventType: GuardEventType, regex: Regex
): Event? {
    val groups = regex.find(line)?.groups ?: return null
    // TODO(colin): is there a way to check if the regex has a group called id?
    // (this shouldn't use the event type).
    val id = if (eventType == GuardEventType.BEGIN) {
        groups["id"]?.value?.let { Integer.parseInt(it) }
    } else {
        null
    }
    val ts = (
        groups["ts"] ?: throw IllegalArgumentException("Must have a ts group"))
        .value
        .let {
            LocalDateTime.parse(
                it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        }

    return if (id != null) {
        GuardEvent(id, ts, eventType)
    } else {
        UnknownGuardEvent(ts, eventType)
    }
}

fun normalizeEvents(rawEvents: List<Event>): List<GuardEvent> {
    var currGuard: Int? = null
    return rawEvents.sortedBy { it.ts }
        .map {
            when (it) {
                is GuardEvent -> {
                    currGuard = it.guardID
                    it
                }
                is UnknownGuardEvent -> {
                    val guardAssignment = currGuard
                        ?: throw IllegalStateException(
                            "Could not assign event $it to a guard.")
                    GuardEvent(guardAssignment, it.ts, it.type)
                }
            }
        }
}

fun parseDay4Input(): List<Event> = readInputResource("day4")
        .lines()
        .filter { it.isNotEmpty() }
        .map {
            parseByRegex(it, GuardEventType.BEGIN, beginRE)
            ?: parseByRegex(it, GuardEventType.SLEEP, sleepRE)
            ?: parseByRegex(it, GuardEventType.WAKE, wakeRE)
            ?: throw IllegalArgumentException(
                "Line $it did not match any expected event type."
            )
        }


data class Nap(
    val startTs: LocalDateTime,
    val durationMin: Int
)

fun getNaps(events: Iterable<Event>): Sequence<Nap> {
    var lastNapStart: LocalDateTime? = null
    return sequence {
        for (event in events) {
            when (event.type) {
                GuardEventType.BEGIN -> Unit
                GuardEventType.SLEEP -> {
                    if (lastNapStart != null) {
                        throw IllegalStateException(
                            "Fell asleep again while asleep at ${event.ts}")
                    }
                    lastNapStart = event.ts
                }
                GuardEventType.WAKE -> {
                    val start = lastNapStart
                        ?: throw IllegalStateException(
                            "Woke up without having fallen asleep")
                    val duration =
                        start.until(event.ts, ChronoUnit.MINUTES).toInt()
                    yield(Nap(start, duration))
                    lastNapStart = null
                }
            }
        }
    }
}

fun timeAsleep(events: Iterable<Event>): Int = getNaps(events)
    .map { it.durationMin }
    .sum()

fun calculateSleepByMinute(events: Iterable<Event>): List<Int> {
    val sleepInstancesByMinute = MutableList(60) { 0 }
    val naps = getNaps(events)
    for (nap in naps) {
        val end = nap.startTs.minute + nap.durationMin
        (nap.startTs.minute until end).forEach {
            sleepInstancesByMinute[it % 60] += 1
        }
    }
    return sleepInstancesByMinute
}

fun mostSleepyMinute(events: Iterable<Event>): Int {
    val sleepInstancesByMinute = calculateSleepByMinute(events)
    val mostSleepTime = sleepInstancesByMinute.max()
    return sleepInstancesByMinute.indexOfFirst { it == mostSleepTime }
}

fun solveDay4P1(): Int {
    val eventStream = normalizeEvents(parseDay4Input())
    val eventsByGuardID = eventStream
        .groupBy { it.guardID }
    val timeAsleepByGuard = eventsByGuardID
        .map { (id, events) ->
            id to timeAsleep(events)
        }.toMap()
    val mostSleepyGuard = timeAsleepByGuard.maxBy { (_, sleepTime) ->
        sleepTime
    }?.key ?: throw IllegalStateException("Could not find most sleepy guard")
    val mostSleepyGuardEvents = eventsByGuardID[mostSleepyGuard]
        ?: throw IllegalStateException("Could not find sleepy guard events")
    return mostSleepyGuard * mostSleepyMinute(mostSleepyGuardEvents)
}

fun solveDay4P2(): Int {
    val eventStream = normalizeEvents(parseDay4Input())
    val eventsByGuardID = eventStream
        .groupBy { it.guardID }
    val sleepyMinutesByGuard = eventsByGuardID
        .map { (id, events) ->
            id to calculateSleepByMinute(events)
        }.toMap()

    val identifiedGuard = sleepyMinutesByGuard.maxBy { (id, sleepyMinutes) ->
        sleepyMinutes.max() ?: -1
    } ?: throw IllegalStateException("Could not find most sleepy guard")
    val identifiedMinute = sleepyMinutesByGuard[identifiedGuard.key]
        ?.indexOfFirst { it == identifiedGuard.value.max() }
        ?: throw IllegalStateException("Could not identify most sleepy minute")
    return identifiedGuard.key * identifiedMinute
}