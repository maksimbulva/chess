package pgn

import chess.engine.core.Piece

object PgnMoveParser {

    private val piece = "[NBRQK]"
    private val cell = "[a-h][1-8]"
    private val incompleteCell = "[a-h]?[1-8]?"
    private val promotion = "=$piece"
    private val suffix = "[+#]?"

    private val patterns = arrayOf(
        PgnMovePatternParser(Regex("O-O$suffix")) { PgnMove.PgnShortCastle },
        PgnMovePatternParser(Regex("O-O-O$suffix")) { PgnMove.PgnLongCastle },
        PgnMovePatternParser(Regex("($cell)($promotion)?$suffix")) { matchResult ->
            PgnMove.PgnMovePattern(
                piece = Piece.Pawn,
                toCell = PgnCellPattern.of(matchResult.groupValues[1]),
                promoteTo = parsePromoteToPiece(matchResult.groupValues[2])
            )
        },
        PgnMovePatternParser(Regex("($incompleteCell)x($cell)($promotion)?$suffix")) { matchResult ->
            PgnMove.PgnMovePattern(
                piece = Piece.Pawn,
                fromCell = PgnCellPattern.of(matchResult.groupValues[1]),
                toCell = PgnCellPattern.of(matchResult.groupValues[2]),
                promoteTo = parsePromoteToPiece(matchResult.groupValues[3])
            )
        },
        PgnMovePatternParser(Regex("$piece($cell)$suffix")) { matchResult ->
            PgnMove.PgnMovePattern(
                piece = parsePiece(matchResult.value[0]),
                toCell = PgnCellPattern.of(matchResult.groupValues[1])
            )
        },
        PgnMovePatternParser(Regex("$piece($incompleteCell?)x?($cell)$suffix")) { matchResult ->
            PgnMove.PgnMovePattern(
                piece = parsePiece(matchResult.value[0]),
                fromCell = PgnCellPattern.of(matchResult.groupValues[1]),
                toCell = PgnCellPattern.of(matchResult.groupValues[2])
            )
        }
    )

    fun parseMove(moveString: String): PgnMove {
        val pgnMove = patterns.asSequence()
            .map {
                it.regex.matchEntire(moveString)?.let { matchResult -> it.parser(matchResult) }
            }
            .firstOrNull { it != null }

        return pgnMove ?: throw PgnParseException("Cannot find matching pattern for $moveString")
    }

    private fun parsePiece(pieceChar: Char): Piece {
        return when (pieceChar) {
            'N' -> Piece.Knight
            'B' -> Piece.Bishop
            'R' -> Piece.Rook
            'Q' -> Piece.Queen
            'K' -> Piece.King
            else -> throw IllegalArgumentException("Unexpected value $pieceChar")
        }
    }

    private fun parsePromoteToPiece(str: String): Piece? {
        if (str.isEmpty()) return null
        require(str.length == 2 && str[0] == '=')
        val result = parsePiece(str[1])
        if (result == Piece.King) {
            throw PgnParseException("Cannot promote to King")
        }
        return result
    }

    private class PgnMovePatternParser(
        val regex: Regex,
        val parser: (MatchResult) -> PgnMove
    )
}