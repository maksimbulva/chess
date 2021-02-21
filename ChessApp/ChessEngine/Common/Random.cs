namespace ChessEngine.Common
{
    // Produces the same sequence of random number regardless of .NET version
    // See https://en.wikipedia.org/wiki/Lehmer_random_number_generator#Parameters_in_common_use
    internal sealed class Random
    {
        private ulong state;

        public Random(uint seed)
        {
            state = seed;
        }

        public uint GetNextUInt()
        {
            ulong product = state * 48271;
            uint x = (uint)((product & 0x7fffffff) + (product >> 31));

            x = (x & 0x7fffffff) + (x >> 31);
            state = x;
            return x;
        }
    }
}
