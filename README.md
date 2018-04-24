# Decision Tree Classifier
## Overview
This solution is an implmentation of the concept of Decision Trees for classification of data. It is implemented in java. The solution accepts all inputs as command line arguments and all the predicted labels / metrics are written to a file. 
## Configurations
The program is implemented using Java 1.8 and no external libraries were used. At this point of time, the solution only accepts .arff files as input. It will be generalized later to accept input from other file formats.
## Executing the solution
To run the decision tree classifier, extract the code bundle. The main project directory is called Decision Trees. Inside the Decision Trees directory, the code files are present inside the src folder. To execute the solution navigate to the src folder in your terminal. The file DecisionTreeClassifier.java is the file that needs to be executed. First step would be to compile the file. Use the command below to compile the file.
```
javac DecisionTreeClassifier.java
```
Once the DecisionTreeClassifier.java file is compiled, the next step would be to execute the code. While executing the code, we pass the training data file, testing data file and the files where we print the output as command line arguments. 
```
java DecisionTreeClassifier <path to training data file> <path to test data file> <file to print predictions on training data> <file to print predictions of the test data> <file to print metrics> <file to print the decision tree>
```
For instance, if the training file is inputTrain.arff, test data file is inputTest.arff, the file to print predictions on the training data is printTrainLabels.txt, the file to print predictions on the test data is printTestLabels.txt, the file to print the metrics is metrics.txt and the file to print the decision tree is printTree.txt, then the command to be executed is as below.
```
java DecisionTreeClassifier inputTrain.arff  inputTest.arff printTrainLabels.txt printTestLabels.txt metrics.txt printTree.txt
```
Please note the path to the input training and test data files can either be absolute paths to the files or the path has to be relative to the directory where the DecisionTreeClassifier.java is present.
## Limitations
1. At this point of time, the solution only supports .arff files.
2. At this point of time, the solution does not validate if the structure of the training and testing file are same. It assumes that the training and test file structure (i.e. the attributes) are same.
3. The solution at this point of time does not handle missing values.