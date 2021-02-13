package com.example.notifier

import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


data class OneTimeReminder(val id: Int, val start: ZonedDateTime)

data class DailyReminder(
    val id: Int,
    val start: ZonedDateTime,
    val end: ZonedDateTime = ZonedDateTime.now().withYear(999_999),
    val repeatEvery: Long = 1L,
    val onDays: Set<DayOfWeek> = DayOfWeek.values().toSet()
) {
    init {
        require(repeatEvery > 0L) { "Repeat interval must be positive" }
        require(onDays.contains(start.dayOfWeek)) { "Start day must be a valid day" }
    }

    private fun withinBounds(dt: ZonedDateTime): Boolean {
        return !start.isAfter(dt) && !end.isBefore(dt)
    }

    private fun nextPossible(after: ZonedDateTime): ZonedDateTime {
        if (after.isBefore(start)) {
            return start
        }
        val periodsGoneBy = ChronoUnit.DAYS.between(start, after) / repeatEvery
        return start.plusDays((periodsGoneBy + 1L) * repeatEvery)
    }

    private fun validDays(startingWith: ZonedDateTime): Sequence<ZonedDateTime> {
        return generateSequence(startingWith) { it.plusDays(repeatEvery) }
            .takeWhile { dt -> withinBounds(dt) }
            .filter { dt -> onDays.contains(dt.dayOfWeek) }
    }

    fun nextOccurrence(after: ZonedDateTime = ZonedDateTime.now()): ZonedDateTime? {
        return validDays(startingWith = nextPossible(after)).firstOrNull()
    }
}

data class MonthlyReminder(
    val id: Int,
    val start: ZonedDateTime,
    val end: ZonedDateTime = ZonedDateTime.now().withYear(999_999),
    val repeatEvery: Long = 1L
) {
    init {
        require(repeatEvery > 0L) { "Repeat interval must be positive" }
    }

    fun nextOccurrence(after: ZonedDateTime = ZonedDateTime.now()): ZonedDateTime? {
        if (after.isBefore(start)) {
            return start
        }
        val periodsGoneBy = ChronoUnit.MONTHS.between(start, after) / repeatEvery
        val occurrence = start.plusMonths((periodsGoneBy + 1L) * repeatEvery)
        return occurrence?.takeUnless { end.isBefore(occurrence) }
    }
}
