using ChessEngine.Board;
using System.Collections.Generic;
using System.Linq;

namespace ChessEngine.AI.Evaluation.Factor
{
    internal sealed class MaterialFactorEvaluation
    {
        private readonly int[] pieceValue = CreateDefaultPieceValues();

        public int Evaluate(IEnumerable<PieceOnBoard> playerPieces)
        {
            return playerPieces.Sum(x => GetPieceValue(x.piece));
        }

        public int CalculateEvaluationDeltaOnMyMove(Move.Move move)
        {
            if (move.IsPromotion)
            {
                var newPieceValue = GetPieceValue(move.GetPromoteToPiece());
                return newPieceValue - GetPieceValue(Piece.Pawn);
            }
            return 0;
        }

        public int CalcaulateEvaluationDeltaOnTheirMove(Move.Move move)
        {
            if (move.IsCapture)
            {
                return -GetPieceValue(move.GetCapturedPiece());
            }
            return 0;
        }

        private int GetPieceValue(Piece piece) => pieceValue[(int)piece];

        private static int[] CreateDefaultPieceValues()
        {
            var result = new int[(int)Piece.King + 1];
            result[(int)Piece.Pawn] = 100;
            result[(int)Piece.Knight] = 300;
            result[(int)Piece.Bishop] = 300;
            result[(int)Piece.Rook] = 500;
            result[(int)Piece.Queen] = 900;
            result[(int)Piece.King] = 0;
            return result;
        }
    }
}
