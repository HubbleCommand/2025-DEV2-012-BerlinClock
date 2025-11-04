package com.dev2012.berlinclock

import com.dev2012.berlinclock.ui.FakeBerlinClockViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin
import java.time.Instant
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ViewModelUnitTest {
    private val testModule = module {
        //Note that the default timezone / offset of .setTime() is zero, so don't need to set here
        factory { FakeBerlinClockViewModel() }
    }

    @Before
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun seconds() {
        val viewModel: FakeBerlinClockViewModel = getKoin().get()

        viewModel.setTime(Instant.parse("2025-01-01T12:00:00Z"))
        assertTrue(viewModel.uiState.value.seconds)
        viewModel.setTime(Instant.parse("2025-01-01T12:00:02Z"))
        assertTrue(viewModel.uiState.value.seconds)
        viewModel.setTime(Instant.parse("2025-01-01T12:00:01Z"))
        assertFalse(viewModel.uiState.value.seconds)
    }

    @Test
    fun minutes() {
        val viewModel: FakeBerlinClockViewModel = getKoin().get()

        viewModel.setTime(Instant.parse("2025-01-01T12:00:00Z"))
        assertEquals(0, viewModel.uiState.value.minutes)
        assertEquals(0, viewModel.uiState.value.minutesAccumulator)
        viewModel.setTime(Instant.parse("2025-01-01T12:04:00Z"))
        assertEquals(4, viewModel.uiState.value.minutes)
        assertEquals(0, viewModel.uiState.value.minutesAccumulator)
        viewModel.setTime(Instant.parse("2025-01-01T12:34:00Z"))
        assertEquals(4, viewModel.uiState.value.minutes)
        assertEquals(6, viewModel.uiState.value.minutesAccumulator)
    }

    @Test
    fun hours() {
        val viewModel: FakeBerlinClockViewModel = getKoin().get()
        viewModel.setTime(Instant.parse("2025-01-01T00:00:00Z"))
        assertEquals(0, viewModel.uiState.value.hours)
        assertEquals(0, viewModel.uiState.value.hoursAccumulator)
        viewModel.setTime(Instant.parse("2025-01-01T08:00:00Z"))
        assertEquals(3, viewModel.uiState.value.hours)
        assertEquals(1, viewModel.uiState.value.hoursAccumulator)
        viewModel.setTime(Instant.parse("2025-01-01T12:00:00Z"))
        assertEquals(2, viewModel.uiState.value.hours)
        assertEquals(2, viewModel.uiState.value.hoursAccumulator)
    }
}