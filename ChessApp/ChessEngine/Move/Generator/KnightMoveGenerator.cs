namespace ChessEngine.Move.Generator
{
    internal sealed class KnightMoveGenerator : BaseDeltasMoveGenerator
    {
        private static readonly MoveDelta[] KnightDeltas = new MoveDelta[]
        {
            new MoveDelta(-1, -2),
            new MoveDelta(1, -2),
            new MoveDelta(-2, -1),
            new MoveDelta(2, -1),
            new MoveDelta(-2, 1),
            new MoveDelta(2, 1),
            new MoveDelta(-1, 2),
            new MoveDelta(1, 2)
        };

        protected override MoveDelta[] GetMoveDeltas() => KnightDeltas;

        protected override Piece GetPiece() => Piece.Knight;
    }
}
