import chess
from factors.pawn_utils import has_pawn_on_column


class FactorPawnIslandCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        result = 0
        for column in range(0, 7):
            if (has_pawn_on_column(color, board, column)
                and not has_pawn_on_column(color, board, column + 1)):
                result += 1
        if (has_pawn_on_column(color, board, 7) and not has_pawn_on_column(color, board, 6)):
            result += 1
        if (result == 0 and len(board.pieces(chess.PAWN, color)) > 0):
            result = 1
        return result
