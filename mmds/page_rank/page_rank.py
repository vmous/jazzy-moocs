# -*- coding: utf-8 -*-


import math


'''
The power iteration step.

Parameters
----------
b : int
    Probability of following the link.
M : numpy.matrix
    The matrix of vectors based on the schma of exercice.
r : numpy.matrix
    Column vector of nodes we are applying the M on every iteration.
S : numpy.matrix
    Represent the chance that the user doesn't follow the link but jump to a
    random link.
e : float
    The choosen PageRank precision.
'''
def power_iteration(b, M, r, S, e):
    r_old = r
    # Calculate new values for r
    r = b * M * r
    # Add the leaking rank due to the probably that user may not follow the link
    r = r + S
    
    # Pagerank is processed when no value changed more than e on this iteration
    matrix_difference = r_old - r
    converged = math.fabs(matrix_difference.max()) < e
    return (r, converged)


'''
The PageRank algorithm.

Parameters
----------
b : int
    Probability of following the link.
M : numpy.matrix
    The matrix of vectors based on the schma of exercice.
r : numpy.matrix
    Column vector of nodes we are applying the M on every iteration.
S : numpy.matrix
    Represent the chance that the user doesn't follow the link but jump to a
    random link.
e : float
    The choosen PageRank precision.
'''
def page_rank(b, M, r, S, e):
    # Execute power interation until the Pagerank is processed
    while True:
        r, converged = power_iteration(b, M, r, S, e)
        if converged:
            print('Pagerank converged!')
            break
            
    return r
