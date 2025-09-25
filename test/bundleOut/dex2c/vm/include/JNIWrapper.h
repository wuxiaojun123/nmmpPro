//
// Created by mao on 2023/8/27.
//

#ifndef DEX_EDITOR_JNIWRAPPER_H
#define DEX_EDITOR_JNIWRAPPER_H
#include <jni.h>

typedef struct {

    jboolean    (*CallStaticBooleanMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jcharArray    (*NewCharArray)(JNIEnv*, jsize);

    jdouble     (*GetDoubleField)(JNIEnv*, jobject, jfieldID);

    jchar       (*GetStaticCharField)(JNIEnv*, jclass, jfieldID);


    jbooleanArray (*NewBooleanArray)(JNIEnv*, jsize);


    jobject     (*AllocObject)(JNIEnv*, jclass);

    jint        (*MonitorExit)(JNIEnv*, jobject);

    void        (*GetByteArrayRegion)(JNIEnv*, jbyteArray,
                        jsize, jsize, jbyte*);

    jint        (*CallStaticIntMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    void        (*CallNonvirtualVoidMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    void        (*GetFloatArrayRegion)(JNIEnv*, jfloatArray,
                        jsize, jsize, jfloat*);

    jint        (*CallNonvirtualIntMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    jbyte       (*GetByteField)(JNIEnv*, jobject, jfieldID);

    void        (*SetStaticShortField)(JNIEnv*, jclass, jfieldID, jshort);

    void        (*SetIntArrayRegion)(JNIEnv*, jintArray,
                        jsize, jsize, const jint*);

    void        (*CallStaticVoidMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jdoubleArray  (*NewDoubleArray)(JNIEnv*, jsize);

    void        (*GetShortArrayRegion)(JNIEnv*, jshortArray,
                        jsize, jsize, jshort*);


    jobject     (*CallNonvirtualObjectMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);


    void        (*SetBooleanArrayRegion)(JNIEnv*, jbooleanArray,
                        jsize, jsize, const jboolean*);

    void        (*CallVoidMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jshort      (*GetStaticShortField)(JNIEnv*, jclass, jfieldID);

    void        (*SetFloatField)(JNIEnv*, jobject, jfieldID, jfloat);

    jfloatArray   (*NewFloatArray)(JNIEnv*, jsize);

    jfloat      (*GetStaticFloatField)(JNIEnv*, jclass, jfieldID);

    void        (*SetDoubleArrayRegion)(JNIEnv*, jdoubleArray,
                        jsize, jsize, const jdouble*);

    jint        (*GetStaticIntField)(JNIEnv*, jclass, jfieldID);

    void        (*SetIntField)(JNIEnv*, jobject, jfieldID, jint);

    jint        (*Throw)(JNIEnv*, jthrowable);

    jbyte       (*CallStaticByteMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jdouble     (*CallStaticDoubleMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);


    jsize       (*GetArrayLength)(JNIEnv*, jarray);

    void        (*SetLongField)(JNIEnv*, jobject, jfieldID, jlong);


    void*       (*GetPrimitiveArrayCritical)(JNIEnv*, jarray, jboolean*);

    void        (*GetCharArrayRegion)(JNIEnv*, jcharArray,
                        jsize, jsize, jchar*);


    jobject     (*CallObjectMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    void        (*SetStaticDoubleField)(JNIEnv*, jclass, jfieldID, jdouble);

    void        (*SetLongArrayRegion)(JNIEnv*, jlongArray,
                        jsize, jsize, const jlong*);

    jdouble     (*CallNonvirtualDoubleMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    jshort      (*CallStaticShortMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jlong       (*GetStaticLongField)(JNIEnv*, jclass, jfieldID);


    jint        (*MonitorEnter)(JNIEnv*, jobject);

    jshortArray   (*NewShortArray)(JNIEnv*, jsize);

    jintArray     (*NewIntArray)(JNIEnv*, jsize);


    jobject     (*GetStaticObjectField)(JNIEnv*, jclass, jfieldID);

    jdouble     (*GetStaticDoubleField)(JNIEnv*, jclass, jfieldID);


    jobject     (*NewLocalRef)(JNIEnv*, jobject);

    void        (*SetStaticBooleanField)(JNIEnv*, jclass, jfieldID, jboolean);

    jfloat      (*CallNonvirtualFloatMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    void        (*SetShortArrayRegion)(JNIEnv*, jshortArray,
                        jsize, jsize, const jshort*);


    jboolean    (*ExceptionCheck)(JNIEnv*);

    jchar       (*CallCharMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jlongArray    (*NewLongArray)(JNIEnv*, jsize);


    void        (*DeleteLocalRef)(JNIEnv*, jobject);

    jchar       (*CallStaticCharMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jbyte       (*CallNonvirtualByteMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    void        (*ReleasePrimitiveArrayCritical)(JNIEnv*, jarray, void*, jint);

    void        (*SetStaticIntField)(JNIEnv*, jclass, jfieldID, jint);


    void        (*SetStaticObjectField)(JNIEnv*, jclass, jfieldID, jobject);

    void        (*SetShortField)(JNIEnv*, jobject, jfieldID, jshort);

    void        (*GetDoubleArrayRegion)(JNIEnv*, jdoubleArray,
                        jsize, jsize, jdouble*);

    jint        (*CallIntMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    void        (*SetStaticByteField)(JNIEnv*, jclass, jfieldID, jbyte);


    void        (*SetObjectField)(JNIEnv*, jobject, jfieldID, jobject);

    jboolean    (*GetBooleanField)(JNIEnv*, jobject, jfieldID);


    void        (*GetBooleanArrayRegion)(JNIEnv*, jbooleanArray,
                        jsize, jsize, jboolean*);


    jboolean    (*IsInstanceOf)(JNIEnv*, jobject, jclass);

    jboolean    (*CallBooleanMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jdouble     (*CallDoubleMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jbyte       (*CallByteMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jlong       (*CallNonvirtualLongMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    jshort      (*CallShortMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);


    jobject     (*GetObjectField)(JNIEnv*, jobject, jfieldID);

    void        (*SetBooleanField)(JNIEnv*, jobject, jfieldID, jboolean);

    jfloat      (*CallFloatMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    void        (*SetByteField)(JNIEnv*, jobject, jfieldID, jbyte);

    jbyteArray    (*NewByteArray)(JNIEnv*, jsize);

    void        (*SetObjectArrayElement)(JNIEnv*, jobjectArray, jsize, jobject);

    void        (*SetDoubleField)(JNIEnv*, jobject, jfieldID, jdouble);

    jlong       (*GetLongField)(JNIEnv*, jobject, jfieldID);

    jfloat      (*GetFloatField)(JNIEnv*, jobject, jfieldID);

    void        (*SetByteArrayRegion)(JNIEnv*, jbyteArray,
                        jsize, jsize, const jbyte*);

    jint        (*GetIntField)(JNIEnv*, jobject, jfieldID);

    void        (*SetCharField)(JNIEnv*, jobject, jfieldID, jchar);

    jthrowable  (*ExceptionOccurred)(JNIEnv*);

    jshort      (*GetShortField)(JNIEnv*, jobject, jfieldID);

    jboolean    (*GetStaticBooleanField)(JNIEnv*, jclass, jfieldID);

    jchar       (*CallNonvirtualCharMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    void        (*GetIntArrayRegion)(JNIEnv*, jintArray,
                        jsize, jsize, jint*);

    void        (*ExceptionClear)(JNIEnv*);

    jboolean    (*CallNonvirtualBooleanMethodA)(JNIEnv*, jobject, jclass,
                         jmethodID, const jvalue*);

    jfloat      (*CallStaticFloatMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jchar       (*GetCharField)(JNIEnv*, jobject, jfieldID);

    void        (*SetStaticCharField)(JNIEnv*, jclass, jfieldID, jchar);

    void        (*SetStaticFloatField)(JNIEnv*, jclass, jfieldID, jfloat);

    jshort      (*CallNonvirtualShortMethodA)(JNIEnv*, jobject, jclass,
                        jmethodID, const jvalue*);

    void        (*SetStaticLongField)(JNIEnv*, jclass, jfieldID, jlong);

    jlong       (*CallLongMethodA)(JNIEnv*, jobject, jmethodID, const jvalue*);

    jbyte       (*GetStaticByteField)(JNIEnv*, jclass, jfieldID);

    void        (*GetLongArrayRegion)(JNIEnv*, jlongArray,
                        jsize, jsize, jlong*);



    jobject     (*CallStaticObjectMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jobject     (*GetObjectArrayElement)(JNIEnv*, jobjectArray, jsize);

    void        (*SetFloatArrayRegion)(JNIEnv*, jfloatArray,
                        jsize, jsize, const jfloat*);

    void        (*SetCharArrayRegion)(JNIEnv*, jcharArray,
                        jsize, jsize, const jchar*);

    jboolean    (*IsSameObject)(JNIEnv*, jobject, jobject);

    jlong       (*CallStaticLongMethodA)(JNIEnv*, jclass, jmethodID, const jvalue*);

    jobjectArray (*NewObjectArray)(JNIEnv*, jsize, jclass, jobject);
} JNIWrapper;

const JNIWrapper * getJNIWrapper();

#endif //DEX_EDITOR_JNIWRAPPER_H