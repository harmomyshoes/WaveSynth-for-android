package com.example.wavetablesynthesizer


import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeWavetableSynthesiser : WavetableSynthesiser, DefaultLifecycleObserver {
    private var synthesiserHandle: Long = 0
    private val synthesiserMutex = Any()
    private external fun create(): Long
    private external fun delete(synthesiserHandle: Long)
    private external fun play(synthesiserHandle: Long)
    private external fun stop(synthesiserHandle: Long)
    private external fun isPlaying(synthesiserHandle: Long): Boolean
    private external fun setFrequency(synthesiserHandle: Long, frequencyInHz: Float)
    private external fun setVolume(synthesiserHandle: Long, amplitudeInDb: Float)
    private external fun setWavetable(synthesiserHandle: Long, wavetable: Int)

    companion object{
        init {
            System.loadLibrary("wavetablesynthesiser")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        synchronized(synthesiserMutex){
            Log.d("NativeWavetableSynthesizer", "onResume() called")
            createNativeHandleIfNotExists()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        synchronized(synthesiserMutex) {
            Log.d("NativeWavetableSynthesizer", "onPause() called")

            if (synthesiserHandle == 0L) {
                Log.e("NativeWavetableSynthesizer", "Attempting to destroy a null synthesizer.")
                return
            }

            // Destroy the synthesizer
            delete(synthesiserHandle)
            synthesiserHandle = 0L
        }
    }

    private fun createNativeHandleIfNotExists() {
        if (synthesiserHandle != 0L) {
            return
        }

        // create the synthesizer
        synthesiserHandle = create()
    }

    override suspend fun play() = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            play(synthesiserHandle)
        }
    }

    override suspend fun stop() = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            stop(synthesiserHandle)
        }
    }

    override suspend fun isPlaying(): Boolean = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            return@withContext isPlaying(synthesiserHandle)
        }
    }

    override suspend fun setFrequency(frequencyInHz: Float) = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            setFrequency(synthesiserHandle, frequencyInHz)
        }
    }

    override suspend fun setVolume(volumeInDb: Float) = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            setVolume(synthesiserHandle, volumeInDb)
        }
    }

    override suspend fun setWavetable(wavetable: Wavetable) = withContext(Dispatchers.Default){
        synchronized(synthesiserMutex){
            createNativeHandleIfNotExists()
            setWavetable(synthesiserHandle, wavetable.ordinal)
        }
    }

}