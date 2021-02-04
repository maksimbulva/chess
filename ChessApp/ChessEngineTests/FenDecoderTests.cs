using ChessEngine;
using ChessEngine.Board;
using ChessEngine.Fen;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using static ChessEngine.Board.BoardNotation;

namespace ChessEngineTests
{
    [TestClass]
    public class FenDecoderTests
    {
        private const string InitialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        private const string EndgamePosition = "6k1/5p2/6p1/8/7p/8/6PP/6K1 b - - 0 0";

        [TestMethod]
        public void DecodePiecesTest()
        {
            var board = FenDecoder.Decode(InitialPosition).Board;
            for (int row = 2; row <= 5; ++row)
            {
                for (int column = 0; column < Board.ColumnCount; ++column)
                {
                    Assert.IsTrue(board.IsEmpty(row, column));
                }
            }

            AssertPiece(Player.White, Piece.Rook, board.GetPieceAt(SquareFromString("a1")));
            AssertPiece(Player.White, Piece.Bishop, board.GetPieceAt(SquareFromString("f1")));
            AssertPiece(Player.White, Piece.Pawn, board.GetPieceAt(SquareFromString("e2")));
            AssertPiece(Player.Black, Piece.King, board.GetPieceAt(SquareFromString("e8")));
            AssertPiece(Player.Black, Piece.Queen, board.GetPieceAt(SquareFromString("d8")));
            AssertPiece(Player.Black, Piece.Knight, board.GetPieceAt(SquareFromString("g8")));
        }

        [DataRow(Player.White, InitialPosition)]
        [DataRow(Player.Black, EndgamePosition)]
        [DataTestMethod]
        public void DecodePlayerToMoveTest(Player expectedPlayer, string encodedPosition)
        {
            Assert.AreEqual(expectedPlayer, FenDecoder.Decode(encodedPosition).PlayerToMove);
        }

        [TestMethod]
        public void DecodeCastlingAvailabilityTest()
        {
            var currentPosition = FenDecoder.Decode(InitialPosition);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.White).CanCastleShort);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.White).CanCastleLong);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.Black).CanCastleShort);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.Black).CanCastleLong);

            currentPosition = FenDecoder.Decode(InitialPosition.Replace("KQkq", "Qk"));
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.White).CanCastleShort);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.White).CanCastleLong);
            Assert.IsTrue(currentPosition.GetCastlingAvailability(Player.Black).CanCastleShort);
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.Black).CanCastleLong);

            currentPosition = FenDecoder.Decode(EndgamePosition);
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.White).CanCastleShort);
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.White).CanCastleLong);
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.Black).CanCastleShort);
            Assert.IsFalse(currentPosition.GetCastlingAvailability(Player.Black).CanCastleLong);
        }

        [DataRow(InitialPosition, false)]
        [DataRow(EndgamePosition, false)]
        [DataRow("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", true)]
        [DataTestMethod]
        public void DecodeEnPassantCaptureAvailabilityTest(string encodedPosition, bool expectedValue)
        {
            Assert.AreEqual(expectedValue, FenDecoder.Decode(encodedPosition).IsCanCaptureEnPassant);
        }

        [TestMethod]
        public void DecodeMoveCountersTest()
        {
            var decodedPosition = FenDecoder.Decode("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
            Assert.AreEqual(1, decodedPosition.HalfMoveClock);
            Assert.AreEqual(2, decodedPosition.FullMoveNumber);
        }

        private void AssertPiece(Player expectedPlayer, Piece expectedPiece, PieceOnBoard actualPiece)
        {
            Assert.AreEqual(expectedPlayer, actualPiece.player);
            Assert.AreEqual(expectedPiece, actualPiece.piece);
        }
    }
}
