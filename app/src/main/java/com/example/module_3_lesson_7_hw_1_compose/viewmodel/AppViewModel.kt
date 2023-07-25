package com.example.module_3_lesson_7_hw_1_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private var stopwatchJob: Job? = null
    private val until = 3_599_999L

    init {
        resetStopwatch()
    }

    fun resetStopwatch() {
        _uiState.value = AppUiState(
            currentHours = "00",
            currentMinutes = "00",
            currentSeconds = "00",
            currentMilliseconds = "000",
            stopwatchPaused = false,
            stopwatchCompleted = false
        )
    }

    fun startStopwatch() {
        stopwatchJob?.cancel()
        stopwatchJob = viewModelScope.launch {
            (0..until step 10L).asFlow()
                .onEach {
                    delay(10)
                    val hours = it / 3_600_000
                    val minutes = (it % 3_600_000) / 60_000
                    val seconds = (it % 60_000) / 1000
                    val milliseconds = it % 1000
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentHours = String.format("%02d", hours),
                            currentMinutes = String.format("%02d", minutes),
                            currentSeconds = String.format("%02d", seconds),
                            currentMilliseconds = String.format("%03d", milliseconds)
                        )
                    }
                }
                .catch {  }
                .collect()
        }
    }

    fun stopStopwatch() {
        stopwatchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(stopwatchCompleted = true)
        }
    }
}