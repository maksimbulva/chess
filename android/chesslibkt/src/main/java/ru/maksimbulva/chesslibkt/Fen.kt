package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.PieceOnBoard
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.position.CastleOptions
import ru.maksimbulva.chesslibkt.position.Position
import ru.maksimbulva.chesslibkt.position.PositionFactory
import ru.maksimbulva.chesslibkt.position.PositionMoveCounters

object Fen {
    private const val ROWS_SEPARATOR = '/'
    private const val FEN_WHITE_TO_MOVE = 'w'
    private const val FEN_BLACK_TO_MOVE = 'b'
    private const val FEN_NO_ONE_CAN_CASTLE = '-'
    private const val FEN_WHITE_CAN_CASTLE_SHORT = 'K'
    private const val FEN_WHITE_CAN_CASTLE_LONG = 'Q'
    private const val FEN_BLACK_CAN_CASTLE_SHORT = 'k'
    private const val FEN_BLACK_CAN_CASTLE_LONG = 'q'
    private const val FEN_CANNOT_CAPTURE_EN_PASSANT = '-'

    private val pieceToChar = mapOf(
        Piece.Pawn to 'p',
        Piece.Knight to 'n',
        Piece.Bishop to 'b',
        Piece.Rook to 'r',
        Piece.Queen to 'q',
        Piece.King to 'k'
    )

    fun decodeFen(fenString: String): Position {
        val tokens = fenString.split(' ')
        return PositionFactory.createPosition(
            pieces = decodeBoard(tokens[0]),
            playerToMove = decodePlayerToMove(tokens[1]),
            castleOptions = decodeCastleOptions(tokens[2]),
            enPassantColumn = decodeEnPassantColumn(tokens[3]),
            halfmoveClock = tokens.takeIf { tokens.size > 4 }
                .run { this?.let { decodeHalfmoveClock(it[4]) } ?: 0 },
            fullmoveNumber = tokens.takeIf { tokens.size > 5 }
                .run { this?.let { decodeFullmoveNumber(it[5]) } ?: 0 }
        )
    }

    fun encodeFen(position: Position): String {
        return listOf(
            encodeBoard(position.board),
            encodePlayerToMove(position.playerToMove),
            encodeCastleOptions(
                mapOf(
                    Player.Black to position.getCastleOptions(Player.Black),
                    Player.White to position.getCastleOptions(Player.White)
                )
            ),
            encodeEnPassantColumn(position.playerToMove, position.enPassantColumn),
            encodeHalfmoveClock(position.halfmoveClock),
            encodeFullmoveNumber(position.fullmoveNumber)
        ).joinToString(separator = "")
    }

    private fun decodeBoard(boardString: String): Collection<PieceOnBoard> {
        val decodedPieces = mutableListOf<PieceOnBoard>()

        val rowStrings = boardString.split(ROWS_SEPARATOR)
        require(rowStrings.size == Board.ROW_COUNT)

        Board.RowsRange.reversed().forEachIndexed { index, currentRow ->
            var currentColumn = 0
            for (c in rowStrings[index]) {
                require(currentColumn < Board.COLUMN_COUNT)
                currentColumn += when {
                    c.isDigit() -> c - '0'
                    else -> {
                        decodedPieces.add(
                            PieceOnBoard(
                                decodePlayer(c),
                                decodePieceType(c),
                                Square(currentRow, currentColumn)
                            )
                        )
                        1
                    }
                }
            }
            require(currentColumn == Board.COLUMN_MAX + 1)
        }

        return decodedPieces
    }

    private fun encodeBoard(board: Board): String
    {
        TODO()
        /*std::string encodedBoard;

        for (square_t currentRow = MAX_ROW; currentRow >= 0; --currentRow) {
        std::string encodedRow;
        fastint emptySquaresCounter = 0;
        for (square_t currentColumn = 0; currentColumn < COLUMN_COUNT; ++currentColumn) {
        const square_t currentSquare = encodeSquare(currentRow, currentColumn);
        if (board.isEmpty(currentSquare)) {
            ++emptySquaresCounter;
        }
        else {
            if (emptySquaresCounter != 0) {
                encodedRow += std::to_string(emptySquaresCounter);
                emptySquaresCounter = 0;
            }
            encodedRow.push_back(encodePlayerPieceType(
                board.getPlayer(currentSquare),
                board.getPieceTypeAt(currentSquare)));
        }
    }
        if (emptySquaresCounter != 0) {
            encodedRow += std::to_string(emptySquaresCounter);
        }
        encodedBoard += encodedRow;
        if (currentRow != 0) {
            encodedBoard.push_back(ROWS_SEPARATOR);
        }
    }

        return encodedBoard;*/
    }

