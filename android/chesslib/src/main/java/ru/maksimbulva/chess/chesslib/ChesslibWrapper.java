package ru.maksimbulva.chess.chesslib;

public class ChesslibWrapper {

    static {
        System.loadLibrary("chess-jni");
    }

    public native String getMsgFromJni();
}
