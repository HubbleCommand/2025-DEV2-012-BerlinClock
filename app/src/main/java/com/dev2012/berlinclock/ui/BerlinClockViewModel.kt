package com.dev2012.berlinclock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
//Would use kotlin time, but would need to update kotlin lang version
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class BerlinClockViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(BerlinClockUiState.from(Instant.now()))

    //These flows allow for reduced recompositions for BerlinClockElements
    val secondsFlow: StateFlow<On> = _uiState
        .map { state -> state.seconds }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, On(false))
    val minuteRemainderFlow: StateFlow<Int> = _uiState
        .map { state -> state.minutesRemainder }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val minuteAccumulatorFlow = _uiState
        .map { state -> state.minutesAccumulator }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val hoursRemainderFlow = _uiState
        .map { state -> state.hoursRemainder }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val hoursAccumulatorFlow = _uiState
        .map { state -> state.hoursAccumulator }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, -1)
    val stringFlow = _uiState
        .map { state -> state.timeString }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

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
