using ChessEngine.AI.Evaluation.Factor;
using System;
using System.Collections.Generic;

namespace ChessEngine.AI.Evaluation
{
    internal sealed class Evaluator
    {
        public const int StalemateEvaluation = 0;

        private readonly MaterialFactorEvaluation materialEvaluator =
            new MaterialFactorEvaluation();

        private readonly Random rnd = new Random();

        private readonly Stack<FactorsRecord> factorsHistory =
            new Stack<FactorsRecord>(capacity: 48);

        private FactorsRecord CurrentFactorsRecord
        {
            get => factorsHistory.Peek();
        }

        public Evaluator()
        {
        }

        public int GetCurrentEvaluation() => CalculateEvaluation(CurrentFactorsRecord);
        
        public void OnNewSearch(Position.Position currentPosition)
        {
            factorsHistory.Clear();
            factorsHistory.Push(CalculateFactorsForPosition(currentPosition));
        }

        public void OnMovePlayed(Move.Move move, Player player)
        {
            var updatedFactors = CalculateUpdatedFactorsAfterMovePlayed(move, player);
            factorsHistory.Push(updatedFactors);
        }

        public void OnUndoMove()
        {
            factorsHistory.Pop();
        }

        public static int GetCheckmatedEvaluation()
        {
            // TODO: Add checkmate depth as input parameter
            return -int.MaxValue;
        }

        private FactorsRecord CalculateFactorsForPosition(Position.Position position)
        {
            return new FactorsRecord(
                blackPlayerFactors: CalculateFactorsForPlayer(position, Player.Black),
                whitePlayerFactors: CalculateFactorsForPlayer(position, Player.White),
                randomNoiseValue: GenerateRandomNoiseValue());
        }

        private FactorsByPlayer CalculateFactorsForPlayer(
            Position.Position position,
            Player player)
        {
            return new FactorsByPlayer(
                materialValue: materialEvaluator.Evaluate(position.Board.GetPieces(player)));
        }

        private static int CalculateEvaluation(FactorsRecord factors)
        {
            return factors.WhitePlayerFactors.MaterialValue -
                factors.BlackPlayerFactors.MaterialValue +
                factors.RandomNoiseValue;
        }

        private FactorsRecord CalculateUpdatedFactorsAfterMovePlayed(
            Move.Move move,
            Player player)
        {
            var currentFactors = CurrentFactorsRecord;
            var updatedBlackPlayerFactors = UpdateFactorsByPlayer(
                currentFactors.BlackPlayerFactors,
                Player.Black,
                player,
                move);
            var updatedWhitePlayerFactors = UpdateFactorsByPlayer(
                currentFactors.WhitePlayerFactors,
                Player.White,
                player,
                move);
            return new FactorsRecord(
                blackPlayerFactors: updatedBlackPlayerFactors,
                whitePlayerFactors: updatedWhitePlayerFactors,
                randomNoiseValue: GenerateRandomNoiseValue());
        }

        private FactorsByPlayer UpdateFactorsByPlayer(
            FactorsByPlayer currentFactors,
            Player player,
            Player playedWhoMadeMove,
            Move.Move move)
        {
            int materialValue = currentFactors.MaterialValue +
                GetMaterialValueDelta(player, playedWhoMadeMove, move);
            return new FactorsByPlayer(materialValue: materialValue);
        }

        private int GetMaterialValueDelta(Player player, Player playedWhoMadeMove, Move.Move move)
        {
            return (player == playedWhoMadeMove) ?
                materialEvaluator.CalculateEvaluationDeltaOnMyMove(move) :
                materialEvaluator.CalcaulateEvaluationDeltaOnTheirMove(move);
        }

        private int GenerateRandomNoiseValue()
        {
            return rnd.Next(17) - 8;
        }
    }
}
