# Product Selection
## Configurations
The program is implemented with Python 3.6 and no other external library is required.
## Run the KNN script
To run the KNN script, simply pass hyperparameters like the number of k, the number of folds for cross validation, the path to the training dataset, and the path to the testing dataset.
For example(assume the data files are in the data directory):
```
python3 knn.py 3 5 data/train.arff data/test.arff
```