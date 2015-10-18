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
'''
def question_1():
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


    D = numpy.matrix([
        [2, 0, 0, 0, 0, 0, 0, 0],
        [0, 2, 0, 0, 0, 0, 0, 0],
        [0, 0, 3, 0, 0, 0, 0, 0],
        [0, 0, 0, 3, 0, 0, 0, 0],
        [0, 0, 0, 0, 3, 0, 0, 0],
        [0, 0, 0, 0, 0, 3, 0, 0],
        [0, 0, 0, 0, 0, 0, 3, 0],
        [0, 0, 0, 0, 0, 0, 0, 3]
    ])

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

The goal is to find two clusters in this graph using Spectral Clustering on the Laplacian matrix.
Compute the Laplacian of this graph. Then compute the second eigen vector of the Laplacian (the
one corresponding to the second smallest eigenvalue).

To cluster the points, we decide to split at the mean value. We say that a node is a tie if its
value in the eigen-vector is exactly equal to the mean value. Let's assume that if a point is a tie,
we choose its cluster at random. Identify the true statement from the list below.
'''
def question_2():
    # Pre-processing:
    # Construct a matrix representation of the graph
    
    # Adjacency matrix A
    A = numpy.matrix([
        [0, 1, 1, 0, 0, 0],
        [1, 0, 0, 1, 0, 1],
        [1, 0, 0, 1, 0, 0],
        [0, 1, 1, 0, 1, 0],
        [0, 0, 0, 1, 0, 1],
        [0, 1, 0, 0, 1, 0]
    ])

    # Degree matrix D
    D = numpy.matrix([
        [2, 0, 0, 0, 0, 0],
        [0, 3, 0, 0, 0, 0],
        [0, 0, 2, 0, 0, 0],
        [0, 0, 0, 3, 0, 0],
        [0, 0, 0, 0, 2, 0],
        [0, 0, 0, 0, 0, 2]        
    ])

    # Graph Laplacian matrix L
    L = D - A

#    TEST = numpy.matrix([
#        [3, -1, -1, -1, 0, 0],
#        [-1, 2, -1, 0, 0, 0],
#        [-1, -1, 3, 0, 0, -1],
#        [-1, 0, 0, 3, -1, -1],
#        [0, 0, 0, -1, 2, -1],
#        [0, 0, -1, -1, -1, 3]
#    ])

    # Decomposition
    # Compute eigenvalues and eigenvectors of the Laplacian matrix
    # Specifically we are interested in the second smallest eigenvalue λ_2 and
    # it corresponding eigenvector x

#    eigvals, eigvecs = numpy.linalg.eigh(TEST)
    eigvals, eigvecs = numpy.linalg.eigh(L)

    # sort the eigenvalues ascending, the first K zero eigenvalues represent the
    # connected components
    idx = eigvals.argsort()
    eigvals.sort()
    evecs = eigvecs[:, idx]
    print(evecs)
    eigvecs = evecs[:, 0:2]
    print()
    print()
    print(eigvecs
)
   
    print(numpy.around(eigvals, decimals=1))
    for vec in eigvecs.T[1:]:
        #vec = vec.real
        print(numpy.around(vec, decimals=1))


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()

if __name__ == '__main__':
    main()
