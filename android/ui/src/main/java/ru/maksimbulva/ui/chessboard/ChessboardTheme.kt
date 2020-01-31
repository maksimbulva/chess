package ru.maksimbulva.ui.chessboard

import androidx.annotation.DrawableRes
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import java.util.*

class ChessboardTheme(
    @DrawableRes val squareDark: Int,
    @DrawableRes val squareLight: Int,
    private val pieces: EnumMap<Player, EnumMap<Piece, Int>>
) {
    @DrawableRes fun pieceImage(player: Player, piece: Piece): Int {
        return pieces.getValue(player).getValue(piece)
    }
}
