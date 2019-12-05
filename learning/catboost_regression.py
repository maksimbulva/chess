import csv
from catboost import CatBoostRegressor
from sklearn.model_selection import train_test_split


def read_data():
    with open('position_evaluation.csv', 'r', newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        return [row for row in reader]

def main():
    # Initialize data
    data = read_data()
    print('Read {0} rows'.format(len(data)))

    labels = [row[0] for row in data]
    data = [row[1:] for row in data]

    train_data, test_data, train_labels, test_labels = train_test_split(data, labels, test_size=0.3)

    # Initialize CatBoostRegressor
    model = CatBoostRegressor(
        iterations=40,
        learning_rate=1,
        depth=8,
        logging_level="Verbose")

    # Fit model
    model.fit(train_data, train_labels)

    score = model.score(test_data, test_labels)
    print("Score: {0}".format(score))

    model.save_model("my_model.cbm", "cpp")

    assert(False)



    train_data = [[1, 4, 5, 6],
                [4, 5, 6, 7],
                [30, 40, 50, 60]]

    eval_data = [[2, 4, 6, 8],
                [1, 4, 50, 60]]

    train_labels = [10, 20, 30]
    # Get predictions
    preds = model.predict(eval_data)

if __name__ == '__main__':
    main()