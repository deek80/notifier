package com.example.notifier

import java.util.Calendar

enum class Day {
    SU, MO, TU, WE, TH, FR, SA;
}
private val allDays = Day.values().toSet()

// I think I'll need two different interval types...a time interval, and a calendar interval
// for counting things like months or years. then again, maybe 99% of what I want to achieve
// could be done with time intervals. hmm, yeah time intervals (like # of millis) could cover
// half days, days, weeks.  you'd only need something more complicated for alarms like
// "3rd day of every month". and even then you could probably get around that with a bit of
// annoying computation (a daily alarm with allowed_month_days = [3]) and then you'd chug thorough
// like 30 days trying to find the next available one. but that's a weird enough case and you'd
// only have to compute that once per ~30 days.
data class Reminder(val id: Int, val startTime: Long, val interval: Long, val days: Set<Day> = allDays)


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