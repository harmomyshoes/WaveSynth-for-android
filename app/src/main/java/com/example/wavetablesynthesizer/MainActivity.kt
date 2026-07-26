package com.example.wavetablesynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.rotate

class MainActivity : ComponentActivity() {
    private val synthesiserViewModel: WavetableSynthesiserViewModel by viewModels<WavetableSynthesiserViewModel>()
    private val synthersiser = NativeWavetableSynthesiser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        synthesiserViewModel.wavetableSynthesizer = synthersiser

        lifecycle.addObserver(synthersiser)
        setContent {
            WavetableSynthesizerTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    WavetableSynthesizerApp(Modifier, synthesiserViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(synthersiser)
    }

    override fun onResume() {
        super.onResume()
        synthesiserViewModel.applyParameters()
    }
}


@Composable
fun WavetableSynthesizerApp(
    modifier: Modifier,
    synthesiserViewModel: WavetableSynthesiserViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        WavetableSelectionPanel(modifier, synthesiserViewModel)
        ControlsPanel(modifier, synthesiserViewModel)
    }
}

@Composable
private fun WavetableSelectionPanel(
    modifier: Modifier,
    synthesiserViewModel: WavetableSynthesiserViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .border(BorderStroke(5.dp, Color.Black)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .border(BorderStroke(5.dp, Color.Black)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Wavetable selection panel")
            WavetableSelectionButtons(modifier, synthesiserViewModel)
        }
    }
}

// inside WavetableSelectionPanel composable
//Text(stringResource(R.string.wavetable))
//WavetableSelectionButtons(modifier)
//...

@Composable
private fun WavetableSelectionButtons(
    modifier: Modifier,
    synthesiserViewModel: WavetableSynthesiserViewModel

) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (wavetable in Wavetable.entries) {
            WavetableButton(
                modifier = modifier,
                onClick = {
                    synthesiserViewModel.setWavetable(wavetable)
                },
                label = stringResource(wavetable.toResourceString()))
        }
    }
}

@Composable
private fun WavetableButton(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String,
) {
    Button(modifier = modifier, onClick = onClick) {
        Text(label)
    }
}

@Composable
private fun ControlsPanel(
    modifier: Modifier,
    synthesiserViewModel: WavetableSynthesiserViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(BorderStroke(5.dp, Color.Black)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f)
                .border(BorderStroke(5.dp, Color.Black)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pitch and play control")
            PitchControl(modifier,synthesiserViewModel)
            PlayControl(modifier,synthesiserViewModel)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .border(BorderStroke(5.dp, Color.Black))
        ) {
            Text("Volume control")
            VolumeControl(modifier,synthesiserViewModel)
        }
    }
}

@Composable
private fun PitchControl(
    modifier: Modifier,
    synthesiserViewModel: WavetableSynthesiserViewModel
) {
    val frequency = synthesiserViewModel.frequency.observeAsState()

    PitchControlContent(
        modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = synthesiserViewModel.sliderPositionFromFrequencyInHz(frequency.value!!),
        onValueChange = {
            synthesiserViewModel.setFrequencySliderPosition(it)
        },
        valueRange = 0f..1f,
        frequencyValueLabel = stringResource(R.string.frequency_value,
            frequency.value!!)
    )
}

@Composable
private fun PitchControlContent(
    modifier: Modifier,
    pitchControlLabel: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {
    Text(pitchControlLabel, modifier = modifier)
    Slider(modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(modifier = modifier, text = frequencyValueLabel)
    }
}

@Composable
private fun PlayControl(modifier: Modifier,
                        synthesiserViewModel: WavetableSynthesiserViewModel) {
    val playButtonLabel = synthesiserViewModel.playButtonLabel.observeAsState()

    PlayControlContent(
        modifier = modifier,
        // onClick handler now simply notifies the ViewModel that it has been clicked
        onClick = { synthesiserViewModel.playClicked() },
        buttonLabel = stringResource(playButtonLabel.value!!)
    )
    // playButtonLabel will never be null;
    // if it is, then we have a serious implementation issue
}

@Composable
private fun PlayControlContent(modifier: Modifier, onClick: () -> Unit, buttonLabel: String) {
    Button(modifier = modifier,
        onClick = onClick) {
        Text(buttonLabel)
    }
}
    @Composable
    fun VolumeControl(
        modifier: Modifier,
        synthesiserViewModel: WavetableSynthesiserViewModel
    ) {
        //val volume = rememberSaveable { mutableStateOf(0F) }
        val volume = synthesiserViewModel.volume.observeAsState()

        VolumeControlContent(
            modifier,
            volume = volume.value!!,
            onValueChange = {
                synthesiserViewModel.setVolume(it)
                            },
            volumeRange = synthesiserViewModel.volumeRange
            )
    }

    @Composable
    private fun VolumeControlContent(
        modifier: Modifier,
        volume: Float,
        onValueChange: (Float) -> Unit,
        volumeRange: ClosedFloatingPointRange<Float>
    ) {
        // The volume slider should take around 1/4 of the screen height
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val sliderHeight = screenHeight / 4

        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .offset(y = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )
        {
            Slider(
                value = volume,
                onValueChange = onValueChange,
                modifier = modifier
                    .width(sliderHeight.dp)
                    .rotate(270f),
                valueRange = volumeRange
            )
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeMute, contentDescription = null)
    }