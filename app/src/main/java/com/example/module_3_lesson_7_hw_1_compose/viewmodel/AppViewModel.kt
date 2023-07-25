package com.example.module_3_lesson_7_hw_1_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private var stopwatchJob: Job? = null
    private val until = 3_599_999L
    private var pause = true
    private var lastValue = 0L

    init {
        resetStopwatch()
    }

    fun resetStopwatch() {
        lastValue = 0L
        pause = true
        _uiState.value = AppUiState(
            currentHours = "00",
            currentMinutes = "00",
            currentSeconds = "00",
            currentMilliseconds = "000",
            stopwatchStarted = false,
            stopwatchCompleted = false
        )
    }

    fun startStopwatch() {
        stopwatchJob?.cancel()
        pause = false
        stopwatchJob = viewModelScope.launch {
            flow {
                for (i in lastValue..until step 10L) {
                    if (pause) {
                        lastValue = i
                        _uiState.update { currenyState ->
                            currenyState.copy(stopwatchStarted = false)
                        }
                        break
                    }
                    emit(i)
                    delay(10)
                }
            }.collect {
                val hours = it / 3_600_000
                val minutes = (it % 3_600_000) / 60_000
                val seconds = (it % 60_000) / 1000
                val milliseconds = it % 1000
                _uiState.update { currentState ->
                    currentState.copy(
                        currentHours = String.format("%02d", hours),
                        currentMinutes = String.format("%02d", minutes),
                        currentSeconds = String.format("%02d", seconds),
                        currentMilliseconds = String.format("%03d", milliseconds),
                        stopwatchStarted = true
                    )
                }
            }
        }
    }

    fun pauseStopwatch() {
        pause = true
        _uiState.update { currentState ->
            currentState.copy(
                stopwatchStarted = false
            )
        }
    }

    fun stopStopwatch() {
        stopwatchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(stopwatchCompleted = true)
        }
    }
}