LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := chess-jni

LOCAL_CPP_FEATURES := exceptions
LOCAL_CPPFLAGS += -std=c++14

LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
	../../../chesslib/board_utils.cpp \
	../../../chesslib/Board.cpp \
	../../../chesslib/CatboostEvaluator.cpp \
	../../../chesslib/CatboostModel.cpp \
	../../../chesslib/Engine.cpp \
	../../../chesslib/Evaluator.cpp \
	../../../chesslib/fen.cpp \
	../../../chesslib/Game.cpp \
	../../../chesslib/MemoryPool.cpp \
	../../../chesslib/moves_generator.cpp \
	../../../chesslib/MovesCollection.cpp \
	../../../chesslib/notation.cpp \
	../../../chesslib/pawns_evaluation.cpp \
	../../../chesslib/Player.cpp \
	../../../chesslib/position_factory.cpp \
	../../../chesslib/Position.cpp \
	../../../chesslib/ScoredMove.cpp \
	../../../chesslib/SearchEngine.cpp \
	../../../chesslib/string_repr.cpp \
	../../../chesslib/string_utils.cpp \
	../../../chesslib/testing_utils.cpp \
	../../../chesslib/TranspositionTable.cpp \
	../../../chesslib/Variation.cpp \
	../../../chesslib/ZobristHasher.cpp \
	../src/main/jni/ChesslibWrapper.cpp \
	../src/main/jni/jni_utils.cpp

include $(BUILD_SHARED_LIBRARY)
