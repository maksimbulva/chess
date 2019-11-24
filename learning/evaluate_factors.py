import chess
from math import exp

from factors.factor_material_difference import FactorMaterialDifference
import positions_db


def calculate_evaluation_factors(board: chess.Board):
    factor_1 = FactorMaterialDifference()
    return [factor_1.calculate(board)]

def sigmoid(x):
    return 1.0 / (1.0 + exp(-x))

def main():
    position_count = positions_db.get_evaluated_positions_count()
    print('Generating evaluation factors for {0} positions'.format(position_count))

    with open('values.txt', 'w') as output_file:
        for evaluated_position in positions_db.get_evaluated_positions():
            fen, evaluation = evaluated_position
            factors = calculate_evaluation_factors(chess.Board(fen))
            factors_str = '\t'.join([str(x) for x in factors])
            output_file.write('{0}\t{1}\n'.format(sigmoid(evaluation), factors_str))

if __name__ == '__main__':
    main()
