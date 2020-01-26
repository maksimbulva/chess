package ru.maksimbulva.chess.chesslib;

public abstract class AbsChesslibWrapper {

    static {
        System.loadLibrary("chess-jni");
    }

    public native long calculateLegalMovesCount(String fen, int depthPly);

    protected native void resetGame(long enginePointer);

    protected native void resetGame(String fen, long enginePointer);

    protected native String getCurrentPositionFen(long enginePointer);

    protected native boolean playMove(String move, long enginePointer);

    protected native String findBestVariation(long enginePointer);

    protected native long createEngineInstance();

    protected native void releaseEngineInstance(long enginePointer);
}
