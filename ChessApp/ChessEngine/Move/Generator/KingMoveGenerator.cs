namespace ChessEngine.Move.Generator
{
    internal sealed class KingMoveGenerator : BaseDeltasMoveGenerator
    {
        private static readonly MoveDelta[] KingDeltas = new MoveDelta[] {
            new MoveDelta(-1, -1),
            new MoveDelta(0, -1),
            new MoveDelta(1, -1),
            new MoveDelta(-1, 0),
            new MoveDelta(1, 0),
            new MoveDelta(-1, 1),
            new MoveDelta(0, 1),
            new MoveDelta(1, 1)
        };

        protected override MoveDelta[] GetMoveDeltas() => KingDeltas;

        protected override Piece GetPiece() => Piece.King;
    }
}
