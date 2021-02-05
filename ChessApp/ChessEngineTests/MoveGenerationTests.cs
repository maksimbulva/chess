using ChessEngine;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Linq;

namespace ChessEngineTests
{
    [TestClass]
    public class MoveGenerationTests
    {
        private readonly IChessEngine engine = ChessEngineFactory.CreateChessEngine();

        [DataRow(1, 20L)]
        [DataTestMethod]
        public void MoveGenerationFromInitialPositionTest(int depthPly, long actualMoveCount)
        {
            engine.ResetGame();
            Assert.AreEqual(actualMoveCount, engine.GetLegalMoves().Count());
        }
    }
}
