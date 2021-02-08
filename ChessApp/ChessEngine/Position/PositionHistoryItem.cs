using Optional;

namespace ChessEngine.Position
{
    internal sealed class PositionHistoryItem
    {
        public Move.Move Move { get; }
        public Option<int> EnPassantCaptureColumn { get; }
        public CastlingAvailability WhiteCastlingAvailability { get; }
        public CastlingAvailability BlackCastlingAvailability { get; }

        public PositionHistoryItem(
            Move.Move move,
            Option<int> enPassantCaptureColumn,
            CastlingAvailability whiteCastlingAvailability,
            CastlingAvailability blackCastlingAvailability)
        {
            Move = move;
            EnPassantCaptureColumn = enPassantCaptureColumn;
            WhiteCastlingAvailability = whiteCastlingAvailability;
            BlackCastlingAvailability = blackCastlingAvailability;
        }
    }
}
