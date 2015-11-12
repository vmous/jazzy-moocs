# -*- coding: utf-8 -*-


import numpy


'''
Question 1

Note: In this question, all columns will be written in their transposed form,
as rows, to make the typography simpler. Matrix M has three rows and two
columns, and the columns form an orthonormal basis. One of the columns is
[2/7,3/7,6/7]. There are many options for the second column [x,y,z]. Write down
those constraints on x, y, and z. Then, identify in the list below the one
column that could be [x,y,z]. All components are computed to three decimal
places, so the constraints may be satisfied only to a close approximation.

Question Explanation

The dot product of [2/7,3/7,6/7] and [x,y,z] must be 0, so 2x+3y+6z=0. Also, the
length of the vector [x,y,z] must be 1, so x2+y2+z2 = 1. Any vector satisfying
these two constraints is a possible answer.
'''
def question_1():
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
Question 2

Note: In this question, all columns will be written in their transposed form, as
rows, to make the typography simpler. Matrix M has three rows and three columns,
and the columns form an orthonormal basis. One of the columns is
[2/7, 3/7, 6/7], and another is [6/7, 2/7, -3/7]. Let the third column
be [x,y,z]. Since the length of the vector [x,y,z] must be 1, there is a
constraint that x^2+y^2+z^2 = 1. However, there are other constraints, and these
other constraints can be used to deduce facts about the ratios among x, y, and z.
Compute these ratios, and then identify one of them in the list below.

Question Explanation

The dot product of [x,y,z] with each of the two other columns must be 0. Thus,
2x+3y+6z=0 and 6x+2y-3z=0. Add the first equation to twice the second and obtain
14x+7y=0. From this equation, we deduce y = -2x. If we make this substitution
for y, the first equation becomes -4x+6z=0, or 2x = 3z. Combining these two
ratios, we find y = -3z.
'''
def question_2():
    print('Solve the system of equations:')
    print('[2/7, 3/7, 6/7] * [x, y, z]')
    print('[6/7, 2/7, -3/7] * [x, y, z]')


'''
Question 3

Suppose we have three points in a two dimensional space: (1,1), (2,2),
and (3,4). We want to perform PCA on these points, so we construct a
2-by-2 matrix whose eigenvectors are the directions that best represent these
three points. Construct this matrix and identify, in the list below, one of its
elements.

Construct the matrix M whose columns correspond to the dimensions of the space
and whose rows correspond to the points. That is,

    1 1
M = 2 2
    3 4

Compute M^T * M, which is

14 17
17 21

This is the matrix whose eigenvectors are the principal components. Thus, the
correct answers are 14, 17, and 21.
'''
def question_3():
    A = numpy.matrix([
        [1, 1],
        [2, 2],
        [3, 4]
    ])
    
    print('The answer is any of the elements of the matrix that is the result of the multiplication A.T * A')
    print(A.T * A)


'''
Question 4

Find, in the list below, the vector that is orthogonal to the vector [1,2,3].
Note: the interesting concept regarding eigenvectors is "orthonormal," that is
unit vectors that are orthogonal. However, this question avoids using unit
vectors to make the calculations simpler.

Question Explanation

Vectors are orthogonal if and only if their dot product (sum of the products of
their corresponding components) is 0. For example, one of the correct choices is
[-4, -1, 2] The dot product of this vector with [1,2,3] is
1*(-4) + 2*(-1) + 3*2 = -4 + -2 + 6 = 0.
'''
def question_4():
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


if __name__ == '__main__':
    main()
