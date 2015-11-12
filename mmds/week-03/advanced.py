# -*- coding: utf-8 -*-


from matplotlib import pyplot
import numpy
import scipy


'''
Question 1

For the following graph:

  C -- D -- E
/ |    |    | \
A  |    |    |  B
\ |    |    | /
  F -- G -- H

Write the adjacency matrix A, the degree matrix D, and the Laplacian matrix L.
For each, find the sum of all entries and the number of nonzero entries.
Then identify the true statement from the list below.

Question Explanation

The Adjacency matrix has 1 in row i and column j if there is an edge between the
nodes represented by these two rows. In this graph, all the nodes have degree 3
except A and B, whose degrees are 2. Thus, there are 6*3 + 2*2 = 22 nonzero
entries. Each of these entries is 1, so the sum of all entries, including
the 0's, is 22.

The degree matrix has the degree of node i in the i-th position along the
diagonal. All other entries are 0. Thus, there are 8 nonzero entries, and their
sum is 22, as we observed in the paragraph above.

The Laplacian matrix is D-A. Since A has only off-diagonal entries, and D has
only diagonal entries, the number of nonzero entries in L is the sum of those
numbers for A and D. That is, 22+8 = 30. The sum of all the entries of any
Laplacian matrix is 0, because both the positive entries (on the diagonal) and
the negative entries (off-diagonal) sum to the sum of the degrees of the nodes.
'''
def question_1():
    # Adjacency matrix.
    A = numpy.matrix([
        [0, 0, 1, 0, 0, 1, 0, 0],
        [0, 0, 0, 0, 1, 0, 0, 1],
        [1, 0, 0, 1, 0, 1, 0, 0],
        [0, 0, 1, 0, 1, 0, 1, 0],
        [0, 1, 0, 1, 0, 0, 0, 1],
        [1, 0, 1, 0, 0, 0, 1, 0],
        [0, 0, 0, 1, 0, 1, 0, 1],
        [0, 1, 0, 0, 1, 0, 1, 0]
    ])
    rn, cn = A.shape

    # Degree matrix.
    D = numpy.asmatrix(numpy.zeros((rn, cn), int))
    numpy.fill_diagonal(D, sum(A))

    # Laplacian matrix.
    L = D - A

    sum_a = A.sum()
    sum_d = D.sum()
    sum_l = L.sum()
    nonzero_a = numpy.count_nonzero(A)
    nonzero_d = numpy.count_nonzero(D)
    nonzero_l = numpy.count_nonzero(L)

    print('A: sum={} #nonzero={}'.format(sum_a, nonzero_a))
    print('D: sum={} #nonzero={}'.format(sum_d, nonzero_d))
    print('L: sum={} #nonzero={}'.format(sum_l, nonzero_l))


'''
Question 2

You are given the following graph.

   2 ----6
 /  \    |
1    4   |
 \  /  \ |
  3      5 

The goal is to find two clusters in this graph using Spectral Clustering on the
Laplacian matrix. Compute the Laplacian of this graph. Then compute the second
eigen vector of the Laplacian (the one corresponding to the second smallest
eigenvalue).

To cluster the points, we decide to split at the mean value. We say that a node
is a tie if its value in the eigen-vector is exactly equal to the mean value.
Let's assume that if a point is a tie, we choose its cluster at random. Identify
the true statement from the list below.
'''
def question_2():
    # Pre-processing: Construct a matrix representation of the graph
    
    # Adjacency matrix A
    A = numpy.matrix([
        [0, 1, 1, 0, 0, 0],
        [1, 0, 0, 1, 0, 1],
        [1, 0, 0, 1, 0, 0],
        [0, 1, 1, 0, 1, 0],
        [0, 0, 0, 1, 0, 1],
        [0, 1, 0, 0, 1, 0]
    ])
    rn, cn = A.shape

    # Degree matrix D
    D = numpy.asmatrix(numpy.zeros((rn, cn), int))
    numpy.fill_diagonal(D, sum(A))

    # Graph Laplacian matrix L
    L = D - A

    # Decomposition: Compute eigenvalues and eigenvectors of the Laplacian
    # matrix. Specifically we are interested in the second smallest eigenvalue
    # λ_2 and its corresponding eigenvector X_2

    # Compute the eigenvalues (vector λ) and eigenvecs (matrix X)
    eigvals, eigvecs = numpy.linalg.eigh(L)
    eigvals = eigvals.round(decimals=0)
    eigvecs = eigvecs.round(decimals=1)

    # Sort the eigenvalues ascending, and get the index of the second
    # smallest (λ_2)
    eigvals_2_idx = eigvals.argsort()[1]
    # Get the corresponding eigenvectors column (X_2)
    eigvecs_2 = eigvecs[:, eigvals_2_idx]
    # Check the mean value of X_2 to see where to split
    print(eigvecs_2)
    print(numpy.mean(eigvecs_2))

    #pyplot.plot(range(1, 7), eigvecs_2)
    #pyplot.show()


