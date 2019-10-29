package pgn

import chess.engine.Engine

internal object PgnGameFactory {

    fun createGame(tags: Map<String, String>, unparsedMoves: List<String>): PgnGame {
        val engine = Engine()

        unparsedMoves.forEach { unparsedMove ->
            val moveNumberDotIndex = unparsedMove.indexOf('.')

            val moveString = if (moveNumberDotIndex >= 0) {
                unparsedMove.substring(moveNumberDotIndex + 1)
            } else {
                unparsedMove
            }

            val parsedMove = PgnMoveParser.parseMove(moveString)

            val currentPosition = engine.currentPosition
            val move = engine.legalMoves
                .firstOrNull { move -> parsedMove.isMatch(move, currentPosition) }
                ?: throw PgnParseException("Cannot find legal move which matches pgn move $moveString")
                    /*piece == board.pieceAt(move.fromCell)?.piece &&
                            move.toCell == toCell &&
                            (fromCell == null || move.fromCell == fromCell) &&
                            (fromRow == null || move.fromCell.row == fromRow) &&
                            (fromColumn == null || move.fromCell.column == fromColumn)
                     */

            engine.playMove(move)
        }

        return PgnGame(tags.toMap(), engine.moveHistory)
    }

}