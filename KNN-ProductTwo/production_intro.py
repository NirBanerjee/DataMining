import numpy as np
from numpy import *
from random import shuffle
import weka.core.converters as converters
from weka.core.converters import Loader
import weka.core.jvm as jvm

# add non-value to matrix one and value to matrix two
# please change index if input sequence is changed
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

# normalization value attributes
def norm(matrix):
    minVals = matrix.min(0)
    maxVals = matrix.max(0)
    ranges = maxVals - minVals
    norm = zeros(shape(matrix))
    m = matrix.shape[0]
    norm = matrix - tile(minVals, (m, 1))
    norm = norm / tile(ranges, (m, 1))
    return norm

def get_matrix(train_data):
    matrixone = []
    matrixtwo = []
    label = []
    for a in train_data:
        a = str(a)
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
    matrixtwo = norm(matrixtwo)
    return matrixone, matrixtwo, label, minVals, maxVals

# get test data labels
def classify_binary(input, matrixone, matrixtwo, labels, k, minVals, maxVals):
    inputone, inputtwo = tranfer(input)
    # normalization value in input data
    ranges = maxVals - minVals
    inputtwo = inputtwo - minVals
    inputtwo = inputtwo / ranges

    # calculate distance for value
    num = matrixtwo.shape[0]
    diff = tile(inputtwo, (num, 1)) - matrixtwo
    # tune weight factor
    diff[:,0] = diff[:,0] * 2
    diff[:,1] = diff[:,1] * 8
    diff[:,2] = diff[:,2] * 2
    diff[:,3] = diff[:,3] * 3
    squared_diff = diff ** 2
    squared_dist = sum(squared_diff, axis=1)
    similarity=[]
    row=matrixone.shape[0]
    # calculate distance for non-value and append similarity for each one
    for index in range(row):
        cur = 1 - initialOne[matrixone[index][0]][inputone[0]] + 1 - initialTwo[matrixone[index][1]][inputone[1]] + 1 - initialThree[matrixone[index][2]][inputone[2]] + 1 - initialFour[matrixone[index][3]][inputone[3]]
        # total distance for one row
        cur += squared_dist[index]
        cur = cur ** 0.5
        similarity.append(1/cur)
    # sort the similarity
    sorted_similarity_index = argsort(similarity)[::-1]

    count = {}
    for i in range(k):
        # choose the min k distance
        vote_label = labels[sorted_similarity_index[i]]
        # count the times labels occur
        count[vote_label] = count.get(vote_label, 0) + 1

    # the max voted class will return
    max_count = 0
    for key, value in count.items():
        if value > max_count:
            max_count = value
            max_index = key

    return max_index

def predict_result_binary(train_data, test_data):
    matrixone, matrixtwo, label, minVals, maxVals=get_matrix(train_data)
    total= 0
    for b in test_data:
        b = str(b)
        total+=1
        # assume there is a fake label at the end of each row, remove it
        # if not, please use b = b.split(",")
        b = b[:-2].split(",")
        # assume k is 5
        res = classify_binary(b, matrixone, matrixtwo, label, 5, minVals, maxVals)
        print(str(total)+" "+res)

def cross_valiadation_binary(train_data, fold):
    dataset=[a for a in train_data]
    shuffle(dataset)
    cv_train=[]
    cv_test=[]
    test_num=int(len(dataset)/fold)
    print("Starting Cross Validation, # of fold is "+str(fold))
    index=1
    sum=0
    for i in range(0, len(dataset), test_num):
        print("Cross Validation #" + str(index) + ":")
        print("Test data index: " + str(i) + "--" + str(i + test_num))
        cv_test.extend(dataset[i:i+test_num])
        cv_train.extend(dataset[0:i])
        cv_train.extend(dataset[i+test_num:])
        matrixone, matrixtwo, label, minVals, maxVals = get_matrix(cv_train)
        count = 0
        total= 0
        for b in cv_test:
            b = str(b)
            total+=1
            actual=b[-1]
            # assume there is a fake label at the end of each row, remove it
            # if not, please use b = b.split(",")
            b = b[:-2].split(",")
            res = classify_binary(b, matrixone, matrixtwo, label, 5, minVals, maxVals)
            if (actual == res):
                count+=1
        sum+=count/total
        print("Accuracy is: "+str(count/total))
        index+=1
        cv_test=[]
        cv_train=[]
    print("Average accuracy is: "+str(sum/fold))

