package com.example.notifier.unit

import org.junit.Test

import org.junit.Assert.*
import java.time.ZoneId
import java.time.ZonedDateTime


/**
 * These tests are more for confirming my understanding of calendar math than they are for
 * confirming functionality of my code.
 */
class DateTimeMathTest {
    private val halifax = ZoneId.of("America/Halifax")
    // in Halifax 2021, DST starts March 14 at 2am (becomes 3am)
    private val beforeDstStart = ZonedDateTime.of(2021, 3, 13, 7, 0, 0, 0, halifax)
    private val afterDstStart = ZonedDateTime.of(2021, 3, 14, 7, 0, 0, 0, halifax)
    // DST ends Nov 7 at 2am (becomes 1am)
    private val beforeDstEnd = ZonedDateTime.of(2021, 11, 6, 7, 0, 0, 0, halifax)
    private val afterDstEnd = ZonedDateTime.of(2021, 11, 7, 7, 0, 0, 0, halifax)

    @Test
    fun calendarMathIgnoresDST() {
        assertEquals("adding days should not have to account for DST", beforeDstStart.plusDays(1), afterDstStart)
        assertEquals("adding days should not have to account for DST", beforeDstEnd.plusDays(1), afterDstEnd)
    }

    @Test
    fun intervalMathObservesDST() {
        assertEquals("adding hours should account for DST", beforeDstStart.plusHours(23), afterDstStart)
        assertEquals("adding hours should account for DST", beforeDstEnd.plusHours(25), afterDstEnd)
    }

    @Test
    fun constructorCorrectsInvalidDateTimes() {
        val ambiguous = ZonedDateTime.of(2021, 3, 14, 2, 0, 0, 0, halifax)
        val standard = ZonedDateTime.of(2021, 3, 14, 3, 0, 0, 0, halifax)
        assertEquals("March 14 at 2:00 am gets adjusted to the unambiguous 3:00am", ambiguous, standard)
    }

    @Test
    fun edgeOfDstStart() {
        val before = ZonedDateTime.of(2021, 3, 14, 1, 59, 0, 0, halifax)
        val after = ZonedDateTime.of(2021, 3, 14, 3, 0, 0, 0, halifax)
        assertEquals("2021-03-14: 1:59 refers to before DST starting, 3:00 refers to after DST starting", before.plusMinutes(1), after)
    }

    @Test
    fun edgeOfDstEnd() {
        val before = ZonedDateTime.of(2021, 11, 7, 1, 59, 0, 0, halifax)
        val after  = ZonedDateTime.of(2021, 11, 7, 2, 0, 0, 0, halifax)
        assertEquals("2021-11-07: 1:59 refers to before DST ending, 2:00 refers to after DST ending", before.plusMinutes(61), after)
    }
}