import chess
from factors.pawn_utils import has_pawn_on_column


class FactorIsolatedPawnCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        result = 0
        for column in range(0, 8):
            # Check whether there is a pawn on the current column and there
            # are no pawns on adjacent columns
            if not has_pawn_on_column(color, board, column):
                continue
            if column > 0 and has_pawn_on_column(color, board, column - 1):
                continue
            if column < 7 and has_pawn_on_column(color, board, column + 1):
                continue
            result += 1
        return result
