import csv
import numpy as np
import math

TYPE_MATRIX = np.linalg.eig(np.diag((1,2,3,4,5)))[1]
LIFE_STYLE_MATRIX = np.linalg.eig(np.diag((1,2,3,4)))[1]


def read_dataset(path):
    '''
    Read dataset from files and return dataset list and associated label list.
    :param path: file path to the dataset
    :return: [data,label] list of dataset, list of label
    '''
    with open(path) as data_file:
        reader = list(csv.reader(data_file))
        data_cat = {}
        data_cat_count = 0
        data = []
        label = []
        for row in reader:
            if len(row) == 0:
                continue
            if str(row[0]).startswith('@attribute'):
                # Attribute list
                if not str(row[0]).endswith(' real'):
                    subcat = {}
                    count = 1
                    for item in row:
                        insert_item = item
                        if count == 1:
                            first_item = str(item)
                            insert_item = first_item[first_item.rfind('{') + 1:]
                        if count == len(row):
                            last_item = str(item)
                            insert_item = last_item[:-1]
                        subcat[insert_item] = count
                        count = count + 1

                        data_cat[data_cat_count] = subcat
                    data_cat_count = data_cat_count + 1

            elif not str(row[0]).startswith('@'):
                row[0] = data_cat[0][row[0]]
                row[1] = data_cat[1][row[1]]
                if str(row[-1]).startswith('C'):
                    data.append(row[:-1])
                    label.append(row[-1])
                else:
                    data.append(row)

        data = np.asarray(data).astype(float)
        original_data = data
        # data = (data - min(data)) / (max(data) - min(data))
        # Normalization
        data = data/np.linalg.norm(data,ord=np.inf,axis=0,keepdims=True)
        data[:,0] = original_data[:,0]
        data[:,1] = original_data[:,1]
        return [data,label]


def calcEuclidean(test_row, train_row,weight_vector):
    '''
    Calculate the vector distance using Euclidean distance method.
    :param test_row: vector from testing dataset
    :param train_row: vector from training dataset
    :param weight_vector: weighted vector
    :return: similarity score
    '''
    type_sim = 1 - TYPE_MATRIX[int(test_row[0]) - 1][int(train_row[0]) - 1]
    life_style_sim = 1 - LIFE_STYLE_MATRIX[int(test_row[1]) - 1][int(train_row[1]) - 1]
    item_vector = np.asarray([type_sim,
                              life_style_sim,
                              pow((test_row[2] - train_row[2]), 2),
                             pow((test_row[3] - train_row[3]), 2),
                             pow((test_row[4] - train_row[4]), 2),
                             pow((test_row[5] - train_row[5]), 2)])
    similarity = math.sqrt(np.dot(item_vector,weight_vector))
    return math.sqrt(np.dot(item_vector,weight_vector))


def calcManhattan(test_row, train_row,weight_vector):
    '''
        Calculate the vector distance using Mahattan distance method.
        :param test_row: vector from testing dataset
        :param train_row: vector from training dataset
        :param weight_vector: weighted vector
        :return: similarity score
        '''
    type_sim = 1 - TYPE_MATRIX[int(test_row[0]) - 1][int(train_row[0]) - 1]
    life_style_sim = 1 - LIFE_STYLE_MATRIX[int(test_row[1]) - 1][int(train_row[1]) - 1]
    item_vector = np.asarray([type_sim,life_style_sim,
                              abs(test_row[2] - train_row[2]),
                              abs(test_row[3] - train_row[3]),
                              abs(test_row[4] - train_row[4]),
                              abs(test_row[4] - train_row[4]),
                              ])
    similarity = np.dot(item_vector,weight_vector)
    similarity = similarity.astype(float)
    return similarity.astype(float)


def calcChebyshev(test_row, train_row,weight_vector):
    '''
            Calculate the vector distance using Chebyshev distance method.
            :param test_row: vector from testing dataset
            :param train_row: vector from training dataset
            :param weight_vector: weighted vector
            :return: similarity score
            '''
    type_sim = 1 - TYPE_MATRIX[int(test_row[0]) - 1][int(train_row[0]) - 1]
    life_style_sim = 1 - LIFE_STYLE_MATRIX[int(test_row[1]) - 1][int(train_row[1]) - 1]
    similarity = np.asarray([type_sim * weight_vector[0],
                             life_style_sim * weight_vector[1],
                             abs(test_row[2] - train_row[2]) * weight_vector[2],
                             abs(test_row[3] - train_row[3]) * weight_vector[3],
                             abs(test_row[4] - train_row[4]) * weight_vector[4],
                             abs(test_row[4] - train_row[4]) * weight_vector[5],
                             ]).max()
    return similarity


