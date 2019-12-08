import chess
import chess.engine
import positions_db


def engine_path():
    return 'D:\\stockfish-10-win\\Windows\\stockfish_10_x64_popcnt.exe'

def evaluation_limit():
    return chess.engine.Limit(time=10)

def evaluate_position(engine, fen: str):
    print('Evaluating position {0}'.format(fen))
    board = chess.Board(fen)
    info = engine.analyse(board, limit=evaluation_limit())
    
    score = info['score'].white().score()
    if score is not None:
        score /= 100.0
    print('Score: {0}'.format(score))
    return score

def main():
    counter = positions_db.count_not_evaluated_positions()
    print('Number of not yet evaluated positions: {0}'.format(counter))

    if not counter:
        print('Nothing to do, quiting.')
        return

    engine = chess.engine.SimpleEngine.popen_uci(engine_path())
    engine.configure({'Threads': 6})

    while True:
        record = positions_db.get_not_evaluated_position()
        if record is None:
            break
        position_id, fen = record
        score = evaluate_position(engine, fen)
        positions_db.update_position_evaluation(position_id, score)

    engine.quit()

if __name__ == '__main__':
    main()