using Optional;
using System.Threading.Tasks;

namespace ChessEngine
{
    public interface IDecisionMaker
    {
        Task<Option<Move.Move>> FindBestMove(Position.Position position);
    }
}
