package dev.anilbeesetti.nextplayer.settings.screens.gesture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.anilbeesetti.nextplayer.core.common.extensions.toString
import dev.anilbeesetti.nextplayer.core.model.DoubleTapGesture
import dev.anilbeesetti.nextplayer.core.model.PlayerPreferences
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.components.ListSectionTitle
import dev.anilbeesetti.nextplayer.core.ui.components.NextTopAppBar
import dev.anilbeesetti.nextplayer.core.ui.components.PreferenceSlider
import dev.anilbeesetti.nextplayer.core.ui.components.PreferenceSwitch
import dev.anilbeesetti.nextplayer.core.ui.components.PreferenceSwitchWithDivider
import dev.anilbeesetti.nextplayer.core.ui.components.RadioTextButton
import dev.anilbeesetti.nextplayer.core.ui.components.rememberTvListFocusRequester
import dev.anilbeesetti.nextplayer.core.ui.components.tvFocusDown
import dev.anilbeesetti.nextplayer.core.ui.components.tvListFocus
import dev.anilbeesetti.nextplayer.core.ui.designsystem.NextIcons
import dev.anilbeesetti.nextplayer.core.ui.preview.DayNightPreview
import dev.anilbeesetti.nextplayer.core.ui.theme.NextPlayerTheme
import dev.anilbeesetti.nextplayer.settings.composables.OptionsDialog
import dev.anilbeesetti.nextplayer.settings.extensions.name
import kotlin.math.roundToInt

