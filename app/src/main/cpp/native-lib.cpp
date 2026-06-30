#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_travelplanner_util_NativeLib_getMapsApiKey(JNIEnv* env, jobject /* this */) {
    std::string api_key = "AIzaSyApoh_GnGlKJy-bqgH0Cc-aNlaYxtGIKJk";
    return env->NewStringUTF(api_key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_travelplanner_util_NativeLib_getWeatherApiKey(JNIEnv* env, jobject /* this */) {
    std::string api_key = "fdedde0353db046c465911df1d2390ce";
    return env->NewStringUTF(api_key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_travelplanner_util_NativeLib_getOpenTripMapApiKey(JNIEnv* env, jobject /* this */) {
    std::string api_key = "5eb2ff22a101f251c51a9677e313791005a3962638363715102029";
    return env->NewStringUTF(api_key.c_str());
}
