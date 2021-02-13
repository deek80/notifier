package com.example.notifier

import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


data class OneTimeReminder(val id: Int, val start: ZonedDateTime)

data class DailyReminder(
    val id: Int,
    val start: ZonedDateTime,
    val repeatEvery: Long = 1L,
    val onDays: Set<DayOfWeek> = DayOfWeek.values().toSet()
) {
    init {
        require(repeatEvery > 0L) { "Repeat interval must be positive" }
        require(repeatEvery % 7L != 0L || onDays.contains(start.dayOfWeek)) { "Must have occurrences" }
    }

    private fun findValidOccurrence(startingWith: ZonedDateTime): ZonedDateTime {
        var candidate = startingWith
        for (i in 1..15) {  // I think 7 is mathematically enough...but :shrug:
            if (onDays.contains(candidate.dayOfWeek)) {
                return candidate
            }
            candidate = candidate.plusDays(repeatEvery)
        }
        throw IllegalStateException("Could not find any occurrences of reminder $id")
    }

    private fun nextPossibleOccurrence(after: ZonedDateTime): ZonedDateTime {
        if (after.isBefore(start)) {
            return start
        }
        val periodsGoneBy = ChronoUnit.DAYS.between(start, after) / repeatEvery
        return start.plusDays((periodsGoneBy + 1L) * repeatEvery)
    }

    fun nextOccurrence(after: ZonedDateTime): ZonedDateTime {
        return findValidOccurrence(nextPossibleOccurrence(after))
    }
}

data class MonthlyReminder(val id: Int, val start: ZonedDateTime, val repeatEvery: Long = 1L) {
    init {
        require(repeatEvery > 0L) { "Repeat interval must be positive" }
    }

    fun nextOccurrence(after: ZonedDateTime): ZonedDateTime {
        if (after.isBefore(start)) {
            return start
        }
        val periodsGoneBy = ChronoUnit.MONTHS.between(start, after) / repeatEvery
        return start.plusMonths((periodsGoneBy + 1L) * repeatEvery)
    }
}