@Composable
fun GesturePreferencesScreen(
    viewModel: GesturePreferencesViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GesturePreferencesScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GesturePreferencesScreenContent(
    state: GesturePreferencesUiState,
    onAction: (GesturePreferencesUiEvent) -> Unit,
) {
    val listFocusRequester = rememberTvListFocusRequester()
    Scaffold(
        topBar = {
            NextTopAppBar(
                title = stringResource(id = R.string.gestures),
                navigationIcon = {
                    FilledTonalIconButton(onClick = { onAction(GesturePreferencesUiEvent.NavigateUp) }, modifier = Modifier.tvFocusDown(listFocusRequester)) {
                        Icon(
                            imageVector = NextIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_up),
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .tvListFocus(listFocusRequester)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            ListSectionTitle(text = stringResource(id = R.string.gestures))
            Column(
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                PreferenceSwitch(
                    title = stringResource(id = R.string.seek_gesture),
                    description = stringResource(id = R.string.seek_gesture_description),
                    icon = NextIcons.SwipeHorizontal,
                    isChecked = state.preferences.useSeekControls,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleUseSeekControls) },
                    isFirstItem = true,
                )
                PreferenceSlider(
                    title = stringResource(R.string.seek_gesture_sensitivity),
                    description = state.preferences.seekSensitivity.toString(decimalPlaces = 2),
                    icon = NextIcons.Sensitivity,
                    enabled = state.preferences.useSeekControls,
                    value = state.preferences.seekSensitivity,
                    valueRange = 0.1f..2.0f,
                    onValueChange = { onAction(GesturePreferencesUiEvent.UpdateSeekSensitivity(it)) },
                    trailingContent = {
                        FilledIconButton(
                            enabled = state.preferences.useSeekControls,
                            onClick = { onAction(GesturePreferencesUiEvent.UpdateSeekSensitivity(PlayerPreferences.DEFAULT_SEEK_SENSITIVITY)) },
                        ) {
                            Icon(
                                imageVector = NextIcons.History,
                                contentDescription = stringResource(id = R.string.reset_seek_sensitivity),
                            )
                        }
                    },
                )
                PreferenceSwitch(
                    title = stringResource(id = R.string.brightness_gesture),
                    description = stringResource(id = R.string.brightness_gesture_description),
                    icon = NextIcons.SwipeVertical,
                    isChecked = state.preferences.enableBrightnessSwipeGesture,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleEnableBrightnessSwipeGesture) },
                )
                PreferenceSlider(
                    title = stringResource(R.string.brightness_gesture_sensitivity),
                    description = state.preferences.brightnessGestureSensitivity.toString(decimalPlaces = 2),
                    icon = NextIcons.Sensitivity,
                    enabled = state.preferences.enableBrightnessSwipeGesture,
                    value = state.preferences.brightnessGestureSensitivity,
                    valueRange = 0.1f..2.0f,
                    onValueChange = { onAction(GesturePreferencesUiEvent.UpdateBrightnessGestureSensitivity(it)) },
                    trailingContent = {
                        FilledIconButton(
                            enabled = state.preferences.enableBrightnessSwipeGesture,
                            onClick = { onAction(GesturePreferencesUiEvent.UpdateBrightnessGestureSensitivity(PlayerPreferences.DEFAULT_BRIGHTNESS_GESTURE_SENSITIVITY)) },
                        ) {
                            Icon(
                                imageVector = NextIcons.History,
                                contentDescription = stringResource(id = R.string.reset_brightness_gesture_sensitivity),
                            )
                        }
                    },
                )
                PreferenceSwitch(
                    title = stringResource(id = R.string.volume_gesture),
                    description = stringResource(id = R.string.volume_gesture_description),
                    icon = NextIcons.SwipeVertical,
                    isChecked = state.preferences.enableVolumeSwipeGesture,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleEnableVolumeSwipeGesture) },
                )
                PreferenceSlider(
                    title = stringResource(R.string.volume_gesture_sensitivity),
                    description = state.preferences.volumeGestureSensitivity.toString(decimalPlaces = 2),
                    icon = NextIcons.Sensitivity,
                    enabled = state.preferences.enableVolumeSwipeGesture,
                    value = state.preferences.volumeGestureSensitivity,
                    valueRange = 0.1f..2.0f,
                    onValueChange = { onAction(GesturePreferencesUiEvent.UpdateVolumeGestureSensitivity(it)) },
                    trailingContent = {
                        FilledIconButton(
                            enabled = state.preferences.enableVolumeSwipeGesture,
                            onClick = { onAction(GesturePreferencesUiEvent.UpdateVolumeGestureSensitivity(PlayerPreferences.DEFAULT_VOLUME_GESTURE_SENSITIVITY)) },
                        ) {
                            Icon(
                                imageVector = NextIcons.History,
                                contentDescription = stringResource(id = R.string.reset_volume_gesture_sensitivity),
                            )
                        }
                    },
                )
                PreferenceSwitch(
                    title = stringResource(id = R.string.zoom_gesture),
                    description = stringResource(id = R.string.zoom_gesture_description),
                    icon = NextIcons.Pinch,
                    isChecked = state.preferences.useZoomControls,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleUseZoomControls) },
                )
                PreferenceSwitch(
                    title = stringResource(id = R.string.pan_gesture),
                    description = stringResource(id = R.string.pan_gesture_description),
                    icon = NextIcons.Pan,
                    enabled = state.preferences.useZoomControls,
                    isChecked = state.preferences.enablePanGesture,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleEnablePanGesture) },
                )
                PreferenceSwitchWithDivider(
                    title = stringResource(id = R.string.double_tap),
                    description = stringResource(id = R.string.double_tap_description),
                    icon = NextIcons.DoubleTap,
                    isChecked = (state.preferences.doubleTapGesture != DoubleTapGesture.NONE),
                    onChecked = { onAction(GesturePreferencesUiEvent.ToggleDoubleTapGesture) },
                    onClick = { onAction(GesturePreferencesUiEvent.ShowDialog(GesturePreferenceDialog.DoubleTapDialog)) },
                )
                PreferenceSwitch(
                    title = stringResource(id = R.string.long_press_gesture),
                    description = stringResource(id = R.string.long_press_gesture_desc, state.preferences.longPressControlsSpeed),
                    icon = NextIcons.Tap,
                    isChecked = state.preferences.useLongPressControls,
                    onClick = { onAction(GesturePreferencesUiEvent.ToggleUseLongPressControls) },
                )
                PreferenceSlider(
                    title = stringResource(R.string.speed),
                    description = stringResource(R.string.fast_playback_speed, state.preferences.longPressControlsSpeed),
                    icon = NextIcons.Speed,
                    enabled = state.preferences.useLongPressControls,
                    value = state.preferences.longPressControlsSpeed.coerceIn(
                        PlayerPreferences.MIN_LONG_PRESS_CONTROLS_SPEED,
                        PlayerPreferences.MAX_LONG_PRESS_CONTROLS_SPEED,
                    ),
                    valueRange = PlayerPreferences.MIN_LONG_PRESS_CONTROLS_SPEED..PlayerPreferences.MAX_LONG_PRESS_CONTROLS_SPEED,
                    steps = 6,
                    onValueChange = { onAction(GesturePreferencesUiEvent.UpdateLongPressControlsSpeed(it.roundToInt().toFloat())) },
                )
                PreferenceSlider(
                    title = stringResource(R.string.seek_increment),
                    description = stringResource(R.string.seconds, state.preferences.seekIncrement),
                    isLastItem = true,
                    icon = NextIcons.Replay,
                    value = state.preferences.seekIncrement.toFloat(),
                    valueRange = 1.0f..60.0f,
                    onValueChange = { onAction(GesturePreferencesUiEvent.UpdateSeekIncrement(it.toInt())) },
                    onReset = { onAction(GesturePreferencesUiEvent.UpdateSeekIncrement(PlayerPreferences.DEFAULT_SEEK_INCREMENT)) },
                    trailingContent = {
                        FilledIconButton(onClick = { onAction(GesturePreferencesUiEvent.UpdateSeekIncrement(PlayerPreferences.DEFAULT_SEEK_INCREMENT)) }) {
                            Icon(
                                imageVector = NextIcons.History,
                                contentDescription = stringResource(id = R.string.reset_seek_increment),
                            )
                        }
                    },
                )
            }
        }

        state.showDialog?.let { showDialog ->
            when (showDialog) {
                GesturePreferenceDialog.DoubleTapDialog -> {
                    OptionsDialog(
                        text = stringResource(id = R.string.double_tap),
                        onDismissClick = { onAction(GesturePreferencesUiEvent.ShowDialog(null)) },
                    ) {
                        items(DoubleTapGesture.entries.toTypedArray()) {
                            RadioTextButton(
                                text = it.name(),
                                selected = (it == state.preferences.doubleTapGesture),
                                onClick = {
                                    onAction(GesturePreferencesUiEvent.UpdateDoubleTapGesture(it))
                                    onAction(GesturePreferencesUiEvent.ShowDialog(null))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@DayNightPreview
@Composable
private fun GesturePreferencesScreenPreview() {
    NextPlayerTheme {
        GesturePreferencesScreenContent(
            state = GesturePreferencesUiState(),
            onAction = {},
        )
    }
}
