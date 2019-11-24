import chess


class FactorMaterialDifference(object):

    _QUEEN_VALUE = 9.0
    _ROOK_VALUE = 5.0
    _BISHOP_VALUE = 3.0
    _KNIGHT_VALUE = 3.0
    _PAWN_VALUE = 1.0

    def calculate(self, board: chess.Board):
        white_material = self._calculate_material(chess.WHITE, board)
        black_material = self._calculate_material(chess.BLACK, board)
        return white_material - black_material

    def _calculate_material(self, color: chess.Color, board: chess.Board):
        return (
            len(board.pieces(chess.QUEEN, color)) * self._QUEEN_VALUE +
            len(board.pieces(chess.ROOK, color)) * self._ROOK_VALUE +
            len(board.pieces(chess.BISHOP, color)) * self._BISHOP_VALUE +
            len(board.pieces(chess.KNIGHT, color)) * self._KNIGHT_VALUE +
            len(board.pieces(chess.PAWN, color)) * self._PAWN_VALUE)

