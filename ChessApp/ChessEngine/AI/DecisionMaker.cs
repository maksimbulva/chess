using ChessEngine.Move.Generator;
using Optional;
using System;
using System.Threading.Tasks;

namespace ChessEngine.AI
{
    internal sealed class DecisionMaker : IDecisionMaker
    {
        private readonly Random rnd = new Random();

        public Task<Option<Move.Move>> FindBestMove(Position.Position position)
        {
            var legalMoves = MoveGenerator.GenerateLegalMoves(position);
            if (legalMoves.Count == 0)
            {
                return Task.Run(() => Option.None<Move.Move>());
            }
            else if (legalMoves.Count == 1)
            {
                return Task.Run(() =>Option.Some(legalMoves[0]));
            }

            var moveToPlay = legalMoves[rnd.Next(legalMoves.Count - 1)];
            return Task.Run(() => Option.Some(moveToPlay));
        }
    }
}
