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

    bestmove, is_bestmove_capture, is_bestmove_check, is_check = None, None, None, None
    if ('pv' in info) and len(info['pv']) > 0:
        bestmove = info['pv'][0]
        is_bestmove_capture = board.is_capture(bestmove)
        is_bestmove_check = board.is_into_check(bestmove)
        is_check = board.is_check()

    print('Score: {0}, bestmove {1}'.format(score, bestmove))
    return (score, bestmove, is_bestmove_capture, is_bestmove_check, is_check)

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

        (score, bestmove, is_bestmove_capture, is_bestmove_check, is_check) = evaluate_position(engine, fen)
        positions_db.update_position_evaluation(
            position_id,
            score,
            bestmove,
            is_bestmove_capture,
            is_bestmove_check,
            is_check)

    engine.quit()

if __name__ == '__main__':
    main()