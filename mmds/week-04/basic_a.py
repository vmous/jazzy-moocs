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

Question Explanation

When we subtract the average for each row from the entries in that column, we
get:

   M  N  P  Q  R
A -2 -1  0  1  2
B -1  0 -1  2  0
C  1  1  1 -1 -2

For instance, the average of the row for A is 3, so 3 is subtracted from each
entry in the first row. The average in row C is 4, so we subtract 4 from each
entry in the bottom row. Then, in the table above, we average each column and
subtract the average from the entries in that column, getting:

     M    N    P    Q    R
A -4/3   -1    0  1/3    2
B -1/3    0   -1  4/3    0
C  5/3    1    1 -5/3   -2

For instance, the average of the first column is -2/3, so we add 2/3 to each
of the entries in the first column.
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


'''                                                                                                                                                                                                                             QQuestion 2                                                                                                                                                                                                                       
Below is a table giving the profile of three items.

A 1 0 1 0 1 2
B 1 1 0 0 1 6
C 0 1 0 1 0 2

The first five attributes are Boolean, and the last is an integer "rating." Assume
that the scale factor for the rating is α. Compute, as a function of α, the cosine
distances between each pair of profiles. For each of α = 0, 0.5, 1, and 2, determine
the cosine of the angle between each pair of vectors. Which of the following is FALSE?

 * For α = 2, C is closer to B than A is. 
 * For α = 0.5, A is closer to B than C is. 
 * For α = 1, C is closer to B than A is. 
 * For α = 1, A is closer to B than C is.

Question Explanation

First, we must compute the dot products of the three pairs of vectors.

A.B = 2 + 12α2
A.C = 4α2
B.C = 1 + 12α2

Next, compute the lengths of the vectors:

|A| = sqrt(3 + 4α2)
|B| = sqrt(3 + 36α2)
|C| = sqrt(2 + 4α2)

From these, we can compute the cosines of the angles between each pair of vectors:

cos(A,B) = (2 + 12α2)/sqrt(9 + 120α2 + 144α4)
cos(A,C) = (4α2)/sqrt(6 + 20α2 + 16α4)
cos(B,C) = (1 + 12α2)/sqrt(6 + 84α2 + 144α4)

The approximate values of these cosines for the four values of α mentioned are:

         α = 0  α = 0.5  α = 1  α = 2
cos(A,B)   .67      .72  .8473  .9461
cos(A,C)     0      .08    .62    .87
cos(B,C)   .41      .67  .8498  .9526
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


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()


if __name__ == '__main__':
    main()
