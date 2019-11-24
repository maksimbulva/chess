import sqlite3


def database_file():
    DATABASE_FILE = '../data/database/positions.db'
    return DATABASE_FILE

def open_connection():
    return sqlite3.connect(database_file())

def evaluated_positions_filter():
    return "Positions.'evaluated' = 1 AND Positions.'evaluation' IS NOT NULL"

def get_evaluated_positions_count():
    connection = open_connection()
    command = "SELECT COUNT(*) FROM Positions WHERE {0};".format(evaluated_positions_filter())
    cursor = connection.cursor()
    cursor.execute(command)
    result = cursor.fetchone()[0]
    connection.close()
    return result

def get_evaluated_positions():
    connection = open_connection()
    command = "SELECT Positions.'position', Positions.'evaluation' FROM Positions"
    command += " WHERE {0};".format(evaluated_positions_filter())
    cursor = connection.cursor()
    cursor.execute(command)
    while True:
        result = cursor.fetchone()
        if result is None:
            break
        yield result
    connection.close()
