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
            'evaluation' SINGLE NULL
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
            WHERE Positions.'evaluation' IS NULL
            LIMIT 1;
    """
    result = execute_command_and_get_result(command)
    if result is None:
        return None
    else:
        return result[0], result[1]

def count_not_evaluated_positions():
    command = "SELECT COUNT() FROM Positions WHERE Positions.'evaluation' IS NULL;"
    return execute_command_and_get_result(command)[0]

def update_position_evaluation(position_id: int, score: float):
    command = "UPDATE Positions SET evaluation={0}".format(score)
    command += " WHERE Positions.'id'={0}".format(position_id)
    execute_command(command)
