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
	../../../chesslib/string_utils.cpp

# board_utils.cpp Board.cpp Engine.cpp fen.cpp Game.cpp moves_generator.cpp notation.cpp position_factory.cpp Position.cpp string_repr.cpp string_utils.cpp uci_main.cpp winboard_main.cpp

include $(BUILD_SHARED_LIBRARY)
