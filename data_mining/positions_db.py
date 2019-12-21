import sqlite3


def database_file():
    DATABASE_FILE = '../data/database/positions.db'
    return DATABASE_FILE

def open_connection():
    return sqlite3.connect(database_file())

def execute_command(command: str):
    connection = open_connection()
    connection.cursor().execute(command)
    connection.commit()

def execute_command_and_get_result(command: str):
    connection = open_connection()
    cursor = connection.cursor()
    cursor.execute(command)
    return cursor.fetchone()

def create_table_if_not_exists():
    command = """
        CREATE TABLE IF NOT EXISTS Positions (
            'id' INTEGER PRIMARY KEY AUTOINCREMENT,
            'position' VARCHAR(80) NOT NULL UNIQUE,
            'evaluation' SINGLE NULL,
            'evaluated' INT DEFAULT 0,
            'bestmove' VARCHAR(8),
            'is_bestmove_capture' BOOLEAN,
            'is_bestmove_check' BOOLEAN,
            'is_check' BOOLEAN
        );
    """
    execute_command(command)

def insert_positions(fen_list: list):
    connection = open_connection()
    for fen in fen_list:
        command = "INSERT OR IGNORE INTO Positions('position') VALUES ('{0}');".format(fen)
        connection.cursor().execute(command)
    connection.commit()

def get_not_evaluated_position():
    command = """
        SELECT Positions.'id', Positions.'position' FROM Positions
            WHERE Positions.'evaluated' = 0
            LIMIT 1;
    """
    result = execute_command_and_get_result(command)
    if result is None:
        return None
    else:
        return result[0], result[1]

def count_not_evaluated_positions():
    command = "SELECT COUNT() FROM Positions WHERE Positions.'evaluated' = 0;"
    return execute_command_and_get_result(command)[0]

def sqlBool(value: bool):
    if (value):
        return 1
    return 0

def update_position_evaluation(
    position_id: int,
    score: float,
    bestmove,
    is_bestmove_capture: bool,
    is_bestmove_check: bool,
    is_check: bool):

    scoreSql = str(score)
    if score is None:
        scoreSql = 'NULL'
    command = "UPDATE Positions SET evaluation = {0}, evaluated = 1, bestmove = '{1}'".format(scoreSql, bestmove)
    command += ", is_bestmove_capture = {0}, is_bestmove_check = {1}".format(sqlBool(is_bestmove_capture), sqlBool(is_bestmove_check))
    command += ", is_check = {0}".format(sqlBool(is_check))
    command += " WHERE Positions.'id' = {0};".format(position_id)
    execute_command(command)
