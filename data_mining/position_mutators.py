import chess
import random


def erase_random_piece(board: chess.Board):
    removable_piece_squares = []
    for square, piece in board.piece_map().items():
        if piece.piece_type != chess.KING:
            removable_piece_squares.append(square)

    for _ in range(0, 10):
        square = random.choice(removable_piece_squares)
        new_board = board.copy()
        new_board.remove_piece_at(square)
        if new_board.is_valid():
            return new_board
    
    return None
