import chess
from factors.pawn_utils import count_pawns_on_column


class FactorDoubledPawnCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        result = 0
        for column in range(0, 8):
            pawns_on_column = count_pawns_on_column(color, board, column)
            result += max(pawns_on_column - 1, 0)
        return result
