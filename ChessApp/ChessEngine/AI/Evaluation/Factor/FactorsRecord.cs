namespace ChessEngine.AI.Evaluation.Factor
{
    internal class FactorsRecord
    {
        public FactorsByPlayer BlackPlayerFactors { get; }
        public FactorsByPlayer WhitePlayerFactors { get; }
        public int RandomNoiseValue { get; }

        public FactorsRecord(
            FactorsByPlayer blackPlayerFactors,
            FactorsByPlayer whitePlayerFactors,
            int randomNoiseValue)
        {
            BlackPlayerFactors = blackPlayerFactors;
            WhitePlayerFactors = whitePlayerFactors;
            RandomNoiseValue = randomNoiseValue;
        }
    }
}
