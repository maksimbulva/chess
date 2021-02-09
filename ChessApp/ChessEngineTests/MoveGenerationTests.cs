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
        [DataRow(5, 4_865_609)]
        [DataTestMethod]
        public void MoveGenerationFromInitialPositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        [DataRow(1, 48)]
        [DataRow(2, 2039)]
        [DataRow(3, 97_862)]
        [DataRow(4, 4_085_603)]
        [DataTestMethod]
        public void MoveCountFromKiwipetePositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            engine.ResetGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        [DataRow(1, 14)]
        [DataRow(2, 191)]
        [DataRow(3, 2812)]
        [DataRow(4, 43_238)]
        [DataRow(5, 674_624)]
        [DataTestMethod]
        public void MoveCountInEndgamePositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            engine.ResetGame("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        [DataRow(1, 46)]
        [DataRow(2, 2079)]
        [DataRow(3, 89_890)]
        [DataRow(4, 3_894_594)]
        [DataTestMethod]
        public void MoveCountForStevenEdwardsPositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            engine.ResetGame("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
            Assert.AreEqual(actualMoveCount, CountMoves(engine, depthPly));
        }

        [DataRow(1, 44)]
        [DataRow(2, 1486)]
        [DataRow(3, 62_379)]
        [DataRow(4, 2_103_487)]
        [DataTestMethod]
        public void MoveCountForSharpOpeningPositionTest(int depthPly, long actualMoveCount)
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            engine.ResetGame("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
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
                    if (move.IsPromotion)
                    {
                        Assert.IsTrue(engine.TryPlayMove(move.OriginSquare, move.DestSquare, move.GetPromoteToPiece()));
                    }
                    else
                    {
                        Assert.IsTrue(engine.TryPlayMove(move.OriginSquare, move.DestSquare));
                    }
                    var result = CountMoves(engine, depthPly - 1);
                    Assert.IsTrue(engine.TryUndoMove());
                    return result;
                });
            }
        }
    }
}
