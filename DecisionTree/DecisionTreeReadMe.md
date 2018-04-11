# Decision Tree Classifier
## Overview
This solution is an implmentation of the concept of Decision Trees for classification of data. It is implemented in java. The solution accepts all inputs as command line arguments and all the predicted labels / metrics are written to a file. 
## Configurations
The program is implemented using Java 1.8 and no external libraries were used. At this point of time, the solution only accepts .arff files as input. It will be generalized later to accept input from other file formats.
## Executing the solution
To run the KNN script, simply pass hyperparameters like the number of k, the number of folds for cross validation, the path to the training dataset, and the path to the testing dataset.
For example(assume the data files are in the data directory):
```
python3 knn.py 3 5 data/train.arff data/test.arff
```