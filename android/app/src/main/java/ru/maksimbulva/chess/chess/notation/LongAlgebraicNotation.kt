package ru.maksimbulva.chess.chess.notation

import android.content.res.Resources
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.move.DetailedMove

object LongAlgebraicNotation : BaseNotation() {

    override fun moveToString(resources: Resources, detailedMove: DetailedMove): String {
        return with (detailedMove) {
            when {
                isShortCastle -> resources.getString(R.string.notation_short_castle)
                isLongCastle -> resources.getString(R.string.notation_long_castle)
                else -> {
                    val sb = StringBuilder()
                    sb.append(resources.getString(pieceToMoveString(pieceToMove)))
                        .append(fromCell)
                        .append(
                            resources.getString(
                                if (detailedMove.isCapture) {
                                    R.string.notation_move_capture
                                } else {
                                    R.string.notation_move_no_capture
                                }
                            )
                        )
                        .append(toCell)
                    if (isEnPassantCapture) {
                        sb.append(resources.getString(
                            R.string.notation_move_suffix_en_passant_capture
                        ))
                    }
                    if (promoteTo != null) {
                        sb.append(resources.getString(
                            R.string.notation_move_suffix_promotion,
                            resources.getString(pieceToMoveString(pieceToMove)).toLowerCase()
                        ))
                    }
                    if (isCheckmate) {
                        sb.append(resources.getString(R.string.notation_checkmate))
                    } else if (isCheck) {
                        sb.append(resources.getString(R.string.notation_check))
                    }
                    sb.toString()
                }
            }
        }
    }
}
