#include "ru_maksimbulva_chess_chesslib_ChesslibWrapper.h"
#include "jni_utils.h"

#include "../../../../../chesslib/Engine.h"
#include "../../../../../chesslib/testing_utils.h"

#include <exception>

using namespace chesslib;

namespace {

Engine* getEngine(jlong enginePointer)
{
    return reinterpret_cast<Engine*>(enginePointer);
}

}

extern "C" {

JNIEXPORT jlong JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_calculateLegalMovesCount(
        JNIEnv* env, jobject, jstring fenString, jint depthPly)
{
    const char *cstr = env->GetStringUTFChars(fenString, nullptr);

    jlong result = -1;
    try {
        result = static_cast<jlong>(countLegalMoves(std::string{cstr}, depthPly));
    }
    catch (std::exception &ex) {
        logError(ex.what());
    }

    env->ReleaseStringUTFChars(fenString, cstr);
    return result;
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_resetGame__J(
        JNIEnv* env, jobject, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);

    try {
        engine->resetGame();
    }
    catch (std::exception &ex) {
        logError(ex.what());
    }
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_resetGame__Ljava_lang_String_2J(
        JNIEnv* env, jobject, jstring fenString, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);
    const char *cstr = env->GetStringUTFChars(fenString, nullptr);

    try {
        engine->resetGame(std::string(cstr));
    }
    catch (std::exception& ex) {
        logError(ex.what());
    }

    env->ReleaseStringUTFChars(fenString, cstr);
}


JNIEXPORT jlong JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_createEngineInstance(
        JNIEnv*, jobject)
{
    return reinterpret_cast<jlong>(new Engine());
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_ChesslibWrapper_releaseEngineInstance(
        JNIEnv*, jobject, jlong enginePointer)
{
    if (enginePointer != 0) {
        delete getEngine(enginePointer);
    }
}

}  // extern "C"
