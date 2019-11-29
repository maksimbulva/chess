import chess
from factors.factor_advanced_pawn_count import FactorAdvancedPawnCount
from factors.factor_doubled_pawn_count import FactorDoubledPawnCount
from factors.factor_isolated_pawn_count import FactorIsolatedPawnCount
from factors.factor_material import FactorMaterial
from factors.factor_passed_pawn_count import FactorPassedPawnCount
from factors.factor_pawn_island_count import FactorPawnIslandCount
from math import exp
import positions_db


def calculate_evaluation_factors(factors, board: chess.Board):
    result = []
    for f in factors:
        result.append(f.calculate(chess.WHITE, board))
        result.append(f.calculate(chess.BLACK, board))
    return result

def sigmoid(x):
    return 1.0 / (1.0 + exp(-x))

def main():
    position_count = positions_db.get_evaluated_positions_count()
    print('Generating evaluation factors for {0} positions'.format(position_count))

    factors = [
        FactorMaterial(),
        FactorPawnIslandCount(),
        FactorDoubledPawnCount(),
        FactorIsolatedPawnCount(),
        FactorPassedPawnCount(),
        FactorAdvancedPawnCount()
    ]

    with open('values.txt', 'w') as output_file:
        for evaluated_position in positions_db.get_evaluated_positions():
            fen, evaluation = evaluated_position
            evaluated_factors = calculate_evaluation_factors(factors, chess.Board(fen))
            evaluated_factors_str = '\t'.join([str(x) for x in evaluated_factors])
            output_file.write('{0}\t{1}\n'.format(sigmoid(evaluation), evaluated_factors_str))

if __name__ == '__main__':
    main()
