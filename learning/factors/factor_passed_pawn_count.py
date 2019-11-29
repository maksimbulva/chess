import chess


class FactorPassedPawnCount(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        my_pawns = board.pieces(chess.PAWN, color)
        result = 0
        for my_pawn in my_pawns:
            if self._is_passed_pawn(color, board, my_pawn):
                result += 1
        return result

    def _is_passed_pawn(self, color: chess.Color, board: chess.Board, pawn_square: int):
        column = chess.square_file(pawn_square)
        columns = range(max(0, column - 1), min(7, column + 1) + 1)
        columns_BB = 0
        for column_index in columns:
            columns_BB = columns_BB | chess.BB_FILES[column_index]

        row = chess.square_rank(pawn_square)
        if color == chess.WHITE:
            rows = range(row, 7)
        else:
            rows = range(0, row)
        rows_BB = 0
        for row_index in rows:
            rows_BB = rows_BB | chess.BB_RANKS[row_index]

        enemy_pawns = board.pieces(chess.PAWN, not color)
        enemy_has_pawns_in_area = (enemy_pawns & columns_BB & rows_BB) != 0
        return not enemy_has_pawns_in_area