def classify_real(input, matrixone, matrixtwo, labels, k, minVals, maxVals):
    inputone, inputtwo = tranfer(input)
    ranges = maxVals - minVals
    inputtwo = inputtwo - minVals
    inputtwo = inputtwo / ranges
    num = matrixtwo.shape[0]
    diff = tile(inputtwo, (num, 1)) - matrixtwo
    # tune weight factor
    diff[:,0] = diff[:,0] * 2
    diff[:,1] = diff[:,1] * 8
    diff[:,2] = diff[:,2] * 2
    diff[:,3] = diff[:,3] * 3
    squared_diff = diff ** 2
    squared_dist = sum(squared_diff, axis=1)
    similarity=[]
    row=matrixone.shape[0]
    for index in range(row):
        cur = 1 - initialOne[matrixone[index][0]][inputone[0]] + 1 - initialTwo[matrixone[index][1]][inputone[1]] + 1 - initialThree[matrixone[index][2]][inputone[2]] + 1 - initialFour[matrixone[index][3]][inputone[3]]
        cur += squared_dist[index]
        cur = cur ** 0.5
        similarity.append(1/cur)
    # sort the similarity
    sorted_similarity_index = argsort(similarity)[::-1]

    ksum=0
    for i in range(k):
        ksum += float(labels[sorted_similarity_index[i]])

    return ksum / k

def predict_result_real(train_data, test_data):
    matrixone, matrixtwo, label, minVals, maxVals=get_matrix(train_data)
    total= 0
    print("Predict Value:")
    for b in test_data:
        b = str(b)
        total+=1
        b = b.split(",")
        # assume k is 5
        res = classify_real(b, matrixone, matrixtwo, label, 5, minVals, maxVals)
        print(str(total)+" "+str(res))

def cross_valiadation_real(train_data, fold):
    dataset=[a for a in train_data]
    shuffle(dataset)
    cv_train=[]
    cv_test=[]
    test_num=int(len(dataset)/fold)
    print("Starting Cross Validation, # of fold is "+str(fold))
    index=1
    sum=0
    for i in range(0, len(dataset), test_num):
        real = []
        predict = []
        print("Cross Validation #"+str(index)+":")
        print("Test data index: "+str(i) +"--"+str(i+test_num))
        cv_test.extend(dataset[i:i+test_num])
        cv_train.extend(dataset[0:i])
        cv_train.extend(dataset[i+test_num:])
        matrixone, matrixtwo, label, minVals, maxVals = get_matrix(cv_train)
        count = 0
        total= 0
        for b in cv_test:
            total += 1
            ls = str(b).split(",")
            real.append(float(ls[-1]))
            b = ls[:-1]
            res = classify_real(b, matrixone, matrixtwo, label, 5, minVals, maxVals)
            # print(str(total)+" "+str(res))
            predict.append(res)
        real = array(real)
        predict = array(predict)
        sum+=((real - predict) ** 2).mean(axis=None)
        print("MSE is: "+str(((real - predict) ** 2).mean(axis=None)))
        index+=1
        cv_test=[]
        cv_train=[]
    print("Average MSE is: "+str(sum/fold))

if __name__ == '__main__':
    # initial symbol transfer by using similarity matrix
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

    # load data from arff file
    jvm.start(class_path=['weka.jar'])
    loader = Loader(classname="weka.core.converters.ArffLoader")
    choice=input("What do you want to do? Input 1 for cross valiadation; Input 2 for get prediction result:")
    if choice == '1':
        train_data_path=input("Please input train data(.arff) path:")
        train_data=loader.load_file(train_data_path)
        fold = input("Please input # of fold(an integer) for cross valiadation:")
        type=input("Which kind of label do you want to evaluate? 1 for binary 2 for real:")
        if type == '1':
            cross_valiadation_binary(train_data, int(fold))
        elif type == '2':
            cross_valiadation_real(train_data, int(fold))
    elif choice == '2':
        train_data_path = input("Please input train data(.arff) path:")
        train_data = loader.load_file(train_data_path)
        test_data_path=input("Please input test data(.arff) path:")
        test_data=loader.load_file(test_data_path)
        type = input("Which kind of label do you want to predict? 1 for binary 2 for real:")
        if type == '1':
            predict_result_binary(train_data, test_data)
        elif type == '2':
            predict_result_real(train_data, test_data)
    else:
        print("Please input 1 or 2!")
