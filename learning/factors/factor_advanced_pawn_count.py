import chess
from factors.pawn_utils import count_pawns_on_row


class FactorAdvancedPawnCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        if color == chess.WHITE:
            advanced_row = 6
        else:
            advanced_row = 1
        return count_pawns_on_row(color, board, advanced_row)
