using System.Collections.Generic;
using System.Linq;

namespace ChessEngine.Board
{
    internal sealed class PieceTable
    {
        private readonly LinkedListNode<PieceOnBoard>[] _table = new LinkedListNode<PieceOnBoard>[Board.SquareCount];

        private readonly LinkedList<PieceOnBoard>[] _playerPieces = new LinkedList<PieceOnBoard>[2];

        public PieceTable(List<PieceOnBoard> pieces)
        {
            foreach (var player in new Player[] { Player.Black, Player.White })
            {
                var piecesLinkedList = CreatePlayerPiecesLinkedList(pieces, player);
                _playerPieces[(int)player] = piecesLinkedList;
                FillTableWithPieces(piecesLinkedList);
            }
        }

        public bool IsEmpty(int squareIndex) => _table[squareIndex] == null;

        public IEnumerable<PieceOnBoard> GetPieces(Player player) => _playerPieces[(int)player];

        public BoardSquare GetKingSquare(Player player) => _playerPieces[(int)player].First.Value.square;

        public PieceOnBoard GetPieceAt(int squareIndex) => _table[squareIndex].Value;

        public void MovePiece(int originIndex, int destIndex)
        {
            var nodeToMove = _table[originIndex];
            var p = nodeToMove.Value;
            nodeToMove.Value = new PieceOnBoard(p.player, p.piece, new BoardSquare(destIndex));
            _table[destIndex] = nodeToMove;
            _table[originIndex] = null;
        }

        public void RemovePieceAt(int squareIndex)
        {
            var nodeToRemove = _table[squareIndex];
            nodeToRemove.List.Remove(nodeToRemove);
            _table[squareIndex] = null;
        }

        public void InsertPiece(PieceOnBoard pieceOnBoard)
        {
            var linkedList = _playerPieces[(int)pieceOnBoard.player];
            var squareIndex = pieceOnBoard.square.IntValue;
            if (pieceOnBoard.piece == Piece.King)
            {
                _table[squareIndex] = linkedList.AddFirst(pieceOnBoard);
            }
            else
            {
                _table[squareIndex] = linkedList.AddLast(pieceOnBoard);
            }
        }

        private void FillTableWithPieces(LinkedList<PieceOnBoard> pieces)
        {
            var currentNode = pieces.First;
            while (currentNode != null)
            {
                _table[currentNode.Value.square.IntValue] = currentNode;
                currentNode = currentNode.Next;
            }
        }

        private static LinkedList<PieceOnBoard> CreatePlayerPiecesLinkedList(
            List<PieceOnBoard> pieces,
            Player player)
        {
            var kingData = pieces
                .Find(x => x.piece == Piece.King && x.player == player);

            var piecesLinkedList = new LinkedList<PieceOnBoard>(
                pieces.Where(x => x.piece != Piece.King && x.player == player));
            piecesLinkedList.AddFirst(kingData);
            return piecesLinkedList;
        }
    }
}
