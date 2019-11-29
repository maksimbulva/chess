import chess


class FactorMaterial(object):

    _QUEEN_VALUE = 9.0
    _ROOK_VALUE = 5.0
    _BISHOP_VALUE = 3.0
    _KNIGHT_VALUE = 3.0
    _PAWN_VALUE = 1.0

    def calculate(self, color: chess.Color, board: chess.Board):
        return (
            len(board.pieces(chess.QUEEN, color)) * self._QUEEN_VALUE +
            len(board.pieces(chess.ROOK, color)) * self._ROOK_VALUE +
            len(board.pieces(chess.BISHOP, color)) * self._BISHOP_VALUE +
            len(board.pieces(chess.KNIGHT, color)) * self._KNIGHT_VALUE +
            len(board.pieces(chess.PAWN, color)) * self._PAWN_VALUE)
