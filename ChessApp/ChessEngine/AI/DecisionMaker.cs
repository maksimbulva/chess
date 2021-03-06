﻿using ChessEngine.AI.Evaluation;
using ChessEngine.AI.Search;
using ChessEngine.Move.Generator;
using Optional;
using System.Threading.Tasks;

namespace ChessEngine.AI
{
    internal sealed class DecisionMaker : IDecisionMaker
    {
        public Task<Option<Move.Move>> FindBestMove(Position.Position position)
        {
            var legalMoves = MoveGenerator.GenerateLegalMoves(position);
            if (legalMoves.Count == 0)
            {
                return Task.FromResult(Option.None<Move.Move>());
            }
            else if (legalMoves.Count == 1)
            {
                return Task.FromResult(Option.Some(legalMoves[0]));
            }

            var searchManager = new SearchManager(new Evaluator(), 2);
            return Task.Run(() => searchManager.FindBestMove(position));
        }
    }
}
