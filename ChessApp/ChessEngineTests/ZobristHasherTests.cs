using ChessEngine;
using ChessEngine.Hash;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using static ChessEngine.Board.BoardNotation;

namespace ChessEngineTests
{
    [TestClass]
    public class ZobristHasherTests
    {
        private readonly ZobristHasher zobristHasher = new ZobristHasher();

        // Hash values are deterministic, thus this test is stable
        [TestMethod]
        public void HashUpdatesOnMoveTest()
        {
            var engine = ChessEngineFactory.CreateChessEngine();
            var oldHashValue = zobristHasher.CalculateHash(engine.CurrentPosition);

            engine.TryPlayMove(SquareFromString("e2"), SquareFromString("e4"));
            var newHashValue = zobristHasher.CalculateHash(engine.CurrentPosition);

            Assert.AreNotEqual(oldHashValue, newHashValue);
        }

        [TestMethod]
        public void HashesAreSameForTranspositionsTest()
        {
            var engine = ChessEngineFactory.CreateChessEngine();

            engine.TryPlayMove(SquareFromString("e2"), SquareFromString("e4"));
            engine.TryPlayMove(SquareFromString("e7"), SquareFromString("e5"));
            engine.TryPlayMove(SquareFromString("g1"), SquareFromString("f3"));
            var hashValue1 = zobristHasher.CalculateHash(engine.CurrentPosition);

            engine.TryPlayMove(SquareFromString("g1"), SquareFromString("f3"));
            engine.TryPlayMove(SquareFromString("e7"), SquareFromString("e5"));
            engine.TryPlayMove(SquareFromString("e2"), SquareFromString("e4"));
            var hashValue2 = zobristHasher.CalculateHash(engine.CurrentPosition);

            Assert.AreEqual(hashValue1, hashValue2);
        }

        [TestMethod]
        public void HashAccountsForPlayerToMoveTest()
        {
            var engine = ChessEngineFactory.CreateChessEngine();

            engine.ResetGame("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
            var hashValueWithWhiteToMove = zobristHasher.CalculateHash(engine.CurrentPosition);

            engine.ResetGame("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 b - -");
            var hashValueWithBlackToMove = zobristHasher.CalculateHash(engine.CurrentPosition);

            Assert.AreNotEqual(hashValueWithWhiteToMove, hashValueWithBlackToMove);
        }
    }
}