'''
Question 3

We wish to estimate the surprise number (2nd moment) of a data stream, using
the method of AMS. It happens that our stream consists of ten different values,
which we'll call 1, 2,..., 10, that cycle repeatedly. That is, at timestamps 1
through 10, the element of the stream equals the timestamp, at timestamps 11
through 20, the element is the timestamp minus 10, and so on. It is now
timestamp 75, and a 5 has just been read from the stream. As a start, you should
calculate the surprise number for this time.

For our estimate of the surprise number, we shall choose three timestamps at
random, and estimate the surprise number from each, using the AMS approach
(length of the stream times 2m-1, where m is the number of occurrences of the
element of the stream at that timestamp, considering all times from that
timestamp on, to the current time). Then, our estimate will be the median of the
three resulting values.

You should discover the simple rules that determine the estimate derived from
any given timestamp and from any set of three timestamps. Then, identify from
the list below the set of three "random" timestamps that give the closest
estimate.

Question Explanation

First, the surprise number is 5*64 + 5*49 = 565. The reason is that the elements
1 through 5 appear 8 times, so they contribute 5*82, and the elements 6 through
10 appear 7 times, contributing 5*72.

Notice that for this contrived example, the AMS estimate is a nondecreasing
function of the timestamp. Thus, of any three timestamps, the middle one will
give the median estimate, and we do not have to calculate all three.

At each of the timestamps between 36 and 45, inclusive, the element appearing
then appears exactly 4 times, from that time forward. Thus, each of these
timestamps generates an estimate of 75*(2*4 - 1) = 525, which is as close to 565
as we can get. Each of the correct answers has a middle timestamp in this range.

Similarly, for the timestamps between 26 and 35, the estimate is
75*(2*5 - 1) = 675 and for the timestamps between 46 and 55 the estimate is
75*(2*3 - 1) = 375. Neither of these groups offer as close an estimate, and the
timestamps earlier or later offer even worse estimates.

'''
from scipy.stats import itemfreq
from collections import defaultdict
def question_3():
    def generator(n):
        if n%10 == 0:
            return 10
        else:
            return n%10

    len_n = 75
    series = numpy.array([generator(n) for n in range(1,len_n+1)])
    surprize = numpy.sum(itemfreq(series)[:,1]**2)

    print('Second moment series is {}'.format(surprize))

    A1 = [30, 47, 62]
    A2 = [25, 34, 47]
    A3 = [14, 35, 42]
    A4 = [24, 44, 65]

    def AMS(series, points):
        X = defaultdict(int)
    
        X[series[points[0]-1]] += 1
        for i in range(points[0]+1, points[1]):
            if series[i-1] in X.keys():
                X[series[i-1]] += 1
            
        #if series[points[1]-1] not in X.keys():        
        X[series[points[1]-1]] += 1
        for i in range(points[1]+1, points[2]):
            if series[i-1] in X.keys():
                X[series[i-1]] += 1 
            
        #if series[points[2]-1] not in X.keys():  
        X[series[points[2]-1]] += 1
        for i in range(points[2]+1, len(series)):
            if series[i-1] in X.keys():
                X[series[i-1]] += 1 
    
        second_moment_random_var = []
        for key in X.keys():
            if key != 'default':
                second_moment_random_var.append(len(series)*(2*X[key]-1))
        #print(X)
        return numpy.mean(second_moment_random_var)

    a1 = AMS(series, A1)
    a2 = AMS(series, A2)
    a3 = AMS(series, A3)
    a4 = AMS(series, A4)
    print('Second moment for point {} is {}. Distance - {}'.format(A1, a1, numpy.abs(surprize-a1)))
    print('Second moment for point {} is {}. Distance - {}'.format(A2, a2, numpy.abs(surprize-a2)))
    print('Second moment for point {} is {}. Distance - {}'.format(A3, a3, numpy.abs(surprize-a3)))
    print('Second moment for point {} is {}. Distance - {}'.format(A4, a4, numpy.abs(surprize-a4)))

    def cal_supprise(current_t = 75):
        common = ( current_t / 10 ) % 10
        more_set = ( current_t ) % 10

        less_set = 10 - more_set
    
        return more_set * (common+1)**2 + less_set * (common**2)
    
    

    def AMS_2(time_list, current_t = 75):
        estimates = []
        for t in time_list:
            delta = current_t - t
            elem = t % 10
            threshold = current_t % 10
            common = (delta / 10) % 10
            if elem > threshold:
                estimates.append( ( 2*common - 1)*current_t )
            else:
                estimates.append( ( 2*common + 1)*current_t )

        return numpy.mean(estimates)

    print (AMS_2(A1)-surprize)
    print (AMS_2(A2)-surprize)
    print (AMS_2(A3)-surprize)
    print (AMS_2(A4)-surprize)


