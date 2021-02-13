package com.example.notifier.unit

import com.example.notifier.Reminder
import org.junit.Test

import org.junit.Assert.*
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime


/**
 * These tests are more for confirming my understanding of calendar math than they are for
 * confirming functionality of my code.
 */
class CalendarMathTest {
    private val daily = Reminder(1, hfxTime(2021, 1, 1, 7, 0), Period.ofDays(1))
    private val once = Reminder(2, hfxTime(2021, 3, 5, 16, 30))

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val now = hfxTime(2019, 2, 4, 14, 0)
        val expectedNextOccurrence = hfxTime(2021, 1, 1, 7, 0)

        assertEquals(expectedNextOccurrence, daily.occurrenceAfter(now))
    }

}
