package com.dev2012.berlinclock


import com.dev2012.berlinclock.ui.BerlinClockUiState
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BerlinClockUiStateTest {

    @Test
    fun seconds() {
        val zeroSeconds = BerlinClockUiState.from(Instant.parse("2025-01-01T12:00:00Z"))
        assertTrue(zeroSeconds.seconds.isOn())
        val positiveSeconds = BerlinClockUiState.from(Instant.parse("2025-01-01T12:00:02Z"))
        assertTrue(positiveSeconds.seconds.isOn())
        val negativeSeconds = BerlinClockUiState.from(Instant.parse("2025-01-01T12:00:01Z"))
        assertFalse(negativeSeconds.seconds.isOn())
    }

    @Test
    fun minutes() {
        val zeroMinutes = BerlinClockUiState.from(Instant.parse("2025-01-01T12:00:00Z"))
        assertEquals(0, zeroMinutes.minutesRemainder)
        assertEquals(0, zeroMinutes.minutesAccumulator)
        val fourMinutes = BerlinClockUiState.from(Instant.parse("2025-01-01T12:04:00Z"))
        assertEquals(4, fourMinutes.minutesRemainder)
        assertEquals(0, fourMinutes.minutesAccumulator)
        val thirtyFourMinutes = BerlinClockUiState.from(Instant.parse("2025-01-01T12:34:00Z"))
        assertEquals(4, thirtyFourMinutes.minutesRemainder)
        assertEquals(6, thirtyFourMinutes.minutesAccumulator)
    }

    @Test
    fun hours() {
        val zeroHours = BerlinClockUiState.from(Instant.parse("2025-01-01T00:00:00Z"), timeZone = ZoneOffset.UTC)
        assertEquals(0, zeroHours.hoursRemainder)
        assertEquals(0, zeroHours.hoursAccumulator)
        val eightHours = BerlinClockUiState.from(Instant.parse("2025-01-01T08:00:00Z"), timeZone = ZoneOffset.UTC)
        assertEquals(3, eightHours.hoursRemainder)
        assertEquals(1, eightHours.hoursAccumulator)
        val twelveHours = BerlinClockUiState.from(Instant.parse("2025-01-01T12:00:00Z"), timeZone = ZoneOffset.UTC)
        assertEquals(2, twelveHours.hoursRemainder)
        assertEquals(2, twelveHours.hoursAccumulator)
    }
}