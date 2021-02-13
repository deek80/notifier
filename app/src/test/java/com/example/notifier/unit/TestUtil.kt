package com.example.notifier.unit

import java.time.ZoneId
import java.time.ZonedDateTime

fun hfxTime(year:Int, month:Int, day:Int, hour:Int, minute:Int): ZonedDateTime {
    return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.of("America/Halifax"))
}
