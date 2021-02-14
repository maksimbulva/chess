using ChessEngine.AI.Evaluation;
using ChessEngine.Move.Generator;
using Optional;

namespace ChessEngine.AI.Search
{
    internal sealed class SearchManager
    {
        public Option<Move.Move> FindBestMove(
            Position.Position currentPosition,
            Evaluator evaluator)
        {
            evaluator.OnNewSearch(currentPosition);

            var bestMove = Move.Move.NullMove;
            var bestMoveEvaluation = (currentPosition.PlayerToMove == Player.Black) ?
                int.MaxValue :
                int.MinValue;

            var playerToMove = currentPosition.PlayerToMove;
            var legalMoves = MoveGenerator.GenerateLegalMoves(currentPosition);
            foreach (var move in legalMoves)
            {
                currentPosition.PlayMove(move);
                evaluator.OnMovePlayed(move, playerToMove);
                var currentEvaluation = evaluator.GetCurrentEvaluation();
                if (IsBetterEvaluationValue(playerToMove, currentEvaluation, bestMoveEvaluation))
                {
                    bestMove = move;
                    bestMoveEvaluation = currentEvaluation;
                }
                currentPosition.UndoMove();
                evaluator.OnUndoMove();
            }

            return bestMove.IsNullMove ? Option.None<Move.Move>() : Option.Some(bestMove);
        }

        private static bool IsBetterEvaluationValue(Player player, int newValue, int oldValue)
        {
            if (player == Player.Black)
            {
                return newValue < oldValue;
            }
            else
            {
                return newValue > oldValue;
            }
        }
    }
}
