package com.example.notifier.unit

import com.example.notifier.Reminder
import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.time.Period
import java.time.ZonedDateTime


/**
 * Defining the behaviour of repeating reminders
 */
class RepeatingRemindersTest {
    private val daily = Reminder(1, hfxTime(2021, 1, 1, 1, 1), Period.ofDays(1))
    private val weekly = Reminder(2, hfxTime(2021, 2, 3, 8, 11), Period.ofDays(7))
    private val monthly = Reminder(3, hfxTime(2021, 3, 5, 15, 21), Period.ofMonths(1))
    private val quarterly = Reminder(4, hfxTime(2021, 4, 7, 22, 31), Period.ofMonths(3))

    @Test
    fun nextOccurrenceIsStartTimeForFutureReminders() {
        val inThePast = hfxTime(2018, 2, 4, 14, 0)

        assertEquals(daily.start, daily.occurrenceAfter(inThePast))
        assertEquals(weekly.start, weekly.occurrenceAfter(inThePast))
        assertEquals(monthly.start, monthly.occurrenceAfter(inThePast))
        assertEquals(quarterly.start, quarterly.occurrenceAfter(inThePast))
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
            assertEquals(expected, daily.occurrenceAfter(time))
        }
    }

    @Test
    fun nextMonthlyOccurrenceFromVariousDays() {
        val someDay = monthly.start.plusMonths(17)
        val expected = monthly.start.plusMonths(18)
        listOf(
          someDay.withDayOfMonth(6).withHour(4).withMinute(35),
          someDay.withDayOfMonth(13).withHour(8).withMinute(17),
          someDay.withDayOfMonth(23).withHour(15).withMinute(9),
          someDay.withDayOfMonth(28).withHour(21).withMinute(55)
        ).forEach {time ->
            assertEquals(expected, monthly.occurrenceAfter(time))
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

    @Test
    fun oneMonthBetweenMonthlyReminders() {
        val occurrenceOne = monthly.occurrenceAfter(ZonedDateTime.now())
        val occurrenceTwo = monthly.occurrenceAfter(occurrenceOne)

        assertEquals(occurrenceOne.plusMonths(1), occurrenceTwo)
    }

    @Test
    fun threeMonthsBetweenQuarterlyReminders() {
        val occurrenceOne = quarterly.occurrenceAfter(ZonedDateTime.now())
        val occurrenceTwo = quarterly.occurrenceAfter(occurrenceOne)

        assertEquals(occurrenceOne.plusMonths(3), occurrenceTwo)
    }

    @Test(expected=IllegalArgumentException::class)
    fun repeatPeriodCanBeOnlyMonthsOrOnlyDays() {
        Reminder(1, ZonedDateTime.now(), Period.of(0, 2, 5))
    }
}
