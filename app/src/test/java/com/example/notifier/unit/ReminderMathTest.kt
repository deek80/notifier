package com.example.notifier.unit

import com.example.notifier.Reminder
import org.junit.Test

import org.junit.Assert.*
import java.time.Period
import java.time.ZonedDateTime


/**
 * Defining the behaviour of repeating reminders
 */
class RepeatingRemindersTest {
    private val daily = Reminder(1, hfxTime(2021, 1, 1, 7, 0), Period.ofDays(1))
    private val weekly = Reminder(1, hfxTime(2021, 2, 5, 15, 0), Period.ofDays(7))

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)
        val expectedFirstOccurrence = daily.start

        assertEquals(expectedFirstOccurrence, daily.occurrenceAfter(inThePast))
    }

    @Test
    fun nextOccurrenceFromVariousTimes() {
        val someDay = daily.start.plusDays(25)
        val expected = daily.start.plusDays(26)
        listOf(
          someDay.withHour(8).withMinute(30),
          someDay.withHour(12).withMinute(0),
          someDay.withHour(19).withMinute(35),
          someDay.withHour(23).withMinute(50)
        ).forEach {time ->
            assertEquals(expected, daily.occurrenceAfter(time))
        }

    }

    @Test
    fun oneDayBetweenDailyReminders() {
        val occurrenceOne = daily.occurrenceAfter(ZonedDateTime.now())
        val occurrenceTwo = daily.occurrenceAfter(occurrenceOne)

        assertEquals(occurrenceOne.plusDays(1), occurrenceTwo)
    }

    @Test
    fun sevenDaysBetweenWeeklyReminders() {
        val occurrenceOne = weekly.occurrenceAfter(ZonedDateTime.now())
        val occurrenceTwo = weekly.occurrenceAfter(occurrenceOne)

        assertEquals(occurrenceOne.plusDays(7), occurrenceTwo)
    }
}
