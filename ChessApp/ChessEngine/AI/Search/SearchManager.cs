using ChessEngine.AI.Evaluation;
using ChessEngine.Move.Generator;
using Optional;
using static ChessEngine.Board.BoardAttackUtils;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.AI.Search
{
    internal sealed class SearchManager
    {
        private readonly Evaluator evaluator;

        public SearchManager(Evaluator evaluator)
        {
            this.evaluator = evaluator;
        }

        public Option<Move.Move> FindBestMove(Position.Position currentPosition)
        {
            evaluator.OnNewSearch(currentPosition);

            Option<Move.Move> bestMove = Option.None<Move.Move>();
            int bestMoveEvaluation = -int.MaxValue;

            foreach (var move in MoveGenerator.GenerateLegalMoves(currentPosition))
            {
                currentPosition.PlayMove(move);
                var moveEvaluation = -CalculateEvaluationRecursively(
                    currentPosition,
                    depthLeft: 3);
                if (!bestMove.HasValue || moveEvaluation > bestMoveEvaluation)
                {
                    bestMove = Option.Some(move);
                    bestMoveEvaluation = moveEvaluation;
                }
                currentPosition.UndoMove();
            }

            return bestMove;
        }

        private int CalculateEvaluationRecursively(
            Position.Position position,
            int depthLeft)
        {
            var bestMove = Move.Move.NullMove;
            var bestMoveEvaluation = -int.MaxValue;
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
                evaluator.OnMovePlayed(move, playerToMove);

                int moveEvaluation;
                if (depthLeft == 1)
                {
                    moveEvaluation = playerEvaluationMultiplier * evaluator.GetCurrentEvaluation();
                }
                else
                {
                    moveEvaluation = -CalculateEvaluationRecursively(
                        position,
                        depthLeft - 1);
                }

                if (moveEvaluation >= bestMoveEvaluation)
                {
                    bestMove = move;
                    bestMoveEvaluation = moveEvaluation;
                }

                position.UndoMove();
                evaluator.OnUndoMove();
            }

            if (!hasLegalMove)
            {
                return GetEvaluationForPositionWithoutLegalMoves(
                    position,
                    playerEvaluationMultiplier);
            }

            return bestMoveEvaluation;
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
    }
}
