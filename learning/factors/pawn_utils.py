import chess


def bit_set_count(square_set: chess.SquareSet):
    result = 0
    while square_set:
        square_set.pop()
        result += 1
    return result

def my_pawns_BB(color: chess.Color, board: chess.Board):
    return board.pawns & board.occupied_co[color]

def my_pawn_on_row_BB(color: chess.Color, board: chess.Board, row: int):
    assert(row >= 0 and row <= 7)
    return my_pawns_BB(color, board) & chess.BB_RANKS[row]
    
def my_pawn_on_column_BB(color: chess.Color, board: chess.Board, column: int):
    assert(column >= 0 and column <= 7)
    return my_pawns_BB(color, board) & chess.BB_FILES[column]

def has_pawn_on_row(color: chess.Color, board: chess.Board, row: int):
    return my_pawn_on_row_BB(color, board, row) != 0

def has_pawn_on_column(color: chess.Color, board: chess.Board, column: int):
    return my_pawn_on_column_BB(color, board, column) != 0

def count_pawns_on_row(color: chess.Color, board: chess.Board, row: int):
    square_set = chess.SquareSet(my_pawn_on_row_BB(color, board, row))
    return bit_set_count(square_set)

def count_pawns_on_column(color: chess.Color, board: chess.Board, column: int):
    square_set = chess.SquareSet(my_pawn_on_column_BB(color, board, column))
    return bit_set_count(square_set)