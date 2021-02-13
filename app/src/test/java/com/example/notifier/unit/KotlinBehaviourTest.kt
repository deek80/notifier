package com.example.notifier.unit

import org.junit.Test
import org.junit.Assert.assertEquals


/**
 * These tests are more for confirming my understanding of calendar math and other
 * libraries/builtins than they are for confirming functionality of my code.
 *
 * In Halifax 2021:
 *   - DST starts March 14 at 2am (becomes 3am)
 *   - DST ends Nov 7 at 2am (becomes 1am)
 */
class KotlinBehaviourTest {
    private val beforeDstStart = hfxTime(2021, 3, 13, 7, 0)
    private val afterDstStart = hfxTime(2021, 3, 14, 7, 0)

    private val beforeDstEnd = hfxTime(2021, 11, 6, 7, 0)
    private val afterDstEnd = hfxTime(2021, 11, 7, 7, 0)

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
        val ambiguous = hfxTime(2021, 3, 14, 2, 0)
        val standard = hfxTime(2021, 3, 14, 3, 0)
        assertEquals("March 14 at 2:00 am gets adjusted to the unambiguous 3:00am", ambiguous, standard)
    }

    @Test
    fun edgeOfDstStart() {
        val before = hfxTime(2021, 3, 14, 1, 59)
        val after = hfxTime(2021, 3, 14, 3, 0)
        assertEquals("2021-03-14: 1:59 refers to before DST starting, 3:00 refers to after DST starting", before.plusMinutes(1), after)
    }

    @Test
    fun edgeOfDstEnd() {
        val before = hfxTime(2021, 11, 7, 1, 59)
        val after  = hfxTime(2021, 11, 7, 2, 0)
        assertEquals("2021-11-07: 1:59 refers to before DST ending, 2:00 refers to after DST ending", before.plusMinutes(61), after)
        // note that there's no great way to refer to the "second 1am"
    }

    @Test
    fun testGenerateSequenceFirstElement() {
        // I just wasn't sure if the first seed value was the first thing yielded, looks like it is
        val first = generateSequence(5) { it * 2 }.first()
        assertEquals(5, first)
    }

    @Test
    fun testGenerateSequenceLastElement() {
        // I just wasn't sure if the first seed value was the first thing yielded, looks like it is
        val last = generateSequence(5){it+1}.takeWhile { it < 8 }.last()
        assertEquals(7, last)
    }
}