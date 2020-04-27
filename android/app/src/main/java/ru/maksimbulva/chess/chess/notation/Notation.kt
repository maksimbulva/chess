package ru.maksimbulva.chess.chess.notation

import android.content.res.Resources
import androidx.annotation.StringRes
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove

interface Notation {
    fun moveToString(resources: Resources, detailedMove: DetailedMove): String
    fun moveToStringWithMoveNumber(resources: Resources, detailedMove: DetailedMove): String
}

abstract class BaseNotation : Notation {

    override fun moveToStringWithMoveNumber(
        resources: Resources,
        detailedMove: DetailedMove
    ): String {
        return with (detailedMove) {
            resources.getString(
                if (playerToMove == Player.Black) {
                    R.string.action_bar_move_black_template
                } else {
                    R.string.action_bar_move_white_template
                },
                fullmoveNumber,
                moveToString(resources, detailedMove)
            )
        }
    }

    @StringRes
    protected fun pieceToMoveString(piece: Piece) = when (piece) {
        Piece.Pawn -> R.string.notation_pawn
        Piece.Knight -> R.string.notation_knight
        Piece.Bishop -> R.string.notation_bishop
        Piece.Rook -> R.string.notation_rook
        Piece.Queen -> R.string.notation_queen
        Piece.King -> R.string.notation_king
    }
}
