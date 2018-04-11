# Product Introduction
## Notice
* The test file of real number is lack of labels but attribute information at the top contains label, which will result in load error when loading. **Please remove the label info in the top attribute information part.**

## Binary Label
* Use IDE to open "product\_intro.py" and run. OR use command line "python3 product\_intro.py"
* Choose to see cross valiadation results or predict labels
	1. If choose to see cross validation results, please input a file path for training data and then # of folds(an integer)
	2. If choose to predict labels, please input a file path for training data and a file path for testing data
* Choose 1 for binary label
* See print out result
	1. For cross valiadation, accuracy of each round will be shown one by one
	2. For label prediction, label of each line will be shown one by one

## Real number
* Use IDE to open "product\_intro.py" and run. OR use command line "python3 product\_intro.py"
* Choose to see cross valiadation results or predict real numbers
	1. If choose to see cross validation results, please input a file path for training data and then # of folds(an integer)
	2. If choose to predict labels, please input a file path for training data and a file path for testing data
* Choose 2 for real number
* See print out result
	1. For cross valiadation, MSE of each round will be shown one by one
	2. For label prediction, real value of each line will be shown one by one

## Appendix
1. Weight factor of each attributes is hard-code in file. Just tune them manually.
2. Similarity matrix is hard-code in file.
