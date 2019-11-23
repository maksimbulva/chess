import chess
import chess.pgn
import positions_db
import sys


def erase_move_counters(fen: str):
    splitted = fen.split()
    splitted[-1] = '0'
    splitted[-2] = '0'
    return ' '.join(splitted)

def import_game(game):
    fen_list = []
    board = game.board()
    for move in game.mainline_moves():
        board.push(move)
        fen_list.append(board.fen())
    return fen_list

def import_games(filename: str):
    print('Importing chess positions from ' + filename)
    fen_list = []
    with open(filename) as pgn_file:
        while True:
            game = chess.pgn.read_game(pgn_file)
            if game is not None:
                fen_list += import_game(game)
            else:
                break
    print("{0} positions imported".format(len(fen_list)))
    return fen_list

def main():
    if (len(sys.argv)) < 2:
        print('Please specify the pgn file to import games from')
        return

    pgn_file = str(sys.argv[1])
    fen_list = import_games(pgn_file)
    adjusted_fen_list = [erase_move_counters(fen) for fen in fen_list]

    positions_db.create_table_if_not_exists()
    positions_db.insert_positions(adjusted_fen_list)

if __name__ == '__main__':
    main()
