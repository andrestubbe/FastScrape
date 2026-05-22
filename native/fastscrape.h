#ifndef FASTXXX_H
#define FASTXXX_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// Export declarations (Matches fastscrape.def)
JNIEXPORT void JNICALL Java_fastscrape_FastScrape_doSomethingNative(JNIEnv* env, jobject obj);

#ifdef __cplusplus
}
#endif

#endif // FASTXXX_H

