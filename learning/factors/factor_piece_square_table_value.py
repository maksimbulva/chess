import chess


# Evaluation based on https://www.chessprogramming.org/Simplified_Evaluation_Function
class FactorPieceSquareTableValue(object):

    def __init__(self):
        self._pawn_table = [
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5],
            [0.1, 0.1, 0.2, 0.3, 0.3, 0.2, 0.1, 0.1],
            [0.05, 0.05, 0.1, 0.25, 0.25, 0.1, 0.05, 0.05],
            [0.0, 0.0, 0.0, 0.2, 0.2, 0.0, 0.0, 0.0],
            [0.05, -0.05, -0.1, 0.0, 0.0, -0.1, -0.05, 0.05],
            [0.5, 0.1, 0.1, -0.2, -0.2, 0.1, 0.1, 0.5],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        ]

        self._knight_table = [
            [-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5],
            [-0.4, -0.2, 0.0, 0.0, 0.0, 0.0, -0.2, -0.4],
            [-0.3, 0.0, 0.1, 0.15, 0.15, 0.1, 0.0, -0.3],
            [-0.3, 0.05, 0.15, 0.2, 0.2, 0.15, 0.05, -0.3],
            [-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3],
            [-0.3, 0.05, 0.1, 0.15, 0.15, 0.10, 0.05, -0.3],
            [-0.4, -0.2, 0.0, 0.05, 0.05, 0.0, -0.2, -0.4],
            [-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5]
        ]

        self._bishop_table = [
            [-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2],
            [-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1],
            [-0.1, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.1],
            [-0.1, 0.05, 0.05, 0.1, 0.1, 0.05, 0.05, -0.1],
            [-0.1, 0.0, 0.1, 0.1, 0.1, 0.1, 0.0, -0.1],
            [-0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, -0.1],
            [-0.1, 0.05, 0.0, 0.0, 0.0, 0.0, 0.05, -0.1],
            [-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2]
        ]

        self._rook_table = [
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05],
            [-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05],
            [-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05],
            [-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05],
            [-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05],
            [-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05],
            [0.0, 0.0, 0.0, 0.05, 0.05, 0.0, 0.0, 0.0]
        ]

        self._queen_table = [
            [-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2],
            [-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1],
            [-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1],
            [-0.05, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05],
            [0.0, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05],
            [-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1],
            [-0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1],
            [-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2],
        ]

        self._king_table = [
            [-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3],
            [-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3],
            [-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3],
            [-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3],
            [-0.2, -0.3, -0.3, -0.4, -0.4, -0.3, -0.3, -0.2],
            [-0.1, -0.2, -0.2, -0.2, -0.2, -0.2, -0.2, -0.1],
            [0.2, 0.2, 0.0, 0.0, 0.0, 0.0, 0.2, 0.2],
            [0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2]
        ]

        self._king_endgame_table = [
            [-0.5, -0.4, -0.3, -0.2, -0.2, -0.3, -0.4, -0.5],
            [-0.3, -0.2, -0.1, 0.0, 0.0, -0.1, -0.2, -0.3],
            [-0.3, -0.1, 0.2, 0.3, 0.3, 0.2, -0.1, -0.3],
            [-0.3, -0.1, 0.3, 0.4, 0.4, 0.3, -0.1, -0.3],
            [-0.3, -0.1, 0.3, 0.4, 0.4, 0.3, -0.1, -0.3],
            [-0.3, -0.1, 0.2, 0.3, 0.3, 0.2, -0.1, -0.3],
            [-0.3, -0.3, 0.0, 0.0, 0.0, 0.0, -0.3, -0.3],
            [-0.5, -0.3, -0.3, -0.3, -0.3, -0.3, -0.3, -0.5]
        ]

    def calculate(self, color: chess.Color, board: chess.Board):
        result = 0.0

        my_pawns = board.pieces(chess.PAWN, color)
        for my_pawn in my_pawns:
            row = self._adjusted_row(color, my_pawn)
            result += self._pawn_value(row, chess.square_file(my_pawn))

        my_knights = board.pieces(chess.KNIGHT, color)
        for my_knight in my_knights:
            row = self._adjusted_row(color, my_knight)
            result += self._knight_value(row, chess.square_file(my_knight))

        my_bishops = board.pieces(chess.BISHOP, color)
        for my_bishop in my_bishops:
            row = self._adjusted_row(color, my_bishop)
            result += self._bishop_value(row, chess.square_file(my_bishop))

        my_rooks = board.pieces(chess.ROOK, color)
        for my_rook in my_rooks:
            row = self._adjusted_row(color, my_rook)
            result += self._rook_value(row, chess.square_file(my_rook))

        my_queens = board.pieces(chess.QUEEN, color)
        for my_queen in my_queens:
            row = self._adjusted_row(color, my_queen)
            result += self._queen_value(row, chess.square_file(my_queen))

        my_kings = board.pieces(chess.KING, color)
        for my_king in my_kings:
            row = self._adjusted_row(color, my_king)
            result += self._king_value(board, row, chess.square_file(my_king))

        return result

    def _adjusted_row(self, color: chess.Color, square: int):
        if color == chess.WHITE:
            return chess.square_rank(square)
        else:
            return 7 - chess.square_rank(square)

    def _pawn_value(self, adjusted_row: int, column: int):
        return self._pawn_table[adjusted_row][column]

    def _knight_value(self, adjusted_row: int, column: int):
        return self._knight_table[adjusted_row][column]

    def _bishop_value(self, adjusted_row: int, column: int):
        return self._bishop_table[adjusted_row][column]

    def _rook_value(self, adjusted_row: int, column: int):
        return self._rook_table[adjusted_row][column]

    def _queen_value(self, adjusted_row: int, column: int):
        return self._queen_table[adjusted_row][column]

    def _king_value(self, board: chess.Board, adjusted_row: int, column: int):
        if self._is_like_endgame(board):
            return self._king_endgame_table[adjusted_row][column]
        else:
            return self._king_table[adjusted_row][column]

    def _is_like_endgame(self, board: chess.Board):
        return self._count_material_without_pawns(board) <= 36

    def _count_material_without_pawns(self, board: chess.Board):
        material = 0
        for color in (chess.WHITE, chess.BLACK):
            material += 9 * len(board.pieces(chess.QUEEN, color))
            material += 5 * len(board.pieces(chess.ROOK, color))
            material += 3 * len(board.pieces(chess.BISHOP, color))
            material += 3 * len(board.pieces(chess.KNIGHT, color))
        return material
