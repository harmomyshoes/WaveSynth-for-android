#pragma once

#include <memory>
#include <mutex>
#include "Wavetable.h"
#include "WavetableFactory.h"

namespace wavetablesynthesiser {
    class WavetableOscillator;
    class AudioPlayer;

    constexpr auto samplingRate = 48000;

    class WavetableSynthesiser {
    public:
        WavetableSynthesiser();
        ~WavetableSynthesiser();

        void play();
        void stop();
        bool isPlaying();
        void setFrequency(float frequencyInHz);
        void setVolume(float volumeInDb);
        void setWavetable(Wavetable wavetable);

    private:
        std::atomic<bool> _isPlaying{false};
        std::mutex _mutex;
        WavetableFactory _wavetableFactory;
        Wavetable _currentWavetable{Wavetable::SINE};
        std::shared_ptr<WavetableOscillator> _oscillator;
        std::unique_ptr<AudioPlayer> _audioPlayer;
    };
}