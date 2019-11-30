import chess


class FactorMaterialBishopPair(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        my_bishops = board.pieces(chess.BISHOP, color)
        has_white_squares_bishop = False
        has_black_squares_bishop = False
        for my_bishop in my_bishops:
            if my_bishop % 2 == 1:
                has_black_squares_bishop = True
            else:
                has_white_squares_bishop = True
        if has_white_squares_bishop and has_black_squares_bishop:
            return 1
        else:
            return 0
