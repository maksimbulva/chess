using ChessEngine;
using ChessEngine.Move;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Linq;

namespace ChessEngineTests
{
    [TestClass]
    public class MoveGenerationTests
    {
        [DataRow(1, 20L)]
        [DataRow(2, 400L)]
        [DataRow(3, 8902)]
        [DataRow(4, 197_281)]
        [DataTestMethod]
        public void MoveGenerationFromInitialPositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        private static long CountMoves(IChessEngine engine, int depthPly)
        {
            if (depthPly < 0)
            {
                return 0L;
            }
            else if (depthPly == 1)
            {
                return engine.GetLegalMoves().LongCount();
            }
            else
            {
                var legalMoves = new List<Move>(engine.GetLegalMoves());
                return legalMoves.Sum(move =>
                {
                    Assert.IsTrue(engine.TryPlayMove(move.OriginSquare, move.DestSquare));
                    var result = CountMoves(engine, depthPly - 1);
                    Assert.IsTrue(engine.TryUndoMove());
                    return result;
                });
            }
        }
    }
}
