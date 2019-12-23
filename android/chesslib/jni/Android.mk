LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := chess-jni

LOCAL_CPP_FEATURES := exceptions
LOCAL_CPPFLAGS += -std=c++14

LOCAL_SRC_FILES := \
	../../../chesslib/board_utils.cpp \
	../../../chesslib/Board.cpp \
	../../../chesslib/Engine.cpp \
	../../../chesslib/fen.cpp \
	../../../chesslib/Game.cpp \
	../../../chesslib/moves_generator.cpp \
	../../../chesslib/notation.cpp \
	../../../chesslib/position_factory.cpp \
	../../../chesslib/Position.cpp \
	../../../chesslib/string_repr.cpp \
	../../../chesslib/string_utils.cpp \
	../src/main/jni/libchess-jni.c

include $(BUILD_SHARED_LIBRARY)
