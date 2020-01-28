#include "ru_maksimbulva_chess_chesslib_AbsChesslibWrapper.h"
#include "jni_utils.h"

#include "../../../../../chesslib/fen.h"
#include "../../../../../chesslib/Engine.h"
#include "../../../../../chesslib/notation.h"
#include "../../../../../chesslib/testing_utils.h"

#include <exception>
#include <string>

using namespace chesslib;

namespace {

Engine* getEngine(jlong enginePointer)
{
    return reinterpret_cast<Engine*>(enginePointer);
}

}

extern "C" {

JNIEXPORT jlong JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_calculateLegalMovesCount(
        JNIEnv* env, jobject, jstring fenString, jint depthPly)
{
    JavaStringWrapper fen(env, fenString);

    jlong result = -1;
    try {
        result = static_cast<jlong>(countLegalMoves(fen.getString(), depthPly));
    }
    catch (std::exception &ex) {
        logError(ex.what());
    }

    return result;
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_resetGame__J(
        JNIEnv*, jobject, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);

    try {
        engine->resetGame();
    }
    catch (std::exception &ex) {
        logError(ex.what());
    }
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_resetGame__Ljava_lang_String_2J(
        JNIEnv* env, jobject, jstring fenString, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);
    JavaStringWrapper fen(env, fenString);

    try {
        engine->resetGame(fen.getString());
    }
    catch (std::exception& ex) {
        logError(ex.what());
    }
}

JNIEXPORT jstring JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_getCurrentPositionFen(
        JNIEnv* env, jobject, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);
    const Position& position = engine->getGame().getCurrentPosition();
    std::string fenString = encodeFen(position);
    return env->NewStringUTF(fenString.c_str());
}

JNIEXPORT jboolean JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_playMove(
        JNIEnv* env, jobject, jstring moveString, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);
    JavaStringWrapper move(env, moveString);

    bool result = false;
    try {
        result = engine->playMove(move.getString());
    }
    catch (std::exception& ex) {
        logError(ex.what());
    }

    return static_cast<jboolean>(result);
}

JNIEXPORT jstring JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_findBestVariation(
        JNIEnv* env, jobject, jlong enginePointer)
{
    Engine* const engine = getEngine(enginePointer);
    std::string variationString;

    try {
        auto bestVariation = engine->findBestVariation(nullptr);
        variationString += std::to_string(bestVariation.getEvaluation());
        for (const auto& move : bestVariation.getMoves()) {
            variationString.push_back(' ');
            variationString += toCoordinateNotation(move);
        }
    }
    catch (std::exception& ex) {
        logError(ex.what());
    }

    return env->NewStringUTF(variationString.c_str());
}


JNIEXPORT jlong JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_createEngineInstance(
        JNIEnv*, jobject)
{
    return reinterpret_cast<jlong>(new Engine());
}

JNIEXPORT void JNICALL Java_ru_maksimbulva_chess_chesslib_AbsChesslibWrapper_releaseEngineInstance(
        JNIEnv*, jobject, jlong enginePointer)
{
    if (enginePointer != 0) {
        delete getEngine(enginePointer);
    }
}

}  // extern "C"