import pprint
import json
import os
import sys
def question_3_1():
    def surprise(seq):
        his = {}
        for n in seq:
            his[n] = his.get(n, 0) + 1
        pprint.pprint(his)
        return sum([v*v for k,v in his.items()])

    ams = [
        [30, 47, 62],
        [25, 34, 47],
        [14, 35, 42],
        [24, 44, 65],
    ]

    def median(v):
        return sorted(v)[len(v)>>1]

    def estimate(r, s):
        v = [sum([v == s[x-1] and p>=(x-1) for p, v in enumerate(s)]) for x in r]
        return [len(s) * (2*m - 1) for m in v]

    s = [(i % 10) + 1 for i in range(0, 75)]
    print(surprise(s))
    for a in ams:
        print('{} => {}'.format(json.dumps(a), median(estimate(a, s))))


def question_3_2():
    ams = [
        [30, 47, 62],
        [25, 34, 47],
        [14, 35, 42],
        [24, 44, 65],
    ]

    #Qns3
    #from timestamp 1 to 70 each appear 7 times except for number 1 2 3 4 5
    #Surprise
    S = 5*7*7 + 5*8*8
    print(S)

    #number of times it occur from 31 to 75
    def moment2(A,S):
        X1 = A[0]%10
        X1Value=0
        for i in range(A[0],76):
            if i%10 == X1:
                X1Value = X1Value + 1
        X2 = A[1]%10
        X2Value=0
        #number of times it occur from 31 to 75
        for i in range(A[1],76):
            if i%10 == X2:
                X2Value = X2Value + 1
        X3 = A[2]%10
        X3Value=0
        #number of times it occur from 31 to 75
        for i in range(A[2],76):
            if i%10 == X3:
                X3Value = X3Value + 1
        print('{} {} {}'.format(X1Value,X2Value,X3Value))
        return (75.0/3*(X1Value*2-1 + X2Value*2-1 + X3Value*2-1))/S

    #OptionABCD
    for a in ams:
        print(moment2(a,S))


