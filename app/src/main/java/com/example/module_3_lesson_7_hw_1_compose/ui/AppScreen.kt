package com.example.module_3_lesson_7_hw_1_compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.module_3_lesson_7_hw_1_compose.R
import com.example.module_3_lesson_7_hw_1_compose.ui.theme.Blue50
import com.example.module_3_lesson_7_hw_1_compose.viewmodel.AppViewModel


@Composable
fun MyApp(
    appViewModel: AppViewModel = viewModel()
) {
    val appUiState by appViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_xlarge)),
            text = stringResource(id = R.string.header),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
        Text(
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_small),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            ),
            text = if (appUiState.currentHours == "00") {
                stringResource(
                    id = R.string.time_showing_less_than_hour,
                    appUiState.currentMinutes,
                    appUiState.currentSeconds,
                    appUiState.currentMilliseconds
                )
            } else {
                stringResource(
                    id = R.string.time_showing_more_than_hour,
                    appUiState.currentHours,
                    appUiState.currentMinutes,
                    appUiState.currentSeconds,
                    appUiState.currentMilliseconds
                )
            },
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xxlarge)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = {
                when (appUiState.stopwatchStarted) {
                    true -> appViewModel.pauseStopwatch()
                    false -> appViewModel.startStopwatch()
                }

            }
        ) {
            Text(
                text = stringResource(
                    id = when {
                        !appUiState.stopwatchStarted &&
                                appUiState.currentMilliseconds.toInt() >= 1 -> R.string.button_resume
                        !appUiState.stopwatchStarted -> R.string.button_start
                        else -> R.string.button_pause
                    }
                )
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = { appViewModel.stopStopwatch() }
        ) {
            Text(text = stringResource(id = R.string.button_stop))
        }
    }

    if (appUiState.stopwatchCompleted) {
        AlertDialog(
            onDismissRequest = {
                appViewModel.resetStopwatch()
            },
            title = {
                Text(text = stringResource(id = R.string.alert_title))
            },
            text = {
                Text(
                    stringResource(
                        id = R.string.alert_text, appUiState.currentHours,
                        appUiState.currentMinutes, appUiState.currentSeconds,
                        appUiState.currentMilliseconds
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        appViewModel.resetStopwatch()
                    }
                ) {
                    Text(stringResource(id = R.string.alert_button))
                }
            }
        )
    }
}