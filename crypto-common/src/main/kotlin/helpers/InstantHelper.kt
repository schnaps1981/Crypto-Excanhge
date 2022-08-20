package helpers

import kotlinx.datetime.*
import java.time.temporal.ChronoUnit

private val INSTANT_NONE = Instant.fromEpochMilliseconds(0)//parse("1970-01-01T00:00:00Z")

val Instant.Companion.NONE
    get() = INSTANT_NONE

val Instant.Companion.TimeZone
    get() = kotlinx.datetime.TimeZone.UTC

fun Instant.toBeginDay(): Instant {
    return toLocalDateTime(Instant.TimeZone).date.atStartOfDayIn(Instant.TimeZone)
}

fun Instant.toEndDay(): Instant {
    return toLocalDateTime(Instant.TimeZone).date.plus(DatePeriod(days = 1)).atStartOfDayIn(Instant.TimeZone)
}

val Instant.Companion.nowMicros: Instant
    get() = Clock.System.now().toJavaInstant().truncatedTo(ChronoUnit.MICROS).toKotlinInstant()
