LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := chess-jni
LOCAL_SRC_FILES := board_utils.cpp Board.cpp Engine.cpp fen.cpp Game.cpp moves_generator.cpp notation.cpp position_factory.cpp Position.cpp string_repr.cpp string_utils.cpp uci_main.cpp winboard_main.cpp

include $(BUILD_SHARED_LIBRARY)
