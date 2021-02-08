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
        [DataRow(1, 20)]
        [DataRow(2, 400)]
        [DataRow(3, 8902)]
        [DataRow(4, 197_281)]
        [DataRow(5, 4_865_609L)]
        [DataTestMethod]
        public void MoveGenerationFromInitialPositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        [DataRow(1, 48)]
        [DataTestMethod]
        public void MoveCountFromKiwipetePositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            engine.ResetGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
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
