# -*- coding: utf-8 -*-


import math
import numpy


'''
The HITS' iterative step (Generic attempt, not tested).

Parameters
----------
L : numpy.matrix
    The link matrix representation of the Web Graph.
h : numpy.matrix
    Column vector of hubs.
a : numpy.matrix
    Column vector of authorities.
'''
def repeated_matrix_powering(L, h, a):
    h_old = h
    a_old = a

    h = L * a
    a = L.T * h
    a = a / a.max()
    h = h / h.max()

    return (h, a)


'''
The HITS algorithm (Generic attempt, not tested).

Parameters
----------
L : numpy.matrix
    The link matrix representation of the Web Graph.
iter_num : int
    The number of iteration to be performed.
'''
def hits(L, iter_num):
    # Check the input is a matrix...
    assert(isinstance(L, numpy.matrix)), 'The input array is not a numpy.matrix.'
    # ... and is squared
    shape = L.shape
    assert(shape[0] == shape[1]), 'The input matrix is not a square matrix.'
    n = shape[0]

    # Initialize hubbiness and authoritiveness
    h = a = numpy.empty(n)
    h.fill(1/math.sqrt(n))
    a.fill(1/math.sqrt(n))
    h = numpy.asmatrix(h).T
    a = numpy.asmatrix(a).T
    
    # Execute repeated matrix powering until HITS has converged
    for _ in range(iter_num):
        h, a = repeated_matrix_powering(L, h, a)
            
    return (h, a)


'''
The HITS' iterative step (MMDS excercise version).

Parameters
----------
L : numpy.matrix
    The link matrix representation of the Web Graph.
h : numpy.matrix
    Column vector of hubs.
a : numpy.matrix
    Column vector of authorities.
'''
def repeated_matrix_powering_mmds(L, h, a):
    h_old = h
    a_old = a

    a = L.T * h
    a = a / a.max()
    h = L * a
    h = h / h.max()

    return (h, a)


'''
The HITS algorithm (MMDS excercise version).

Parameters
----------
L : numpy.matrix
    The link matrix representation of the Web Graph.
h : numpy.matrix
    Column vector of hubs.
a : numpy.matrix
    Column vector of authorities.
iter_num : int
    The number of iteration to be performed.
'''
def hits_mmds(L, h, a, iter_num):    
    # Execute repeated matrix powering until HITS has converged
    for _ in range(iter_num):
        h, a = repeated_matrix_powering_mmds(L, h, a)
            
    return (h, a)
