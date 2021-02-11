using ChessEngine.AI;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine
{
    public static class DecisionMakerFactory
    {
        public static IDecisionMaker CreateDecisionMaker() => new DecisionMaker();
    }
}
