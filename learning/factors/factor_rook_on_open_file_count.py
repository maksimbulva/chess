import chess
from factors.pawn_utils import has_pawn_on_column


class FactorRookOnOpenFileCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        my_rooks = board.pieces(chess.ROOK, color)
        result = 0
        for my_rook in my_rooks:
            column = chess.square_file(my_rook)
            if (not has_pawn_on_column(color, board, column)
                and not has_pawn_on_column(not color, board, column)):
                result += 1
        return result
