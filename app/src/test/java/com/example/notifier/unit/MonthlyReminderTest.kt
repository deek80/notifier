package com.example.notifier.unit

import com.example.notifier.MonthlyReminder
import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.time.ZonedDateTime


/**
 * Defining the behaviour of repeating reminders
 */
class MonthlyReminderTest {
    private val someDay = hfxTime(2021, 1, 1, 1, 1)

    private val monthly = MonthlyReminder(1, someDay, repeatEvery = 1)
    private val bimonthly = MonthlyReminder(2, someDay, repeatEvery = 2)
    private val quarterly = MonthlyReminder(3, someDay, repeatEvery = 3)

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)
        listOf(monthly, bimonthly, quarterly).forEach {
            assertEquals(it.nextOccurrence(inThePast), someDay)
        }
    }

    @Test
    fun nextDailyOccurrenceFromVariousTimes() {
        val day = someDay.plusMonths(17)
        val expected = someDay.plusMonths(18)
        listOf(
          day.withDayOfMonth(2).withHour(8).withMinute(0),
          day.withDayOfMonth(28).withHour(12).withMinute(30),
          day.withDayOfMonth(15).withHour(19).withMinute(15),
          day.withDayOfMonth(9).withHour(23).withMinute(55)
        ).forEach {time ->
            assertEquals(expected, monthly.nextOccurrence(time))
        }
    }

    @Test
    fun monthsBetweenReminders() {
        mapOf(monthly to 1L, bimonthly to 2L, quarterly to 3L).forEach { (reminder, interval) ->
            val occurrenceOne = reminder.nextOccurrence(ZonedDateTime.now())!!
            val occurrenceTwo = reminder.nextOccurrence(occurrenceOne)
            assertEquals("should be $interval months between occurrences", occurrenceOne.plusMonths(interval), occurrenceTwo)
        }
    }

    @Test(expected=IllegalArgumentException::class)
    fun negativeRepeatInterval() {
        MonthlyReminder(1, ZonedDateTime.now(), repeatEvery = -5)
    }
}
