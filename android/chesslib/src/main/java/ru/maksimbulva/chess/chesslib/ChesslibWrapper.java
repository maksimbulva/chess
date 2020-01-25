package ru.maksimbulva.chess.chesslib;

public class ChesslibWrapper {

    private long enginePointer = createEngineInstance();

    static {
        System.loadLibrary("chess-jni");
    }

    public void destroy() {
    }

    public native long calculateLegalMovesCount(String fen, int depthPly);

    public native void resetGame(long enginePointer);

    public native void resetGame(String fen, long enginePointer);

    private native long createEngineInstance();

    private native void releaseEngineInstance(long enginePointer);
}