def calcStdEuclidean(test_row,train_row,weight_vector,std_list):
    '''
            Calculate the vector distance using Standard Euclidean distance method.
            :param test_row: vector from testing dataset
            :param train_row: vector from training dataset
            :param weight_vector: weighted vector
            :return: similarity score
            '''
    type_sim = 1 - TYPE_MATRIX[int(test_row[0]) - 1][int(train_row[0]) - 1]
    life_style_sim = 1 - LIFE_STYLE_MATRIX[int(test_row[1]) - 1][int(train_row[1]) - 1]
    item_vector = np.asarray([type_sim, life_style_sim,
                              pow((test_row[2] - train_row[2])/std_list[2],2),
                              pow((test_row[3] - train_row[3]) / std_list[3], 2),
                              pow((test_row[4] - train_row[4]) / std_list[4], 2),
                              pow((test_row[5] - train_row[5]) / std_list[5], 2),
                              ])
    similarity = math.sqrt(np.dot(item_vector, weight_vector))
    return similarity


def calcCosine(test_row,train_row,weight_vector):
    '''
            Calculate the vector distance using Cosine similarity method.
            :param test_row: vector from testing dataset
            :param train_row: vector from training dataset
            :param weight_vector: weighted vector
            :return: similarity score
            '''
    np.multiply(train_row,np.sqrt(weight_vector))
    np.multiply(test_row,np.sqrt(weight_vector))
    similarity = np.dot(test_row, train_row) / (math.sqrt(np.dot(test_row, test_row)) * math.sqrt(np.dot(train_row, train_row)))
    return similarity


def knn_classifier(train_data,train_label,test_data,k,wv=None,vote_method=1,distance_method=1):
    '''
    K-Nearest-Neighbour classifier method.
    :param train_data: trainning dataset
    :param train_label: label list from trainning dataset
    :param test_data: testing dataset
    :param k: number of the nearest neighbours
    :param wv: weighted vector
    :param vote_method: options for counting methods
    :param distance_method: options for distance calculation methods
    :return: list of predict label
    '''
    # print("train shape: {}".format(train_data.shape))
    # print("train lable shape: {}".format(train_label.shape))
    # print("test shape: {}".format(test_data.shape))

    std_list = np.std(train_data,axis=0)

    if wv is None:
        weight_vector = np.vstack(np.ones(len(train_data[0])))
    else:
        weight_vector = wv
    pred_label = []
    # calculate the similarity value
    for test_row in test_data:
        votes = {}
        similarity_list = []
        for train_row in train_data:
            if distance_method == 1:
                similarity = 1 / calcEuclidean(test_row, train_row,weight_vector)
            elif distance_method == 2:
                similarity = 1 / float(calcManhattan(test_row, train_row, weight_vector))
            elif distance_method == 3:
                similarity = 1 / calcChebyshev(test_row,train_row,weight_vector)
            elif distance_method == 4:
                similarity = 1 / calcStdEuclidean(test_row, train_row, weight_vector,std_list)
            elif distance_method == 5:
                similarity = calcCosine(test_row,train_row,weight_vector)
            similarity_list.append(similarity)

        similarity_index_list = np.argsort(np.asarray(similarity_list))[::-1]

        # count the number of existence
        if vote_method == 1:
            for i in range(0, k):
                label = train_label[similarity_index_list[i]]
                if label not in votes:
                    votes[label] = 1
                else:
                    votes[label] = votes[label] + 1
        # sum up the similarity scores of each label
        elif vote_method == 2:
            similarity_score_list = np.sort(np.asarray(similarity_list))[::-1]
            for i in range(0, k):
                label = train_label[similarity_index_list[i]]
                if label not in votes:
                    votes[label] = similarity_score_list[i]
                else:
                    votes[label] = votes[label] + similarity_score_list[i]

        votes = sorted(votes.items(), key=lambda kv: kv[1], reverse=True)
        pred = votes[0][0]
        pred_label.append(pred)

    return pred_label


def calc_accuracy(real_label,pred_label):
    '''
    Calculate the accuracy.
    :param real_label: the real label
    :param pred_label: the predicted label
    :return: accuracy score
    '''
    correct = 0
    for i in range(0,len(pred_label)):
        if pred_label[i] == real_label[i]:
            correct = correct + 1

    return float(correct / len(real_label))