    private fun decodePlayer(c: Char): Player {
        return if (c.isLowerCase()) {
            Player.Black
        } else {
            Player.White
        }
    }

    private fun decodePieceType(c: Char): Piece {
        val pieceCharLower = c.toLowerCase()
        return pieceToChar.filter { it.value == pieceCharLower }.map { it.key }.first()
    }

    private fun encodePlayerPieceType(player: Player, piece: Piece): Char {
        return pieceToChar.getValue(piece).run {
            when (player) {
                Player.Black -> this
                Player.White -> toUpperCase()
            }
        }
    }

    private fun decodePlayerToMove(encoded: String): Player {
        require(encoded.length == 1)
        return when (encoded.first().toLowerCase()) {
            FEN_BLACK_TO_MOVE -> Player.Black
            FEN_WHITE_TO_MOVE -> Player.White
            else -> throw ChesslibException()
        }
    }

    private fun encodePlayerToMove(playerToMove: Player): Char {
        return when (playerToMove) {
            Player.Black -> FEN_BLACK_TO_MOVE
            Player.White -> FEN_WHITE_TO_MOVE
        }
    }

    private fun decodeCastleOptions(encoded: String): Map<Player, CastleOptions> {
        val blackCastleOptions = CastleOptions.None
            .run {
                if (FEN_BLACK_CAN_CASTLE_SHORT in encoded) {
                    setCanCastleShort(true)
                } else {
                    this
                }
            }
            .run {
                if (FEN_BLACK_CAN_CASTLE_LONG in encoded) {
                    setCanCastleLong(true)
                } else {
                    this
                }
            }

        val whiteCastleOptions = CastleOptions.None
            .run {
                if (FEN_WHITE_CAN_CASTLE_SHORT in encoded) {
                    setCanCastleShort(true)
                } else {
                    this
                }
            }
            .run {
                if (FEN_WHITE_CAN_CASTLE_LONG in encoded) {
                    setCanCastleLong(true)
                } else {
                    this
                }
            }

        return mapOf(
            Player.Black to blackCastleOptions,
            Player.White to whiteCastleOptions
        )
    }

    private fun encodeCastleOptions(castleOptionsByPlayer: Map<Player, CastleOptions>): String {
        return with (castleOptionsByPlayer) {
            listOfNotNull(
                FEN_BLACK_CAN_CASTLE_SHORT.takeIf { getValue(Player.Black).isCanCastleShort },
                FEN_BLACK_CAN_CASTLE_LONG.takeIf { getValue(Player.Black).isCanCastleLong },
                FEN_WHITE_CAN_CASTLE_SHORT.takeIf { getValue(Player.White).isCanCastleShort },
                FEN_WHITE_CAN_CASTLE_LONG.takeIf { getValue(Player.White).isCanCastleLong }
            ).joinToString(separator = "").run {
                if (isEmpty()) FEN_NO_ONE_CAN_CASTLE.toString() else this
            }
        }
    }

    private fun decodeEnPassantColumn(encoded: String): Int? {
        return if (encoded.length > 1) {
            (encoded.first().toLowerCase() - 'a').also {
                check(it in 0..Board.COLUMN_MAX)
            }
        } else {
            null
        }
    }

    private fun encodeEnPassantColumn(playerToMove: Player, enPassantColumn: Int?): String {
        return if (enPassantColumn != null) {
            val columnChar = 'a' + enPassantColumn
            val rowChar = when (playerToMove) {
                Player.Black -> '3'
                Player.White -> '6'
            }
            "$columnChar$rowChar"
        } else {
            FEN_CANNOT_CAPTURE_EN_PASSANT.toString()
        }
    }

    private fun decodeHalfmoveClock(encoded: String): Int {
        return encoded.toInt()
    }

    private fun encodeHalfmoveClock(halfmoveClock: Int): String {
        return halfmoveClock.toString()
    }

    private fun decodeFullmoveNumber(encoded: String): Int {
        return encoded.toInt()
    }

    private fun encodeFullmoveNumber(fullmoveNumber: Int): String {
        return fullmoveNumber.toString()
    }
}
