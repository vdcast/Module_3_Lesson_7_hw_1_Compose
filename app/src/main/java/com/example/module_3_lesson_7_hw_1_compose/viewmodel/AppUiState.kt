package com.example.module_3_lesson_7_hw_1_compose.viewmodel

data class AppUiState(
    val currentHours: String = "",
    val currentMinutes: String = "",
    val currentSeconds: String = "",
    val currentMilliseconds: String = "",
    val stopwatchPaused: Boolean = false,
    val stopwatchCompleted: Boolean = false
)
