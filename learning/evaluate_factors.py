import chess
import csv
from factors.factor_advanced_pawn_count import FactorAdvancedPawnCount
from factors.factor_doubled_pawn_count import FactorDoubledPawnCount
from factors.factor_isolated_pawn_count import FactorIsolatedPawnCount
from factors.factor_material_bishop_pair import FactorMaterialBishopPair
from factors.factor_material import FactorMaterial
from factors.factor_mobility import FactorMobility
from factors.factor_passed_pawn_count import FactorPassedPawnCount
from factors.factor_pawn_island_count import FactorPawnIslandCount
from factors.factor_piece_square_table_value import FactorPieceSquareTableValue
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
        FactorMaterialBishopPair(),
        FactorPieceSquareTableValue(),
        FactorMobility(),
        FactorPawnIslandCount(),
        FactorDoubledPawnCount(),
        FactorIsolatedPawnCount(),
        FactorPassedPawnCount(),
        FactorAdvancedPawnCount()
    ]

    with open('position_evaluation.csv', 'w', newline='') as csvfile:
        writer = csv.writer(csvfile, delimiter=',')
        for evaluated_position in positions_db.get_evaluated_positions():
            fen, evaluation = evaluated_position
            row_data = [sigmoid(evaluation)]
            row_data += calculate_evaluation_factors(factors, chess.Board(fen))
            writer.writerow(row_data)

if __name__ == '__main__':
    main()
