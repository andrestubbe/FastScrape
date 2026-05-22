#ifndef FASTSCRAPE_H
#define FASTSCRAPE_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// Export JNI declarations for FastScrapeImpl

JNIEXPORT jstring JNICALL Java_fastscrape_FastScrapeImpl_nativeExtractReadableText(
    JNIEnv* env, jobject obj, jbyteArray htmlData);

JNIEXPORT jobjectArray JNICALL Java_fastscrape_FastScrapeImpl_nativeExtractLinks(
    JNIEnv* env, jobject obj, jbyteArray htmlData);

JNIEXPORT jobjectArray JNICALL Java_fastscrape_FastScrapeImpl_nativeExtractByTag(
    JNIEnv* env, jobject obj, jbyteArray htmlData, jstring tagName);

JNIEXPORT jstring JNICALL Java_fastscrape_FastScrapeImpl_nativeExtractJsonLD(
    JNIEnv* env, jobject obj, jbyteArray htmlData);

#ifdef __cplusplus
}
#endif

#endif // FASTSCRAPE_H