'''
Question 4

We wish to use the Flagolet-Martin algorithm of Section 4.4 to count the number
of distinct elements in a stream. Suppose that there ten possible elements,
1, 2,..., 10, that could appear in the stream, but only four of them have
actually appeared. To make our estimate of the count of distinct elements, we
hash each element to a 4-bit binary number. The element x is hashed to
3x + 7 (modulo 11). For example, element 8 hashes to 3*8+7 = 31, which is
9 modulo 11 (i.e., the remainder of 31/11 is 9). Thus, the 4-bit string for
element 8 is 1001.

A set of four of the elements 1 through 10 could give an estimate that is exact
(if the estimate is 4), or too high, or too low. You should figure out under
what circumstances a set of four elements falls into each of those categories.
Then, identify in the list below the set of four elements that gives the exactly
correct estimate.

Question Explanation

Here is a table of the hash values and resulting bit strings for each of the ten
elements:

x    3x+7 (mod 11)    Bit String
1    10               1010
2    2                0010
3    5                0101
4    8                1000
5    0                0000
6    3                0011
7    6                0110
8    9                1001
9    1                0001
10   4                0100

In order to give the correct estimate (4), a set must have at most two 0's at
the end of the hash value of any of its members, but must have a member with
exactly two 0's at the end. Observe from the table above that 10 is the only
element whose hash value has exactly two bits at the end. However,
1, 2, 3, 6, 7, 8, and 9 have zero or one 0 at the end, so the correct answers
are any set of four elements that includes 10 and does not include 4 or 5.
'''
def question_4():
    A1 = (1, 4, 7, 9)
    A2 = (4, 6, 9, 10)
    A3 = (4, 5, 6, 10)
    A4 = (1, 6, 7, 10)

    A = (A1, A2, A3, A4)

    def hash_elem(n):
        b_hash_element = bin((3*n + 7) % 11)[2:]
        b_hash_element = '0' * (4 - len(b_hash_element)) + b_hash_element
        return b_hash_element
    
    def countZerosInBinary(bnum):
        return len(bnum) - len(bnum.rstrip('0'))
    
    def FlagoletMatrtin(hash_list):
        maxnum = 0
        for val in hash_list:
            num = countZerosInBinary(val)
            if num > maxnum:
                maxnum = num
        return 2**maxnum
    
    print(FlagoletMatrtin(([hash_elem(x) for x in [1,3,6,8]]))) 
    
    _hash = {}
    for i in range(1,11):
        _hash[i] = hash_elem(i)
    
    print(_hash)
    for Ai in A:
        print(Ai, '\t', FlagoletMatrtin(([hash_elem(x) for x in Ai])))


'''
Question 5

Suppose we are using the DGIM algorithm of Section 4.6.2 to estimate the number
of 1's in suffixes of a sliding window of length 40. The current timestamp is
100, and we have the following buckets stored:

End Time 100 98  95  92  87  80  65
Size     1   1   2   2   4   8   8

Note: we are showing timestamps as absolute values, rather than modulo the
window size, as DGIM would do.

Suppose that at times 101 through 105, 1's appear in the stream. Compute the set
of buckets that would exist in the system at time 105. Then identify one such
bucket from the list below. Buckets are represented by pairs (end-time, size).

Question Explanation

Time 101: When the first 1 comes in, we construct (101,1). That gives us
three 1's, so we combine (100,1) and (98,1) into (100,2). That gives us
three 2's, so we combine (95,2) and (92,2) into (95,4).

Time 102: Create (102,1).

Time 103: Create (103,1). Combine (102,1) and (101,1) into (102,2).

Time 104: Create (104,1).

Time 105: Drop (65,8), because it is now completely outside the window of
length 40. We have three 1's, so we combine (104,1) and (103,1) into (104,2). We
have three 2's, so we combine (102,2) and (100,2) into (102,4). We have
three 4's, so we combine (95,4) and (87,4) into (95,8). Note that there are only
two 8's, not three, since we already dropped the last 8.
'''
def question_5():
    pass


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()
    print('Question 3:')
    question_3()
    print()
    print('Question 3.1:')
    question_3_1()
    print()
    print('Question 3.2:')
    question_3_2()
    print()
    print('Question 4:')
    question_4()
    print()
    print('Question 5:')
    question_5()
    print()


if __name__ == '__main__':
    main()
