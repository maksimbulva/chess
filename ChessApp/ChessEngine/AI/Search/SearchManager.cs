using ChessEngine.AI.Evaluation;
using ChessEngine.Move.Generator;
using Optional;
using static ChessEngine.Board.BoardAttackUtils;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.AI.Search
{
    internal sealed class SearchManager
    {
        private const int MinEvaluationValue = -int.MaxValue;

        private const int MinPriorityToConsiderMove = 0;
        private const int PriorityPerDepthLevel = 100;
        private const int PriorityBonusMoveChangesMaterial = 1000;

        private readonly Evaluator evaluator;
        private readonly int recommendedMaxDepth;

        public SearchManager(
            Evaluator evaluator,
            int recommendedMaxDepth)
        {
            this.evaluator = evaluator;
            this.recommendedMaxDepth = recommendedMaxDepth;
        }

        public Option<Move.Move> FindBestMove(Position.Position currentPosition)
        {
            evaluator.OnNewSearch(currentPosition);

            Option<Move.Move> bestMove = Option.None<Move.Move>();
            int bestMoveEvaluation = MinEvaluationValue;

            foreach (var move in MoveGenerator.GenerateLegalMoves(currentPosition))
            {
                currentPosition.PlayMove(move);

                var evaluation = -CalculateEvaluationRecursively(
                    currentPosition,
                    currentDepth: 1)
                    .ValueOr(-MinEvaluationValue);

                if (!bestMove.HasValue || evaluation >= bestMoveEvaluation)
                {
                    bestMove = Option.Some(move);
                    bestMoveEvaluation = evaluation;
                }

                currentPosition.UndoMove();
            }

            return bestMove;
        }

        private Option<int> CalculateEvaluationRecursively(
            Position.Position position,
            int currentDepth)
        {
            var bestMove = Move.Move.NullMove;
            var bestMoveEvaluation = MinEvaluationValue;
            bool hasLegalMove = false;

            var playerEvaluationMultiplier = GetPlayerEvaluationMultiplier(position.PlayerToMove);

            var playerToMove = position.PlayerToMove;
            var pseudoLegalMoves = MoveGenerator.GeneratePseudoLegalMoves(position);
            foreach (var move in pseudoLegalMoves)
            {
                position.PlayMove(move);
                if (!IsPlayedMoveLegal(position))
                {
                    position.UndoMove();
                    continue;
                }

                hasLegalMove = true;
                int movePriority = CalculateMovePriority(move, currentDepth);
                if (movePriority < MinPriorityToConsiderMove)
                {
                    position.UndoMove();
                    continue;
                }

                evaluator.OnMovePlayed(move, playerToMove);

                var evaluation = -CalculateEvaluationRecursively(
                    position,
                    currentDepth + 1)
                    .ValueOr(() => -playerEvaluationMultiplier * evaluator.GetCurrentEvaluation());

                if (evaluation >= bestMoveEvaluation)
                {
                    bestMove = move;
                    bestMoveEvaluation = evaluation;
                }

                position.UndoMove();
                evaluator.OnUndoMove();
            }

            if (!hasLegalMove)
            {
                return Option.Some(
                    GetEvaluationForPositionWithoutLegalMoves(
                        position,
                        playerEvaluationMultiplier));
            }

            return bestMove.IsNullMove ? Option.None<int>() : Option.Some(bestMoveEvaluation);
        }

        private int CalculateMovePriority(
            Move.Move move,
            int currentDepth)
        {
            int result = PriorityPerDepthLevel * (recommendedMaxDepth - currentDepth);
            if (IsMoveChangesMaterial(move))
            {
                result += PriorityBonusMoveChangesMaterial;
            }
            return result;
        }

        private static bool IsPlayedMoveLegal(Position.Position positionAfterMovePlayed)
        {
            var playedWhoPlayedMove = GetOtherPlayer(positionAfterMovePlayed.PlayerToMove);
            return !IsKingUnderAttack(positionAfterMovePlayed, playedWhoPlayedMove);
        }

        private static bool IsKingUnderAttack(
            Position.Position position,
            Player playerToCheck)
        {
            var board = position.Board;
            var kingSquare = board.GetKingSquare(playerToCheck);
            return IsBoardSquareAttacked(
                kingSquare,
                board,
                GetOtherPlayer(playerToCheck));
        }

        private static int GetPlayerEvaluationMultiplier(Player playerToMove)
        {
            return playerToMove == Player.Black ? -1 : 1;
        }

        private static int GetEvaluationForPositionWithoutLegalMoves(
            Position.Position position,
            int playerEvaluationMultiplier)
        {
            return playerEvaluationMultiplier *
                (IsKingUnderAttack(position, position.PlayerToMove) ?
                    Evaluator.GetCheckmatedEvaluation() :
                    Evaluator.StalemateEvaluation);
        }

        private static bool IsMoveChangesMaterial(Move.Move move)
        {
            return move.IsCapture || move.IsPromotion;
        }
    }
}
