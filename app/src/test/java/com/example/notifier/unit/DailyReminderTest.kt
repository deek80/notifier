package com.example.notifier.unit

import com.example.notifier.DailyReminder
import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.time.DayOfWeek
import java.time.ZonedDateTime


class DailyReminderTest {
    private val someFriday = hfxTime(2021, 1, 1, 1, 1)
    private val weekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
    private val tuesThurs = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)

    private val daily = DailyReminder(1, someFriday, 1)
    private val weekly = DailyReminder(2, someFriday, 7)
    private val biweekly = DailyReminder(3, someFriday, 14)
    private val onlyWeekdays = DailyReminder(4, someFriday, 1, weekdays)
    private val onlyTuesThurs = DailyReminder(5, someFriday, 1, tuesThurs)

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)
        listOf(daily, weekly, biweekly).forEach {
            assertEquals(it.nextOccurrence(inThePast), it.start)
        }
    }

    @Test
    fun nextOccurrenceFutureReminderRespectsDayFilter() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)
        assertEquals(onlyWeekdays.nextOccurrence(inThePast), onlyWeekdays.start)
        assertEquals(onlyTuesThurs.nextOccurrence(inThePast), onlyTuesThurs.start.plusDays(4))

    }
    @Test
    fun nextDailyOccurrenceFromVariousTimes() {
        val someDay = daily.start.plusDays(25)
        val expected = daily.start.plusDays(26)
        listOf(
          someDay.withHour(8).withMinute(30),
          someDay.withHour(12).withMinute(0),
          someDay.withHour(19).withMinute(35),
          someDay.withHour(23).withMinute(50)
        ).forEach {time ->
            assertEquals(expected, daily.nextOccurrence(time))
        }
    }

    @Test
    fun daysBetweenReminders() {
        mapOf(daily to 1L, weekly to 7L, biweekly to 14L).forEach { (reminder, interval) ->
            val occurrenceOne = reminder.nextOccurrence(ZonedDateTime.now())
            val occurrenceTwo = reminder.nextOccurrence(occurrenceOne)
            assertEquals("should be $interval days between occurrences", occurrenceOne.plusDays(interval), occurrenceTwo)
        }
    }

    @Test(expected=IllegalArgumentException::class)
    fun negativeRepeatInterval() {
        DailyReminder(1, ZonedDateTime.now(), -5)
    }

    @Test(expected=IllegalArgumentException::class)
    fun noOccurrences() {
        DailyReminder(1, someFriday, 7, setOf(DayOfWeek.MONDAY))
    }
}
