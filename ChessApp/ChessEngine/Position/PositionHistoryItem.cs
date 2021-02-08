using Optional;

namespace ChessEngine.Position
{
    internal sealed class PositionHistoryItem
    {
        public Move.Move Move { get; }
        public Option<int> EnPassantCaptureColumn { get; }

        public PositionHistoryItem(
            Move.Move move,
            Option<int> enPassantCaptureColumn)
        {
            Move = move;
            EnPassantCaptureColumn = enPassantCaptureColumn;
        }
    }
}
