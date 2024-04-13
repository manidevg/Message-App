#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring


JNICALL
Java_com_sciflare_message_utils_AppController_getSecretKeyPswd(JNIEnv * env, jobject){
    std::string hello = "ManikandanG#@!456";
    return env->NewStringUTF(hello.c_str());
}