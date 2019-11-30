import chess


class FactorMobility(object):

    def calculate(self, color: chess.Color, board: chess.Board):
        move_generator = chess.PseudoLegalMoveGenerator(board)
        return move_generator.count()
