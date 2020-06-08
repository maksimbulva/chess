package ru.maksimbulva.chesslibkt.fen

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.maksimbulva.chesslibkt.ChessEngineImpl
import ru.maksimbulva.chesslibkt.Fen
import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.Square

class FenDecoderTest {

    @Test
    fun decodeBoard() {
        val position = Fen.decodeFen(ChessEngineImpl.INITIAL_POSITION_FEN)
        val board = position.board
        for (row in 2..5) {
            for (column in Board.ColumnsRange) {
                Assert.assertTrue(board.isEmpty(Square(row, column)))
            }
        }

        assertPlayerPiece(Player.White, Piece.Rook, board, Square.of("a1"))
        assertPlayerPiece(Player.White, Piece.Bishop, board, Square.of("f1"))
        assertPlayerPiece(Player.White, Piece.Pawn, board, Square.of("e2"))
        assertPlayerPiece(Player.Black, Piece.King, board, Square.of("e8"))
        assertPlayerPiece(Player.Black, Piece.Queen, board, Square.of("d8"))
        assertPlayerPiece(Player.Black, Piece.Knight, board, Square.of("g8"))

        Assert.assertNull(board.getPieceTypeAt(Square.of("d4")))
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
}
