#include <cmath>
#include "Log.h"
#include "WavetableSynthesiser.h"
#include "OboeAudioPlayer.h"
#include "WavetableOscillator.h"


namespace wavetablesynthesiser {

//    WavetableSynthesiser::WavetableSynthesiser()
//            : _oscillator{std::make_shared<A4Oscillator>(samplingRate)},
//              _audioPlayer{
//                      std::make_unique<OboeAudioPlayer>(
//                              _oscillator, samplingRate)} {}
    WavetableSynthesiser::WavetableSynthesiser()
            : _oscillator{
            std::make_shared<WavetableOscillator>(
                    _wavetableFactory.getWaveTable(_currentWavetable),
                    samplingRate)},
              _audioPlayer{
                      std::make_unique<OboeAudioPlayer>(_oscillator, samplingRate)} {}

    WavetableSynthesiser::~WavetableSynthesiser() = default;

    bool WavetableSynthesiser::isPlaying() {
        LOGD("isPlaying() called.");
        return _isPlaying;
    }

    void WavetableSynthesiser::play() {
        std::lock_guard<std::mutex> lock(_mutex);
        const auto result = _audioPlayer->play();
        if (result == 0) {
            _isPlaying = true;
        } else {
            LOGD("Could not start playback.");
        }
    }

    void WavetableSynthesiser::stop() {
        std::lock_guard<std::mutex> lock(_mutex);
        _audioPlayer->stop();
        _isPlaying = false;
    }

//    void WavetableSynthesiser::setFrequency(float frequencyInHz) {
//        LOGD("Frequency set to %.2f Hz.", frequencyInHz);
//    }
//
//    void WavetableSynthesiser::setVolume(float volumeInDb) {
//        LOGD("Volume set to %.2f dBFS.", volumeInDb);
//    }
//
//    void WavetableSynthesiser::setWavetable(Wavetable wavetable) {
//        LOGD("Wavetable set to %d.", static_cast<int>(wavetable));
//    }
// WavetableOscillator.cpp
    void WavetableSynthesiser::setFrequency(float frequencyInHz) {
        _oscillator->setFrequency(frequencyInHz);
    }

    float dBToAmplitude(float dB) {
        return std::pow(10.f, dB / 20.f);
    }

    void WavetableSynthesiser::setVolume(float volumeInDb) {
        const auto amplitude = dBToAmplitude(volumeInDb);
        _oscillator->setAmplitude(amplitude);
    }

    void WavetableSynthesiser::setWavetable(Wavetable wavetable) {
        if (_currentWavetable != wavetable) {
            _currentWavetable = wavetable;
            _oscillator->setWavetable(_wavetableFactory.getWaveTable(wavetable));
        }
    }


} // namespace wavetablesynthesizer//
// Created by Xie on 30/06/2026.
//
