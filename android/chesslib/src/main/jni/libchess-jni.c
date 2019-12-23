#include <jni.h>

JNIEXPORT jstring JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_getMsgFromJni(
        JNIEnv* env, jobject instance)
{
    return (*env)->NewStringUTF(env, "Hello, C++ world!");
}

