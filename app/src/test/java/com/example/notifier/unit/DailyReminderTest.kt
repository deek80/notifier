package com.example.notifier.unit

import com.example.notifier.DailyReminder
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.ZonedDateTime


class DailyReminderTest {
    private val someFriday = hfxTime(2021, 1, 1, 1, 1)
    private val weekdays = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    )

    private val daily = DailyReminder(1, start=someFriday, repeatEvery=1)
    private val weekly = DailyReminder(2, start=someFriday, repeatEvery=7)
    private val biweekly = DailyReminder(3, start=someFriday, repeatEvery=14)
    private val onlyWeekdays = DailyReminder(4, start=someFriday, repeatEvery=1, onDays=weekdays)

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)
        listOf(daily, weekly, biweekly).forEach {
            assertEquals(it.nextOccurrence(inThePast), it.start)
        }
    }

    @Test
    fun nextOccurrenceWithSkippedDays() {
        assertEquals(onlyWeekdays.nextOccurrence(someFriday)!!.dayOfWeek, DayOfWeek.MONDAY)
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
        ).forEach { time ->
            assertEquals(expected, daily.nextOccurrence(time))
        }
    }

    @Test
    fun daysBetweenReminders() {
        mapOf(daily to 1L, weekly to 7L, biweekly to 14L).forEach { (reminder, interval) ->
            val occurrenceOne = reminder.nextOccurrence(ZonedDateTime.now())!!
            val occurrenceTwo = reminder.nextOccurrence(occurrenceOne)
            assertEquals(
                "should be $interval days between occurrences",
                occurrenceOne.plusDays(interval),
                occurrenceTwo
            )
        }
    }

    @Test
    fun endTimeBoundaryIncluded() {
        val reminder = DailyReminder(1, start=someFriday, end=someFriday.plusDays(1))
        val occurrences = generateSequence(someFriday) { reminder.nextOccurrence(it) }.toList()
        assertEquals(listOf(someFriday, someFriday.plusDays(1)), occurrences)
    }

    @Test
    fun endTimeBoundaryIsPrecise() {
        val reminder = DailyReminder(1, start=someFriday, end=someFriday.plusDays(1).minusNanos(1))
        val occurrences = generateSequence(someFriday) { reminder.nextOccurrence(it) }.toList()
        assertEquals(listOf(someFriday), occurrences)
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativeRepeatInterval() {
        DailyReminder(1, ZonedDateTime.now(), repeatEvery = -5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun noOccurrences() {
        DailyReminder(1, someFriday, repeatEvery = 7, onDays = setOf(DayOfWeek.MONDAY))
    }
}
