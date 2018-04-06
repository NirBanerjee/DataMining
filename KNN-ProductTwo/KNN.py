import numpy as np
from numpy import *
import weka.core.converters as converters
from weka.core.converters import Loader
import weka.core.jvm as jvm

# jvm.start(class_path=['weka.jar'])
# loader = Loader(classname="weka.core.converters.ArffLoader")
# data = loader.load_file("trainProdIntro.binary.arff")

def tranfer(info):
    lsone=[]
    lstwo=[]
    lsone.append(mapOne.get(info[0]))
    lsone.append(mapTwo.get(info[1]))
    lstwo.append(float(info[2]))
    lstwo.append(float(info[3]))
    lsone.append(mapThree.get(info[4]))
    lsone.append(mapFour.get(info[5]))
    lstwo.append(float(info[6]))
    lstwo.append(float(info[7]))
    return lsone, lstwo

def Norm(matrix):
    minVals = matrix.min(0)
    maxVals = matrix.max(0)
    ranges = maxVals - minVals
    norm = zeros(shape(matrix))
    m = matrix.shape[0]
    norm = matrix - tile(minVals, (m, 1))
    norm = norm / tile(ranges, (m, 1))
    return norm

def classify(input, matrixone, matrixtwo, labels, k, minVals, maxVals):
    inputone, inputtwo = tranfer(input)
    ranges = maxVals - minVals
    inputtwo = inputtwo - minVals
    inputtwo = inputtwo / ranges
    num = matrixtwo.shape[0]
    diff = tile(inputtwo, (num, 1)) - matrixtwo
    squared_diff = diff ** 2
    squared_dist = sum(squared_diff, axis=1)
    distance=[]
    row=matrixone.shape[0]
    for index in range(row):
        cur = 1 - initialOne[matrixone[index][0]][inputone[0]] + 1 - initialTwo[matrixone[index][1]][inputone[1]] + 1 - initialThree[matrixone[index][2]][inputone[2]] + 1 - initialFour[matrixone[index][3]][inputone[3]]
        cur += squared_dist[index]
        cur = cur ** 0.5
        distance.append(cur)
    # sort the distance
    sortedDistIndices = argsort(distance)

    classCount = {}  # define a dictionary (can be append element)
    for i in range(k):
        # choose the min k distance
        voteLabel = labels[sortedDistIndices[i]]
        ## count the times labels occur
        classCount[voteLabel] = classCount.get(voteLabel, 0) + 1

    # the max voted class will return
    maxCount = 0
    for key, value in classCount.items():
        if value > maxCount:
            maxCount = value
            maxIndex = key

    return maxIndex

if __name__ == '__main__':
    # initial symbol transfer
    initialOne = array(
        [[1, 0, 0.1, 0.3, 0.2], [0, 1, 0, 0, 0], [0.1, 0, 1, 0.2, 0.2], [0.3, 0, 0.2, 1, 0.1], [0.2, 0, 0.2, 0.1, 1]])
    wordOne = ['Loan', 'Bank_Account', 'CD', 'Mortgage', 'Fund']
    mapOne = {}
    for index in range(len(wordOne)):
        mapOne[wordOne[index]] = index

    initialTwo = array(
        [[1, 0.2, 0.1, 0.2, 0], [0.2, 1, 0.2, 0.1, 0], [0.1, 0.2, 1, 0.1, 0], [0.2, 0.1, 0.1, 1, 0], [0, 0, 0, 0, 1]])
    wordTwo = ['Business', 'Professional', 'Student', 'Doctor', 'Other']
    mapTwo = {}
    for index in range(len(wordTwo)):
        mapTwo[wordTwo[index]] = index

    initialThree = array([[1, 0.1, 0], [0.1, 1, 0.1], [0, 0.1, 1]])
    wordThree = ['Small', 'Medium', 'Large']
    mapThree = {}
    for index in range(len(wordThree)):
        mapThree[wordThree[index]] = index

    initialFour = array([[1, 0.8, 0, 0], [0.8, 1, 0.1, 0.5], [0, 0.1, 1, 0.4], [0, 0.5, 0.4, 1]])
    wordFour = ['Full', 'Web&Email', 'Web', 'None']
    mapFour = {}
    for index in range(len(wordFour)):
        mapFour[wordFour[index]] = index

    # construct train data matrix and labels
    matrixone = []
    matrixtwo = []
    label = []
    with open('train5') as finput:
        for a in finput:
            if a[-1] == '\n':
                a=a[:-1]
            a = a.split(",")
            if len(a) == 9:
                label.append(a[8])
                one, two = tranfer(a)
                matrixone.append(one)
                matrixtwo.append(two)
    matrixone = np.asarray(matrixone)
    matrixtwo = np.asarray(matrixtwo)
    minVals = matrixtwo.min(0)
    maxVals = matrixtwo.max(0)
    matrixtwo = Norm(matrixtwo)
    count = 0
    total= 0
    with open('test5') as ftest:
        for b in ftest:
            total+=1
            actual=b[-2]
            b = b[:-2].split(",")
            res = classify(b, matrixone, matrixtwo, label, 6, minVals, maxVals)
            if (actual == res):
                count+=1
    print(count/total)