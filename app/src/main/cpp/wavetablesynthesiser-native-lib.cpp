// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("wavetablesynthesizer");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("wavetablesynthesizer")
//      }
//    }
#include <jni.h>
#include <memory>
#include "Log.h"
#include "WavetableSynthesiser.h"


extern "C" {
JNIEXPORT jlong JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_create(JNIEnv *env, jobject thiz) {

    // TODO: implement create()
    auto synthesizer =
            std::make_unique<wavetablesynthesiser::WavetableSynthesiser>();

    if (not synthesizer) {
        LOGD("Failed to create the synthesizer.");
        synthesizer.reset(nullptr);
    }

    //“Stop owning this object. Do not delete it when this function finishes.”
    return reinterpret_cast<jlong>(synthesizer.release());
}

JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_delete(JNIEnv *env, jobject thiz,
                                                                        jlong synthesiser_handle) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);

    if (not synthesizer) {
        LOGD("Attempt to destroy an unitialized synthesizer.");
        return;
    }

    delete synthesizer;
}


JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_play(JNIEnv *env, jobject thiz,
                                                                      jlong synthesiser_handle) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);

    if (synthesizer) {
        synthesizer->play();
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}

JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_stop(JNIEnv *env, jobject thiz,
                                                                      jlong synthesiser_handle) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);

    if (synthesizer) {
        synthesizer->stop();
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}


JNIEXPORT jboolean JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_isPlaying(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jlong synthesiser_handle) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);

    if (not synthesizer) {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
        return false;
    }

    return synthesizer->isPlaying();
}

JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_setFrequency(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jlong synthesiser_handle,
                                                                              jfloat frequency_in_hz) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);
    const auto nativeFrequency = static_cast<float>(frequency_in_hz);

    if (synthesizer) {
        synthesizer->setFrequency(nativeFrequency);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}

JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_setVolume(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jlong synthesiser_handle,
                                                                           jfloat amplitude_in_db) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);
    const auto nativeVolume = static_cast<float>(amplitude_in_db);

    if (synthesizer) {
        synthesizer->setVolume(nativeVolume);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}

JNIEXPORT void JNICALL
Java_com_example_wavetablesynthesizer_NativeWavetableSynthesiser_setWavetable(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jlong synthesiser_handle,
                                                                              jint wavetable) {
    auto* synthesizer =
            reinterpret_cast<wavetablesynthesiser::WavetableSynthesiser*>(
                    synthesiser_handle);
    const auto nativeWavetable = static_cast<wavetablesynthesiser::Wavetable>(wavetable);

    if (synthesizer) {
        synthesizer->setWavetable(nativeWavetable);
    } else {
        LOGD(
                "Synthesizer not created. Please, create the synthesizer first by "
                "calling create().");
    }
}
}
