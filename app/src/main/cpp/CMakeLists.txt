cmake_minimum_required(VERSION 3.4.1)

add_library(uppercase-word-jni SHARED
        uppercase-word-jni.c)

# Include libraries needed for uppercase-word-jni lib
target_link_libraries(uppercase-word-jni
                      android
                      log)