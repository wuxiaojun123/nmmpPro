#include <jni.h>
#include "./include/GlobalCache.h"

//auto generated
extern void classes_setup(JNIEnv *env);
extern void classes2_setup(JNIEnv *env);
extern void classes3_setup(JNIEnv *env);
extern void classes4_setup(JNIEnv *env);


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    cacheInitial(env);


    //auto generated setup function
    classes_setup(env);
    classes2_setup(env);
    classes3_setup(env);
    classes4_setup(env);


    return JNI_VERSION_1_6;
}


