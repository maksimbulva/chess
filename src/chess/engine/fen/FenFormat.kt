package chess.engine.fen

object FenFormat {
    const val rowSeparator = "/"
    const val blackToMove = "b"
    const val whiteToMove = "w"
    const val canCastleShort = "k"
    const val canCastleLong = "q"
    const val noOneCanCastle = "-"
    const val cannotCaptureEnPassant = "-"

    const val whiteEnPassantCaptureRow = "6"
    const val blackEnPassantCaptureRow = "3"
}