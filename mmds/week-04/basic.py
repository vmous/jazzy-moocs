# -*- coding: utf-8 -*-

import math
import numpy


'''
Question 1

Here is a table of 1-5 star ratings for five movies (M, N, P. Q. R) by three
raters (A, B, C).

  M N P Q R
A 1 2 3 4 5
B 2 3 2 5 3
C 5 5 5 3 2

Normalize the ratings by subtracting the average for each row and then
subtracting the average for each column in the resulting table.
'''
def question_1():
    A = numpy.matrix([
        [1, 2, 3, 4, 5],
        [2, 3, 2, 5, 3],
        [5, 5, 5, 3, 2]
    ])

    A = A - numpy.average(A, axis=1)
    A = A - numpy.average(A, axis=0)
    print(A)


'''                                                                                                                                                                                                                             Question 2                                                                                                                                                                                                                       
Below is a table giving the profile of three items.

A 1 0 1 0 1 2
B 1 1 0 0 1 6
C 0 1 0 1 0 2

The first five attributes are Boolean, and the last is an integer "rating." Assume
that the scale factor for the rating is α. Compute, as a function of α, the cosine
distances between each pair of profiles. For each of α = 0, 0.5, 1, and 2, determine
the cosine of the angle between each pair of vectors. Which of the following is FALSE?
'''
def question_2():

    def compute_cosine(matrix, alpha):
        print('alpha = {}'.format(alpha))

        r = len(matrix)
        c = len(matrix[0])

        for i in range(r):
            matrix[i][c - 1] *= alpha

        for k in range(r):
            for l in range(k + 1, r):
                print('cos(Θ_matrix[{},{}])= {}'.format(k, l, numpy.dot(matrix[k], matrix[l]) / (numpy.linalg.norm(matrix[k]) * numpy.linalg.norm(matrix[l]))))

    for a in [0, 0.5, 1, 2]:
        m = [[1, 0, 1, 0, 1, 2],
             [1, 1, 0, 0, 1, 6],
             [0, 1, 0, 1, 0, 2]]
        
        compute_cosine(m, a)
        print()

'''
Question 3

Note: In this question, all columns will be written in their transposed form, as rows,
to make the typography simpler. Matrix M has three rows and two columns, and the columns
form an orthonormal basis. One of the columns is [2/7,3/7,6/7]. There are many options
for the second column [x,y,z]. Write down those constraints on x, y, and z. Then, identify
in the list below the one column that could be [x,y,z]. All components are computed
to three decimal places, so the constraints may be satisfied only to a close approximation.
'''
def question_3():
    v = [2/7, 3/7, 6/7]

    candidates = [[0.485, -0.485, 0.728],
                  [0.975, 0.700, -0.675],
                  [-0.288, -0.490, 0.772],
                  [0.702, -0.702, 0.117]]

    for cand in candidates:
        norm = numpy.linalg.norm(cand)
        if abs(norm - 1) > 0.01:
            continue

        dot_prod = numpy.dot(v, cand)
        if abs(dot_prod) <= 0.01:
            print('And the answer is: {}'.format(cand))

'''
Question 4

Note: In this question, all columns will be written in their transposed form, as rows, to
make the typography simpler. Matrix M has three rows and three columns, and the columns
form an orthonormal basis. One of the columns is [2/7, 3/7, 6/7], and another is
[6/7, 2/7, -3/7]. Let the third column be [x,y,z]. Since the length of the vector
[x,y,z] must be 1, there is a constraint that x^2+y^2+z^2 = 1. However, there are other
constraints, and these other constraints can be used to deduce facts about the ratios
among x, y, and z. Compute these ratios, and then identify one of them in the list below.
'''
def question_4():
    print('Solve the system of equations:')
    print('[2/7, 3/7, 6/7] * [x, y, z]')
    print('[6/7, 2/7, -3/7] * [x, y, z]')


'''
Question 5

Suppose we have three points in a two dimensional space: (1,1), (2,2), and (3,4). We want
to perform PCA on these points, so we construct a 2-by-2 matrix whose eigenvectors are the
directions that best represent these three points. Construct this matrix and identify, in
the list below, one of its elements.
'''
def question_5():
    A = numpy.matrix([
        [1, 1],
        [2, 2],
        [3, 4]
    ])
    
    print('The answer is any of the elements of the matrix that is the result of the multiplication A.T * A')
    print(A.T * A)

'''
Question 6

Find, in the list below, the vector that is orthogonal to the vector [1,2,3].
Note: the interesting concept regarding eigenvectors is "orthonormal," that is unit vectors
that are orthogonal. However, this question avoids using unit vectors to make the
calculations simpler.
'''
def question_6():
    v = [1, 2, 3]

    candidates = [[-1, -2, 0],
                  [0, 2, -1],
                  [-1, -1, 1],
                  [-1, 1, -1]]

    for cand in candidates:
        if numpy.dot(v, cand) == 0:
            print('And the answer is: {}'.format(cand))


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
    print('Question 4:')
    question_4()
    print()
    print('Question 5:')
    question_5()
    print()
    print('Question 6:')
    question_6()
    print()

if __name__ == '__main__':
    main()
