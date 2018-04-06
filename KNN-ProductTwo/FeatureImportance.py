import numpy as np
from numpy import *
import matplotlib.pyplot as plt
import weka.core.converters as converters
from weka.core.converters import Loader
import weka.core.jvm as jvm

# from sklearn.datasets import make_classification
from sklearn.ensemble import ExtraTreesClassifier

def getVector(matrix):
    Lambda, Q = np.linalg.eig(matrix)
    return Q

initialOne = array(
    [[1, 0, 0.1, 0.3, 0.2], [0, 1, 0, 0, 0], [0.1, 0, 1, 0.2, 0.2], [0.3, 0, 0.2, 1, 0.1], [0.2, 0, 0.2, 0.1, 1]])
wordOne = ['Loan', 'Bank_Account', 'CD', 'Mortgage', 'Fund']
matrixOne = getVector(initialOne)
mapOne = {}
for index in range(len(wordOne)):
    mapOne[wordOne[index]] = matrixOne[index]

initialTwo = array(
    [[1, 0.2, 0.1, 0.2, 0], [0.2, 1, 0.2, 0.1, 0], [0.1, 0.2, 1, 0.1, 0], [0.2, 0.1, 0.1, 1, 0], [0, 0, 0, 0, 1]])
wordTwo = ['Business', 'Professional', 'Student', 'Doctor', 'Other']
matrixTwo = getVector(initialTwo)
mapTwo = {}
for index in range(len(wordTwo)):
    mapTwo[wordTwo[index]] = matrixTwo[index]

initialThree = array([[1, 0.1, 0], [0.1, 1, 0.1], [0, 0.1, 1]])
wordThree = ['Small', 'Medium', 'Large']
matrixThree = getVector(initialThree)
mapThree = {}
for index in range(len(wordThree)):
    mapThree[wordThree[index]] = matrixThree[index]

initialFour = array([[1, 0.8, 0, 0], [0.8, 1, 0.1, 0.5], [0, 0.1, 1, 0.4], [0, 0.5, 0.4, 1]])
wordFour = ['Full', 'Web&Email', 'Web', 'None']
matrixFour = getVector(initialFour)
mapFour = {}
for index in range(len(wordFour)):
    mapFour[wordFour[index]] = matrixFour[index]
# Build a classification task using 3 informative features
# X, y = make_classification(n_samples=160,
#                            n_features=8,
#                            n_informative=3,
#                            n_redundant=0,
#                            n_repeated=0,
#                            n_classes=2,
#                            random_state=0,
#                            shuffle=False)
jvm.start(class_path=['weka.jar'])
loader = Loader(classname="weka.core.converters.ArffLoader")
data = loader.load_file("trainProdIntro.binary.arff")
X=[]
y=[]
for info in data:
    info=str(info).split(",")
    ls = []
    ls.extend(list(mapOne.get(info[0])))
    ls.extend(list(mapTwo.get(info[1])))
    ls.append(float(info[2]))
    ls.append(float(info[3]))
    ls.extend(list(mapThree.get(info[4])))
    ls.extend(list(mapFour.get(info[5])))
    ls.append(float(info[6]))
    ls.append(float(info[7]))
    X.append(ls)
    y.append(info[8])

X=array(X)
y=array(y)


# Build a forest and compute the feature importances
forest = ExtraTreesClassifier(n_estimators=250,
                              random_state=0)

forest.fit(X, y)
importances = forest.feature_importances_
std = np.std([tree.feature_importances_ for tree in forest.estimators_],
             axis=0)
indices = np.argsort(importances)[::-1]

# Print the feature ranking
print("Feature ranking:")

for f in range(X.shape[1]):
    print("%d. feature %d (%f)" % (f + 1, indices[f], importances[indices[f]]))

print(forest.predict(X))
# Plot the feature importances of the forest
plt.figure()
plt.title("Feature importances")
plt.bar(range(X.shape[1]), importances[indices],
       color="r", yerr=std[indices], align="center")
plt.xticks(range(X.shape[1]), indices)
plt.xlim([-1, X.shape[1]])
plt.show()