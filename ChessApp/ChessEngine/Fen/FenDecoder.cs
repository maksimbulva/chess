using ChessEngine.Board;
using ChessEngine.Position;
using Optional;
using System;
using System.Collections.Generic;

namespace ChessEngine.Fen
{
    public static class FenDecoder
    {
        public static Position.Position Decode(string encoded)
        {
            var splited = encoded.Split(' ');
            var pieces = DecodePieces(splited[0]);
            var playerToMove = DecodePlayerToMove(splited[1]);
            var whiteCastlingAvailability = DecodeCastlingAvailability(splited[2], Player.White);
            var blackCastlingAvailability = DecodeCastlingAvailability(splited[2], Player.Black);
            var enPassantCaptureColumn = DecodeEnPassantCaptureAvailability(splited[3]);
            var halfMoveClock = (splited.Length > 4) ? int.Parse(splited[4]) : 0;
            var fullMoveNumber = (splited.Length > 5) ? int.Parse(splited[5]) : 0;

            return new Position.Position(
                pieces,
                playerToMove,
                whiteCastlingAvailability,
                blackCastlingAvailability,
                enPassantCaptureColumn,
                halfMoveClock,
                fullMoveNumber);
        }

        private static List<PieceOnBoard> DecodePieces(String encoded)
        {
            var encodedRows = encoded.Split(FenFormat.RowSeparator);
            if (encodedRows.Length != Board.Board.RowCount)
            {
                throw new ArgumentException($"The number of rows in {encoded} must be exactly " +
                    $"{Board.Board.RowCount}");
            }

            List<PieceOnBoard> decodedPieces = new List<PieceOnBoard>();

            int currentRow = Board.Board.RowCount - 1;
            foreach (var currentEncodedRow in encodedRows)
            {
                int currentColumn = 0;
                foreach (var currentChar in currentEncodedRow)
                {
                    if (Char.IsDigit(currentChar))
                    {
                        currentColumn += currentChar - '0';
                    }
                    else
                    {
                        var currentPiece = DecodePiece(currentChar);
                        decodedPieces.Add(
                            new PieceOnBoard(
                                currentPiece.player,
                                currentPiece.piece,
                                new BoardSquare(currentRow, currentColumn)));
                        ++currentColumn;
                    }
                }
                if (currentColumn != Board.Board.ColumnCount)
                {
                    throw new ArgumentException($"Cannot parse board row {currentEncodedRow}");
                }
                --currentRow;
            }

            return decodedPieces;
        }

        private static Player DecodePlayerToMove(String encoded)
        {
            switch (encoded)
            {
                case FenFormat.BlackToMove:
                    return Player.Black;
                case FenFormat.WhiteToMove:
                    return Player.White;
                default:
                    throw new Exception($"Cannot parse player to move from {encoded}");
            }
        }

        private static CastlingAvailability DecodeCastlingAvailability(String encoded, Player player)
        {
            var canCastleShortChar = (player == Player.Black)
                ? FenFormat.CanCastleShort : FenFormat.CanCastleShort.ToUpper();
            var canCastleLongChar = (player == Player.Black)
                ? FenFormat.CanCastleLong : FenFormat.CanCastleLong.ToUpper();
            return new CastlingAvailability(
                canCastleShort: encoded.Contains(canCastleShortChar),
                canCastleLong: encoded.Contains(canCastleLongChar));
        }

        private static Option<int> DecodeEnPassantCaptureAvailability(String encoded)
        {
            if (encoded == FenFormat.CannotCaptureEnPassant)
            {
                return Option.None<int>();
            }
            else
            {
                return Option.Some(BoardNotation.CharToColumn(encoded[0]));
            }
        }

        private static ColoredPiece DecodePiece(char encodedPiece)
        {
            var player = Char.IsUpper(encodedPiece) ? Player.White : Player.Black;
            var piece = FenFormat.DecodePiece(Char.ToLower(encodedPiece));
            return new ColoredPiece(player, piece);
        }
    }
}

/*
 * package ru.maksimbulva.chess.core.engine.fen

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.position.CastlingAvailability
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.core.extensions.toUpperCaseIf
import java.util.*

object FenDecoder {

    // Decodes chess positions into Forsyth–Edwards notation format
    fun decode(encoded: String): Position {
        val splited = encoded.split(' ')
        val board = decodeBoard(splited[0])
        val playerToMove = decodePlayerToMove(splited[1])
        val whiteCastlingAvailability = decodeCastlingAvailability(splited[2], Player.White)
        val blackCastlingAvailability = decodeCastlingAvailability(splited[2], Player.Black)
        val enPassantCaptureColumn = decodeEnPassantCaptureAvailability(splited[3])
        val halfMoveClock = if (splited.size > 4) splited[4].toInt() else 0
        val fullMoveNumber = if (splited.size > 5) splited[5].toInt() else 0

        return Position(
            board,
            playerToMove,
            whiteCastlingAvailability,
            blackCastlingAvailability,
            enPassantCaptureColumn,
            halfMoveClock,
            fullMoveNumber
        )
    }

    private fun decodeBoard(encoded: String): Board {

    }

    private fun decodePlayerToMove(encoded: String): Player {

    }

    private fun decodeCastlingAvailability(encoded: String, player: Player): CastlingAvailability {

    }

    private fun decodeEnPassantCaptureAvailability(encoded: String): Int? {
    }

    private fun decodePiece(char: Char): ColoredPiece {

    }
}*/
