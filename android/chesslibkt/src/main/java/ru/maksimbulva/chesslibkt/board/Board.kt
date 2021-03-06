package ru.maksimbulva.chesslibkt.board

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player
import java.lang.IllegalStateException

class Board(blackKingSquare: Square, whiteKingSquare: Square) {

    private val boardSquares = Array<BoardSquare?>(SQUARE_COUNT) { null }

    private var blackKingNode = BoardSquare(blackKingSquare, Player.Black, Piece.King)
    private var whiteKingNode = BoardSquare(whiteKingSquare, Player.White, Piece.King)

    init {
        require(blackKingSquare.encoded != whiteKingSquare.encoded)
        setBoardSquare(blackKingNode, blackKingSquare)
        setBoardSquare(whiteKingNode, whiteKingSquare)
    }

    fun isEmpty(square: Square): Boolean {
        return boardSquares[square.encoded] == null
    }

    fun isNotEmpty(square: Square) = !isEmpty(square)

    fun isNotEmptyAndOtherPlayer(square: Square, player: Player): Boolean {
        return boardSquares[square.encoded]?.player == player
    }

    fun isPlayerPiece(square: Square, player: Player, pieceType: Piece): Boolean {
        val boardSquare = boardSquares[square.encoded]
        return boardSquare != null && boardSquare.player == player && boardSquare.piece == pieceType
    }

    fun isPlayerSlider(square: Square, player: Player, pieceType: Piece): Boolean {
        val boardSquare = boardSquares[square.encoded]
        return if (boardSquare != null) {
            val boardSquarePiece = boardSquare.piece
            boardSquare.player == player || boardSquarePiece == pieceType ||
                    boardSquarePiece == Piece.Queen
        } else {
            false
        }
    }

    fun getPieceTypeAt(square: Square): Piece? {
        return boardSquares[square.encoded]?.piece
    }

    fun getPieceValue(square: Square): Piece = getPieceTypeAt(square)!!

    fun getPlayer(square: Square): Player? {
        return boardSquares[square.encoded]?.player
    }

    fun getPlayerValue(square: Square): Player = getPlayer(square)!!

//    fun getKingSquare(player: Player): Square {
//        TODO()
//    }

//    PieceIterator getPieceIterator(player_t player) const
//    {
//        return PieceIterator(player, *this);
//    }

//    void doForEachPiece(player_t player, std::function<void(piece_type_t pieceType, square_t square)> action) const
//    {
//        auto piecesIt = getPieceIterator(player);
//        while (true) {
//            action(piecesIt.getPieceType(), piecesIt.getSquare());
//            if (piecesIt.hasNext()) {
//                ++piecesIt;
//            }
//            else {
//                break;
//            }
//        }
//    }

    fun getPlayerPieces(player: Player): Sequence<BoardSquare> {
        val startingBoardSquare = when (player) {
            Player.Black -> blackKingNode
            Player.White -> whiteKingNode
        }
        return Sequence { PieceIterator(boardSquares, startingBoardSquare) }
    }

    fun addPiece(pieceOnBoard: PieceOnBoard) {
        require(pieceOnBoard.pieceType != Piece.King)
        require(isEmpty(pieceOnBoard.square))

        val kingNode = getKingNode(pieceOnBoard.player)

        val pieceNode = BoardSquare(
            pieceOnBoard.square,
            pieceOnBoard.player,
            pieceOnBoard.pieceType
        ).apply {
            next = kingNode.next
            prev = kingNode
        }

        kingNode.next?.let {
            it.prev = pieceNode
        }

        kingNode.next = pieceNode

        setBoardSquare(pieceNode, pieceOnBoard.square)
    }

    fun erasePieceAt(square: Square) {
        val boardSquare = boardSquares[square.encoded]!!
        removeFromList(boardSquare)
        boardSquares[square.encoded] = null
    }

    fun updatePieceSquare(oldSquare: Square, newSquare: Square) {
        val boardSquare = boardSquares[oldSquare.encoded] ?: throw IllegalStateException()
        boardSquare.square = newSquare

        boardSquares[oldSquare.encoded] = null
        boardSquares[newSquare.encoded] = boardSquare
    }

    fun promotePawn(square: Square, promoteTo: Piece) {
        boardSquares[square.encoded]!!.piece = promoteTo
    }

    fun demoteToPawn(square: Square) {
        boardSquares[square.encoded]!!.piece = Piece.Pawn
    }

    private fun getKingNode(player: Player): BoardSquare {
        return when (player) {
            Player.Black -> blackKingNode
            Player.White -> whiteKingNode
        }
    }

    private fun setBoardSquare(boardSquare: BoardSquare?, square: Square) {
        boardSquares[square.encoded] = boardSquare
    }

    private fun removeFromList(boardSquare: BoardSquare) {
        boardSquare.prev?.next = boardSquare.next
        boardSquare.next?.prev = boardSquare.prev
    }

    companion object {
        const val COLUMN_MAX = 7
        const val COLUMN_COUNT = COLUMN_MAX + 1

        const val ROW_MAX = 7
        const val ROW_COUNT = ROW_MAX + 1

        const val SQUARE_COUNT = COLUMN_COUNT * ROW_COUNT

        val RowsRange = 0..ROW_MAX
        val ColumnsRange = 0..COLUMN_MAX

        val allSquares: Sequence<Square>
            get() = (0 until SQUARE_COUNT).asSequence().map { Square(it) }

        fun rowOf(rowChar: Char): Int {
            require(rowChar in '1'..'8')
            return rowChar - '1'
        }

        fun columnOf(columnChar: Char): Int {
            require(columnChar in 'a'..'h')
            return columnChar - 'a'
        }

        const val COLUMN_A = 0
        const val COLUMN_D = 3
        const val COLUMN_F = 5
        const val COLUMN_H = 7
    }
}
