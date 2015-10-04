# coding: utf-8
import math
import numpy

# b is probability of following the link
# M is the matrix of vectors based on the schma of exercice
# r is the list of node we are applying the M on every iteration
# S represent the change that the user doesn't follow the link but jump to a random link
# e is the choosen Pagerank precision
def power_iteration(b, M, r, S, e):
    r_copy = r
    # Calculate new values for r
    r = b * M * r
    # Add the leaking rank due to the probably that user may not follow the link
    r = r + S
    
    # Pagerank is processed when no value changed more than e on this iteration
    matrix_difference = r_copy - r
    is_processed = math.fabs(matrix_difference.max()) < e
    return (r, is_processed)

# b is probability of following the link
# M is the matrix of vectors based on the schma of exercice
# r is the list of node we are applying the M on every iteration
# S represent the change that the user doesn't follow the link but jump to a random link
# e is the choosen Pagerank precision
def page_rank(b, M, r, S, e):
    # Execute power interation until the Pagerank is processed
    while True:
        r, is_processed = power_iteration(b, M, r, S, e)
        if is_processed:
            print('Pagerank processed!')
            break
            
    return r


def question_1():
    print('Question 1')

    b = 0.7
    M = numpy.matrix([[0, 0, 0], [1/2, 0, 0], [1/2, 1, 1]])
    r = numpy.matrix([1/3, 1/3, 1/3]).T
    S = numpy.matrix([(1 - b)/3, (1 - b)/3, (1 - b)/3]).T
    e = 1 / 10000

    r = page_rank(b, M, r, S, e)

    r = 3 * r

    a = r.flat[0]
    b = r.flat[1]
    c = r.flat[2]
    print('a + c = 2.035: {0}'.format(round(a + c, 3) == 2.035))
    print('b + c = 2.5: {0}'.format(round(b + c, 3) == 2.5))
    print('b + c = 2.7: {0}'.format(round(b + c, 3) == 2.7))
    print('a + b = 0.55:  {0}'.format(round(a + b, 3) == 0.55))


def question_2():
    print('Question 2')

    b = 0.85
    M = numpy.matrix([[0, 0, 1], [1/2, 0, 0], [1/2, 1, 0]])
    r = numpy.matrix([1/3, 1/3, 1/3]).T
    S = numpy.matrix([(1 - b)/3, (1 - b)/3, (1 - b)/3]).T
    e = 1 / 10000

    r = page_rank(b, M, r, S, e)

    a = r.flat[0]
    b = r.flat[1]
    c = r.flat[2]
    print("c = .9b + .475a: {0}".format(round(c, 3) ==  round(0.9 * b + 0.475 * a, 3)))
    print(".95c = .9b + .475a: {0}".format(round(0.95 * c, 3) == round(0.9 * b + 0.475 * a,3)))
    print("a = c + .15b: {0}".format(round(a, 3) == round(c + 0.15 * b, 3)))
    print(".85a = c + .15b: {0}".format(round(0.85 * a, 3) == round(c + 0.15 * b, 3)))


def question_3():
    print('Question 3')

    b = 1
    M = numpy.matrix([[0, 0, 1], [1/2, 0, 0], [1/2, 1, 0]])
    r = numpy.matrix([1, 1, 1]).T
    S = numpy.matrix([0, 0, 0]).T
    e = 1 / 10000

    iteration = 1
    # Execute power interation until the Pagerank is processed
    while True:
        r, is_processed = power_iteration(b, M, r, S, e)
        if iteration == 5:
            a_5 = round(r.flat[0], 3)
            b_5 = round(r.flat[1], 3)
            c_5 = round(r.flat[2], 3)
        if is_processed:
            a = round(r.flat[0], 3)
            b = round(r.flat[1], 3)
            c = round(r.flat[2], 3)
            break
        iteration = iteration + 1

    print('Pagerank at 5th iteration: a={0}, b={1}, c={2}'.format(a_5, b_5, c_5))
    print('Pagerank at limit: a={0}, b={1}, c={2}'.format(a, b, c))
    print('After iteration 5, c = 9/8: {0}'.format(c_5 == 9 / 8))
    print('After iteration 5, c = 7/4: {0}'.format(c_5 == 7/4))
    print('After iteration 5, a = 21/16: {0}'.format(a_5 == 21/16))
    print('In the limit, b = 1/2: {0}'.format(b == 1/3))


if __name__ == '__main__':
    question_1()
    print('')
    question_2()
    print('')
    question_3()