def cross_validation(data, folds, k, wv):
    '''
    Cross validation method.
    :param data: dataset
    :param folds: number of folds for cross validation
    :param k:  number of nearest neighbour
    :param wv: weighted vector
    :return: average accuracy score
    '''
    avg_accuracy = 0
    for i in range(0,folds):
        np.random.shuffle(data)
        train, test = data[:int(len(data)*0.8),:], data[int(len(data)*0.8):,:]
        real_label = test[:,-1]
        pred_label = knn_classifier(np.asarray(train[:,:-1]).astype(float), train[:,-1],np.asarray(test[:,:-1]).astype(float), k,wv)
        acc = calc_accuracy(real_label,pred_label)
        avg_accuracy = avg_accuracy + acc

    return avg_accuracy / folds


def weight_optimization(data,folds,num_of_attr, k):
    '''
    Weight optimization method. To find the best weight for each attribute.
    :param data: dataset
    :param folds: number of folds for cross validation
    :param num_of_attr: number of attributes
    :param k: number of nearest neighbours
    :return: weighted vector, accuracy
    '''
    weight_vector = np.ones(num_of_attr)
    accuracy = cross_validation(data,folds,k,weight_vector)
    print("accuracy: {}".format(accuracy))

    for i in range(0,num_of_attr):
        weight_vector[i] = weight_vector[i]*2
        # cross validation
        attr_accuracy = cross_validation(data,folds, k, weight_vector)
        while True:
            weight_vector[i] = weight_vector[i] * 2
            attr_accuracy = cross_validation(data, folds, k, weight_vector)
            if attr_accuracy > accuracy:
                accuracy = attr_accuracy
            else:
                weight_vector[i] = weight_vector[i] / 2
                break

        while True:
            weight_vector[i] = weight_vector[i] / 2
            attr_accuracy = cross_validation(data, folds, k, weight_vector)
            if attr_accuracy > accuracy:
                accuracy = attr_accuracy
            else:
                weight_vector[i] = weight_vector[i] * 2
                break

    return weight_vector,accuracy


def knn():

    k = 3
    cross_val_folds = 5
    num_of_attr = 6
    # read training data file
    train_data_list = read_dataset('data/trainProdSelection.arff')
    train_data = np.asarray(train_data_list[0])
    train_label = np.asarray(train_data_list[1])
    train_data_label = np.hstack((np.asarray(train_data),np.vstack(train_label)))
    # print(train_data_label)

    # read testing data file -- using Frobenius norm, try and change back to min-max
    test_data_list = read_dataset('data/testProdSelection.arff')
    test_data = np.asarray(test_data_list[0])

    # print(knn_classifier(train_data,train_label,test_data,k))
    max_accuracy = 0
    max_label = []
    max_weight_vector = []
    min_accuracy = 1
    min_label = []
    min_weight_vector = []
    current_accuracy = 0

    epoch = 0
    while current_accuracy < 0.901:
        epoch = epoch + 1
        if epoch > 100:
            break
        print("epoch: {}".format(epoch))
        weight_vector,accuracy = weight_optimization(train_data_label,cross_val_folds,num_of_attr,k)
        print("weight_vector: {}".format(weight_vector))
        print("highest accuracy: {}".format(accuracy))

        pred_label = knn_classifier(train_data, train_label, test_data, k, weight_vector)
        print("predict label: {}".format(pred_label))
        if accuracy > max_accuracy:
            max_accuracy = accuracy
            max_label = pred_label
            max_weight_vector = weight_vector
        if accuracy < min_accuracy:
            min_accuracy = accuracy
            min_label = pred_label
            min_weight_vector = weight_vector
        current_accuracy = accuracy


    print("================ Result ================")
    print("highest accuracy: {}".format(max_accuracy))
    print("highest labels: {}".format(max_label))
    print("highest weight vector: {}".format(max_weight_vector))
    print("lowest accuracy: {}".format(min_accuracy))
    print("lowest labels: {}".format(min_label))
    print("lowest weight vector: {}".format(min_weight_vector))
    print("accuracy difference: {}".format(max_accuracy - min_accuracy))
    print("final epoch: {}".format(epoch))
    print("=======================================")

    # weight_vector,accuracy = weight_optimization(train_data_label,cross_val_folds,num_of_attr,k)
    # print("weight_vector: {}".format(weight_vector))
    # print("highest accuracy: {}".format(accuracy))

    # pred_label = knn_classifier(train_data, train_label, test_data, k, weight_vector)
    # print("predict label: {}".format(pred_label))


if __name__ != '__main__':
    pass

knn()