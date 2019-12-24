#include "ru_maksimbulva_chess_chesslib_ChesslibWrapper.h"
#include "jni_utils.h"

#include "../../../../../chesslib/testing_utils.h"

#include <exception>

using namespace chesslib;

JNIEXPORT jlong JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_calculateLegalMovesCount(
    JNIEnv* env, jobject, jstring fenString, jint depthPly)
{
    const char* cstr = env->GetStringUTFChars(fenString, nullptr);

    jlong result = -1;
    try {
        result = static_cast<jlong>(countLegalMoves(std::string{ cstr }, depthPly));
    }
    catch (std::exception ex) {
        logError(ex.what());
    }

    env->ReleaseStringUTFChars(fenString, cstr);
    return result;
}
