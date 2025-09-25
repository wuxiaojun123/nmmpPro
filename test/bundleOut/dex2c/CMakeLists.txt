cmake_minimum_required(VERSION 3.18.1)
project(nmmp)


#add_subdirectory(vm)


include_directories(.)
include_directories(generated/)
include_directories(vm)

file(GLOB GEN_SOURCES generated/*_functions.c generated/jni_init.c)

set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -fvisibility=hidden ")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -fvisibility=hidden ")

#外部程序处理时替换 LIBNAME_PLACEHOLDER
set(LIBNAME_PLACEHOLDER "null")


add_library(${LIBNAME_PLACEHOLDER} SHARED
        ConstantPool.c
        ./vm/DexCatch.cpp
        ./vm/Exception.cpp
        ./vm/Interp.cpp
        ./vm/InterpC-portable.cpp
        ./vm/GlobalCache.cpp
        ./vm/JNIWrapper.cpp
        ${GEN_SOURCES}
)

set(CMAKE_CXX_FLAGS_RELEASE "-mllvm -fla -mllvm -sub -mllvm -bcf -mllvm -sobf")
set(CMAKE_C_FLAGS_RELEASE "-mllvm -fla -mllvm -sub -mllvm -bcf -mllvm -sobf")

target_link_libraries(${LIBNAME_PLACEHOLDER} log)