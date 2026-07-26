package com.example.wavetablesynthesizer

import android.util.Log

class LoggingWavetableSynthesiser : WavetableSynthesiser {
    private var isPlaying = false;

    override suspend fun play() {
        //TODO("Not yet implemented")
        Log.d("LoggingWavetableSynthesizer", "play() called.")
        isPlaying = true
    }

    override suspend fun stop() {
        //TODO("Not yet implemented")
        Log.d("LoggingWavetableSynthesizer", "stop() called.")
        isPlaying = false
    }

    override suspend fun isPlaying(): Boolean {
        return isPlaying
    }

    override suspend fun setFrequency(frequencyInHz: Float) {
        //TODO("Not yet implemented")
        Log.d("LoggingWavetableSynthesizer", "Frequency set to $frequencyInHz Hz.")
    }

    override suspend fun setVolume(volumeInDb: Float) {
        //TODO("Not yet implemented")
        Log.d("LoggingWavetableSynthesizer", "Volume set to $volumeInDb dB.")
    }

    override suspend fun setWavetable(wavetable: Wavetable) {
        Log.d("LoggingWavetableSynthesizer", "Wavetable set to $wavetable")
    }
}