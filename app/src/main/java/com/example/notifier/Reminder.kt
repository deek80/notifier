package com.example.notifier
import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.Period
import java.time.temporal.ChronoUnit

private val allDays = DayOfWeek.values().toSet()

data class Reminder(val id: Int, val start: ZonedDateTime, val period: Period = Period.ZERO, val days: Set<DayOfWeek> = allDays) {
    init {
        require(period.days == 0 || period.toTotalMonths() == 0L) {
            "Repeat period of both both days and months is not supported"
        }
    }

    fun occurrenceAfter(now: ZonedDateTime): ZonedDateTime {
        if (start.isAfter(now) || period == Period.ZERO) {
            return start
        }

        if (period.days != 0) {
            val periodsGoneBy = (ChronoUnit.DAYS.between(start, now) / period.days).toInt()
            return start.plus(period.multipliedBy(periodsGoneBy + 1))
        }

        val periodsGoneBy = (ChronoUnit.MONTHS.between(start, now) / period.toTotalMonths()).toInt()
        return start.plus(period.multipliedBy(periodsGoneBy + 1))
    }
}
/*
use case:
it's 2021-01-30 at 14:29:33. the alarm callback awakens and is told to send notification id=4
  1. send that notification
  2. check if that notification is recurring and schedule the next one

1: easy
2: if recurring: ahhh, here's where the locale comes into play.  if you just count `interval`s from
   `startTime` then you'll get out of sync after a time change. instead, you need to do "calendar math"
   rather than "interval math".

example:
   startTime: 2021-03-13 11:00:00 (March 13 at 11am, time changes March 14 at 2am)
   interval: daily

 wrong way
   next time = startTime + 24h = 2021-03-14 12:00:00     oops!

 right way
   next time = startTime + 1 "day" = 2021-03-14 11:00:00


 */