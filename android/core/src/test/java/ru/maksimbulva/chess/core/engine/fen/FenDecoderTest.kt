package ru.maksimbulva.chess.core.engine.fen

import org.junit.Assert.*
import org.junit.Test
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.PieceOnBoard

internal class FenDecoderTest {

    @Test
    fun decodeBoard() {
        val board = FenDecoder.decode(initialPosition).board
        for (row in 2..5) {
            for (column in 0..7) {
                assertTrue(board.isEmpty(row, column))
            }
        }
        assertPiece(Player.White, Piece.Rook, board.pieceAt(Cell.of("a1")))
        assertPiece(Player.White, Piece.Bishop, board.pieceAt(Cell.of("f1")))
        assertPiece(Player.White, Piece.Pawn, board.pieceAt(Cell.of("e2")))
        assertNull(board.pieceAt(Cell.of("d4")))
        assertPiece(Player.Black, Piece.King, board.pieceAt(Cell.of("e8")))
        assertPiece(Player.Black, Piece.Queen, board.pieceAt(Cell.of("d8")))
        assertPiece(Player.Black, Piece.Knight, board.pieceAt(Cell.of("g8")))
    }

    @Test
    fun decodePlayerToMove() {
        assertEquals(Player.White, FenDecoder.decode(initialPosition).playerToMove)
        assertEquals(Player.Black, FenDecoder.decode(endgamePosition).playerToMove)
    }

    @Test
    fun decodeCastlingAvailability() {
        with(FenDecoder.decode(initialPosition)) {
            assertTrue(whiteCastlingAvailability.canCastleShort)
            assertTrue(whiteCastlingAvailability.canCastleLong)
            assertTrue(blackCastlingAvailability.canCastleShort)
            assertTrue(blackCastlingAvailability.canCastleLong)
        }

        with(FenDecoder.decode(initialPosition.replace("KQkq", "Qk"))) {
            assertFalse(whiteCastlingAvailability.canCastleShort)
            assertTrue(whiteCastlingAvailability.canCastleLong)
            assertTrue(blackCastlingAvailability.canCastleShort)
            assertFalse(blackCastlingAvailability.canCastleLong)
        }

        with(FenDecoder.decode(endgamePosition)) {
            assertFalse(whiteCastlingAvailability.canCastleShort)
            assertFalse(whiteCastlingAvailability.canCastleLong)
            assertFalse(blackCastlingAvailability.canCastleShort)
            assertFalse(blackCastlingAvailability.canCastleLong)
        }
    }

    @Test
    fun decodeEnPassantCaptureAvailability() {
        assertFalse(FenDecoder.decode(initialPosition).isCanCaptureEnPassant)
        assertFalse(FenDecoder.decode(endgamePosition).isCanCaptureEnPassant)

        assertTrue(
            FenDecoder.decode(
                "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"
            ).isCanCaptureEnPassant
        )
    }

    @Test
    fun decodeMoveCounters() {
        val decoded = FenDecoder.decode("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
        assertEquals(1, decoded.halfMoveClock)
        assertEquals(2, decoded.fullMoveNumber)
    }

    companion object {
        private const val initialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        private const val endgamePosition = "6k1/5p2/6p1/8/7p/8/6PP/6K1 b - - 0 0"

        private fun assertPiece(player: Player, piece: Piece, pieceOnBoard: PieceOnBoard?) {
            assertEquals(player, pieceOnBoard?.player)
            assertEquals(piece, pieceOnBoard?.piece)
        }
    }
}
