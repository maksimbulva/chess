using ChessEngine;
using ChessEngine.Board;
using Optional;
using System;
using static ChessEngine.Board.BoardNotation;

namespace UCIConsoleApp
{
    internal static class UciMoveParser
    {
        public struct ParsedMove
        {
            public BoardSquare OriginSquare { get; }
            public BoardSquare DestSquare { get; }
            public Option<Piece> PromoteToPiece { get; }

            public ParsedMove(
                BoardSquare originSquare,
                BoardSquare destSquare,
                Option<Piece> promoteToPiece)
            {
                OriginSquare = originSquare;
                DestSquare = destSquare;
                PromoteToPiece = promoteToPiece;
            }

            public override string ToString()
            {
                return $"{OriginSquare}-{DestSquare}={PromoteToPiece}";
            }
        }

        public static Option<ParsedMove> ParseUciMove(string uciMove)
        {
            if (uciMove.Length < 4 || uciMove.Length > 5)
            {
                return Option.None<ParsedMove>();
            }
            try
            {
                var originSquare = SquareFromString(uciMove.Substring(0, 2));
                var destSquare = SquareFromString(uciMove.Substring(2, 2));
                var promoteToPiece = uciMove.Length == 5 ?
                     PromotionPieceFromUciNotation(uciMove[4]) :
                     Option.None<Piece>();
                return Option.Some(new ParsedMove(originSquare, destSquare, promoteToPiece));
            }
            catch (ArgumentException)
            {
                return Option.None<ParsedMove>();
            }
        }

        private static Option<Piece> PromotionPieceFromUciNotation(char promotionPieceChar)
        {
            return (char.ToLower(promotionPieceChar)) switch
            {
                'n' => Option.Some(Piece.Knight),
                'b' => Option.Some(Piece.Bishop),
                'r' => Option.Some(Piece.Rook),
                'q' => Option.Some(Piece.Queen),
                _ => Option.None<Piece>(),
            };
        }
    }
}
