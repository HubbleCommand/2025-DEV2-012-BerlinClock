package com.dev2012.berlinclock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
//Would use kotlin time, but would need to update kotlin lang version
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Using Instant as this is the modern way to do timing
 */
class FakeBerlinClockViewModel(instant: Instant = Instant.parse("2020-01-01T12:34:56Z")) : BerlinClockUiProvider {
    private val _uiState = MutableStateFlow(BerlinClockUiState.from(instant))
    override val uiState: StateFlow<BerlinClockUiState> = _uiState.asStateFlow()

    /**
     * By default, time offset is Zero
     */
    fun setTime(instant: Instant, timeZone: ZoneOffset = ZoneOffset.UTC) {
        _uiState.value = BerlinClockUiState.from(instant, timeZone)
    }
}

interface BerlinClockUiProvider {
    val uiState: StateFlow<BerlinClockUiState>
}

class BerlinClockViewModel : ViewModel(), BerlinClockUiProvider {
    private val _uiState = MutableStateFlow(BerlinClockUiState.from(Instant.now()))
    override val uiState: StateFlow<BerlinClockUiState> = _uiState.asStateFlow()

    init {
        //I guess I could do https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.concurrent/timer.html
        // but this viewModelScope.launch exists within the VM scope, so is better
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _uiState.value = BerlinClockUiState.from(Instant.now())
            }
        }
    }
}

//Overkill for what it is, but I prefer value classes over boolean flags
@JvmInline
value class On(private val on: Boolean) {
    fun isOn(): Boolean {
        return on
    }
}

data class BerlinClockUiState(
    val seconds: On,
    val hoursRemainder: Int,
    val hoursAccumulator: Int,
    val minutesRemainder: Int,
    val minutesAccumulator: Int,
    val timeString: String
) {
    companion object {
        fun from(
            instant: Instant, timeZone: ZoneId = ZoneId.systemDefault(), formatPattern: String = "HH:mm"
        ): BerlinClockUiState {
            val formatter = DateTimeFormatter.ofPattern(formatPattern).withZone(timeZone)
            val formatted = formatter.format(instant)

            val zoned = instant.atZone(timeZone)
            val seconds = zoned.second % 2 == 0
            val minutes = zoned.minute % 5
            val minutesAccumulator = zoned.minute / 5
            val hours = zoned.hour % 5
            val hoursAccumulator = zoned.hour / 5

            return BerlinClockUiState(
                On(seconds), hours, hoursAccumulator, minutes, minutesAccumulator, formatted
            )
        }
    }
}
