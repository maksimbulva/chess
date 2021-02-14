namespace ChessEngine.AI.Evaluation.Factor
{
    internal sealed class FactorsByPlayer
    {
        public int MaterialValue { get; }

        public FactorsByPlayer(
            int materialValue)
        {
            MaterialValue = materialValue;
        }
    }
}
