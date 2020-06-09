package ru.maksimbulva.chesslibkt.fen

import org.junit.Assert.*
import org.junit.Test
import ru.maksimbulva.chesslibkt.Fen
import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.Square

class FenDecoderTest {

    @Test
    fun decodeBoard() {
        val position = Fen.decodeFen(initialPosition)
        val board = position.board
        for (row in 2..5) {
            for (column in Board.ColumnsRange) {
                assertTrue(board.isEmpty(Square(row, column)))
            }
        }

        assertPlayerPiece(Player.White, Piece.Rook, board, Square.of("a1"))
        assertPlayerPiece(Player.White, Piece.Bishop, board, Square.of("f1"))
        assertPlayerPiece(Player.White, Piece.Pawn, board, Square.of("e2"))
        assertPlayerPiece(Player.Black, Piece.King, board, Square.of("e8"))
        assertPlayerPiece(Player.Black, Piece.Queen, board, Square.of("d8"))
        assertPlayerPiece(Player.Black, Piece.Knight, board, Square.of("g8"))

        assertNull(board.getPieceTypeAt(Square.of("d4")))
    }

    @Test
    fun decodePlayerToMove() {
        assertEquals(Player.White, Fen.decodeFen(initialPosition).playerToMove)
        assertEquals(Player.Black, Fen.decodeFen(endgamePosition).playerToMove)
    }
    @Test
    fun decodeCastlingAvailability() {
        with (Fen.decodeFen(initialPosition)) {
            assertTrue(getCastleOptions(Player.White).isCanCastleShort)
            assertTrue(getCastleOptions(Player.White).isCanCastleLong)
            assertTrue(getCastleOptions(Player.Black).isCanCastleShort)
            assertTrue(getCastleOptions(Player.Black).isCanCastleLong)
        }

        with(Fen.decodeFen(initialPosition.replace("KQkq", "Qk"))) {
            assertFalse(getCastleOptions(Player.White).isCanCastleShort)
            assertTrue(getCastleOptions(Player.White).isCanCastleLong)
            assertTrue(getCastleOptions(Player.Black).isCanCastleShort)
            assertFalse(getCastleOptions(Player.Black).isCanCastleLong)
        }

        with(Fen.decodeFen(endgamePosition)) {
            assertFalse(getCastleOptions(Player.White).isCanCastleShort)
            assertFalse(getCastleOptions(Player.White).isCanCastleLong)
            assertFalse(getCastleOptions(Player.Black).isCanCastleShort)
            assertFalse(getCastleOptions(Player.Black).isCanCastleLong)
        }
    }

    @Test
    fun decodeEnPassantCaptureAvailability() {
        assertNull(Fen.decodeFen(initialPosition).enPassantColumn)
        assertNull(Fen.decodeFen(endgamePosition).enPassantColumn)

        assertNotNull(
            Fen.decodeFen(
                "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"
            ).enPassantColumn
        )
    }

    @Test
    fun decodeMoveCounters() {
        val decoded = Fen.decodeFen("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
        assertEquals(1, decoded.halfmoveClock)
        assertEquals(2, decoded.fullmoveNumber)
    }

    private fun assertPlayerPiece(
        expectedPlayer: Player,
        expectedPiece: Piece,
        board: Board,
        square: Square
    ) {
        assertEquals(expectedPlayer, board.getPlayer(square))
        assertEquals(expectedPiece, board.getPieceTypeAt(square))
    }

    companion object {
        private const val initialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        private const val endgamePosition = "6k1/5p2/6p1/8/7p/8/6PP/6K1 b - - 0 0"
    }
}
