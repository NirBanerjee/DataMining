import numpy as np
from numpy import *
import weka.core.converters as converters
from weka.core.converters import Loader
import weka.core.jvm as jvm

# jvm.start(class_path=['weka.jar'])
# loader = Loader(classname="weka.core.converters.ArffLoader")
# data = loader.load_file("trainProdIntro.binary.arff")

def getVector(matrix):
    Lambda, Q = np.linalg.eig(matrix)
    return Q


def tranfer(info):
    ls=[]
    ls.extend(list(mapOne.get(info[0])))
    ls.extend(list(mapTwo.get(info[1])))
    ls.append(float(info[2]))
    ls.append(float(info[3]))
    ls.extend(list(mapThree.get(info[4])))
    ls.extend(list(mapFour.get(info[5])))
    ls.append(float(info[6]))
    ls.append(float(info[7]))
    return ls

def Norm(matrix):
    minVals = matrix.min(0)
    maxVals = matrix.max(0)
    ranges = maxVals - minVals
    norm = zeros(shape(matrix))
    m = matrix.shape[0]
    norm = matrix - tile(minVals, (m, 1))
    norm = norm / tile(ranges, (m, 1))
    return norm

def classify(input, dataset, labels, k):
    num=dataset.shape[0]
    # calculate euclidean distance
    # tile(A, reps): Construct an array by repeating A reps times
    # the following copy numSamples rows for dataSet
    input = tranfer(input)
    diff = tile(input, (num, 1)) - dataset  # Subtract element-wise
    squaredDiff = diff ** 2  # squared for the subtract
    squaredDist = sum(squaredDiff, axis=1)  # sum is performed by row
    distance = squaredDist ** 0.5

    # sort the distance
    sortedDistIndices = argsort(distance)

    total=0
    for i in range(k):
        # choose the min k distance
        voteLabel = labels[sortedDistIndices[i]]
        # add the current value
        total += float(voteLabel)

    return total/k

if __name__ == '__main__':
    # initial symbol transfer
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

    # construct train data matrix and labels
    matrix = []
    label = []
    with open('train_real1') as finput:
        for a in finput:
            if a[-1] == '\n':
                a=a[:-1]
            a = a.split(",")
            if len(a) == 9:
                label.append(a[8])
                a = tranfer(a)
                matrix.append(a)
    dataset = np.asarray(matrix)
    norm = Norm(dataset)
    count = 0
    total= 0
    with open('test_real1') as ftest:
        for b in ftest:
            total+=1
            ls=b.split(",")
            actual=ls[-1]
            b = b[:-2].split(",")
            res = classify(b, dataset, label, 5)
            print(actual+" "+str(res))
            if (actual == res):
                count+=1
    print(count/total)