using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Move
{
    public readonly struct Move
    {
        private readonly int packedValue;

        // Use MoveBuilder class to generate packedValue
        internal Move(int packedValue)
        {
            this.packedValue = packedValue;
        }
    }
}
