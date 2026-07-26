#pragma once

#include <stdint.h>

namespace wavetablesynthesiser {
    class AudioPlayer {
    public:
        virtual ~AudioPlayer() = default;

        // Start the audio device
        virtual int32_t play() = 0;

        // Stop the audio device
        virtual void stop() = 0;
    };
}  // namespace wavetablesynthesizer