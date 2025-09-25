
#include <stdio.h>
#include <string.h>
#include <malloc.h>
#include <jni.h>
#include "./vm/include/vm.h"
#include "classes3_resolver.c"

#ifdef __cplusplus
extern "C" {
#endif


#define SET_REGISTER_FLOAT(_idx, _val)      (*((float*) &regs[(_idx)]) = (_val))


#define SET_REGISTER_WIDE(_idx, _val)       (regs[(_idx)] =(s8) (_val));

#define SET_REGISTER_DOUBLE(_idx, _val)     (*((double*) &regs[(_idx)]) = (_val));


static jobject Java_com_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_valueOf__Ljava_lang_String_2_Lcom_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_2(JNIEnv *env, jclass jcls , jobject p0) {
    regptr_t regs[2];
    regs[0] = 0;
    regs[1] = 0;
    regs[1] = (regptr_t) p0;

    u1 reg_flags[2];
    reg_flags[0] = 0;
    reg_flags[1] = 0;
    reg_flags[1] = 1;

    static const u2 insns[] = {
0x00e1, 0x0003, 0x2075, 0x0002, 0x0010, 0x01c8, 0x0138, 0x0003, 0x01bb, 
    };
    const u1 *tries = NULL;

    const vmCode code = {
            .insns=insns,
            .insnsSize=9,
            .regs=regs,
            .reg_flags=reg_flags,
            .triesHandlers=tries
    };

    jvalue value = vmInterpret(env,
                                &code,
                                &dvmResolver);
    return value.l;
}

static jobject Java_com_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_values__3Lcom_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_2(JNIEnv *env, jclass jcls ) {
    regptr_t regs[1];
    regs[0] = 0;

    u1 reg_flags[1];
    reg_flags[0] = 0;

    static const u2 insns[] = {
0x00d1, 0x0097, 0x1006, 0x0003, 0x0000, 0x00c8, 0x0038, 0x0016, 0x00bb, 
    };
    const u1 *tries = NULL;

    const vmCode code = {
            .insns=insns,
            .insnsSize=9,
            .regs=regs,
            .reg_flags=reg_flags,
            .triesHandlers=tries
    };

    jvalue value = vmInterpret(env,
                                &code,
                                &dvmResolver);
    return value.l;
}


typedef struct{
    u4 nameIdx;
    u4 sigIdx;
    void *fnPtr;
} MyNativeMethod;
static const MyNativeMethod gNativeMethods[] = {
    {350, 364, (void *) Java_com_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_valueOf__Ljava_lang_String_2_Lcom_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_2},
    {351, 374, (void *) Java_com_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_values__3Lcom_mbridge_msdk_MBridgeSDK_00024PLUGIN_1LOAD_1STATUS_2},
};
//ends native method

typedef struct {
    u4 classIdx;
    u4 offset;
    u4 count;
} NativeMethodData;
static const NativeMethodData gNativeRegisterData[] = {
    {.classIdx = 3, .offset = 0, .count = 2},
};

static void Java_com_Sxmnvjrof_cpixktjixak_classes3udzwl__I_V(JNIEnv *env, jclass jcls, jint dataIdx){
#define MAX_METHOD 8
    JNINativeMethod methodBuf[MAX_METHOD];

    JNINativeMethod *methods;
    const NativeMethodData data = gNativeRegisterData[(u4) dataIdx];
    if (data.count > MAX_METHOD) {
        methods = (JNINativeMethod *) malloc(sizeof(JNINativeMethod) * data.count);
    } else {
        //方法数比较小直接使用栈内存,减少内存分配和释放
        methods = methodBuf;
    }

    jclass clazz = (*env)->FindClass(env, STRING_BY_CLASS_ID(data.classIdx));
    if (clazz == NULL) {
        return;
    }
    for (int midx = 0; midx < data.count; ++midx) {
        MyNativeMethod myNativeMethod = gNativeMethods[data.offset + midx];

        JNINativeMethod *method = methods + midx;
        method->name = STRING_BY_ID(myNativeMethod.nameIdx);
        method->signature = STRING_BY_ID(myNativeMethod.sigIdx);
        method->fnPtr = myNativeMethod.fnPtr;
    }

    (*env)->RegisterNatives(env, clazz, methods, data.count);

    (*env)->DeleteLocalRef(env, clazz);

    //不相等表示使用malloc申请的内存需要释放
    if (methods != methodBuf)free(methods);
}

void classes3_setup(JNIEnv *env) {

    //符号解析器初始化
    resolver_init(env);

    //注册
    jclass clazz = (*env)->FindClass(env, "com/Sxmnvjrof/cpixktjixak");
    static const JNINativeMethod nativeMethod = {
        .name="classes3udzwl",
        .signature="(I)V",
        .fnPtr=Java_com_Sxmnvjrof_cpixktjixak_classes3udzwl__I_V
    };
   (*env)->RegisterNatives(env, clazz, &nativeMethod, 1);

   (*env)->DeleteLocalRef(env, clazz);

}


#ifdef __cplusplus
}
#endif

