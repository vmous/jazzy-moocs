# -*- coding: utf-8 -*-


import numpy
import itertools


'''
Question 1

Using the matrix-vector multiplication described in Section 2.3.1, applied to
the matrix and vector:

1  2  3  4  | 1
5  6  7  8  | 2
9  10 11 12 | 3
13 14 15 16 | 4

apply the Map function to this matrix and vector. Then, identify in the list
below, one of the key-value pairs that are output of Map.
'''
def question_1():

    M = numpy.array([
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 15, 16],
    ])

    v = numpy.array([1, 2, 3, 4])

    def mr(M, v):
        t = []
        mr, mc = M.shape
        for i in range(mc):
            for j in range(mr):
                t.append((i, M[i, j] * v[j]))

        t = sorted(t, key=lambda x:x[0])
        for x in t:
            print((x[0]+1, x[1]))

        r = numpy.zeros((mr, 1))
        for key, vals in itertools.groupby(t, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            r[key] = sum(vals)
            print('{}, {}'.format(key, sum(vals)))
        return r.transpose()

    print(numpy.dot(M, v.transpose()))
    print(mr(M, v))


'''
Question 2

Suppose we use the algorithm of Section 2.3.10 to compute the product of
matrices M and N. Let M have x rows and y columns, while N has y rows and
z columns. As a function of x, y, and z, express the answers to the following
questions:

  1. The output of the Map function has how many different keys? How many
     key-value pairs are there with each key? How many key-value pairs are there
     in all?

  2. The input to the Reduce function has how many keys? What is the length of
     the value (a list) associated with each key?

Then, identify the true statement in the list below.
'''
def question_2():
    M = numpy.array([
        [1, 2, 3],
        [4, 5, 6],
    ])

    N = numpy.array([
        [20, 30, 30, 40, 50],
        [60, 70, 80, 90, 21],
        [31, 41, 51, 61, 71],
    ])

    # x = 2
    # y = 3
    # z = 5

    def mr(M, N):
        t = []
        mr, mc = M.shape
        nr, nc = N.shape

        for i in range(mr):
            for j in range(mc):
                l = [ ((i, k), ('M', j, M[i,j])) for k in range(nc) ]
                print(len(l))
                t.extend(l)
                # for k in range(nc):
                #   t.append( ((i, k), ('M', j, M[i,j])) )

        for j in range(nr):
            for k in range(nc):
                l = [ ((i, k), ('N', j, N[j, k])) for i in range(mr) ]
                print(len(l))
                t.extend(l)
                # for i in range(mr):
                #   t.append( ((i, k), ('N', j, N[j, k])) )

        t = sorted(t, key=lambda x:x[0])
        print(len(t))

        r = numpy.zeros((mr, nc))  
        #reduce
        for key, vals in itertools.groupby(t, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            print('reducer {} has {} tuples'.format(key, len(vals)))
            a = sorted([x for x in vals if x[0] == 'M'], key=lambda x:x[1])
            b = sorted([x for x in vals if x[0] == 'N'], key=lambda x:x[1])

            v = zip([x[2] for x in a], [x[2] for x in b])
            if len(v) == 0:
                continue

            i, j = key
            r[i, j] = sum([k*v for k,v in v])
        return r

    print(numpy.dot(M, N))
    print(mr(M, N))


'''
Question 3

Suppose we use the two-stage algorithm of Section 2.3.9 to compute the product
of matrices M and N. Let M have x rows and y columns, while N has y rows and
z columns. As a function of x, y, and z, express the answers to the following
questions:

  1. The output of the first Map function has how many different keys? How many
     key-value pairs are there with each key? How many key-value pairs are there
     in all?

  2. The output of the first Reduce function has how many keys? What is the
     length of the value (a list) associated with each key?

  3. The output of the second Map function has how many different keys? How many
     key-value pairs are there with each key? How many key-value pairs are there
     in all?

Then, identify the true statement in the list below.
'''
def question_3():
    M = numpy.array([
        [1, 2, 3],
        [4, 5, 6],
    ])

    N = numpy.array([
        [20, 30, 30, 40, 50],
        [60, 70, 80, 90, 21],
        [31, 41, 51, 61, 71],
    ])

    # x = 2
    # y = 3
    # z = 5

    # x + z = 7, got 7 YES
    # x + z = 7, got 3 NO 
    # z = 5, got 30 NO
    # x+z = 7, got 30 NO

    def mr(M, N):
        m1 = []
        mr, mc = M.shape
        nr, nc = N.shape

        # map 1
        for i in range(mr):
            for j in range(mc):
                m1.append( (j, ('M', i, M[i,j])) )

        for j in range(nr):
            for k in range(nc):
                m1.append( (j, ('N', k, N[j,k])) )

        m1 = sorted(m1, key=lambda x:x[0])

        # reduce 1
        r1 = []
        for key, vals in itertools.groupby(m1, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            print(len(vals))
            ms = [x for x in vals if x[0] == 'M']
            ns = [x for x in vals if x[0] == 'N']
            for m in ms:
                for n in ns:
                    r1.append( ((m[1], n[1]), m[2]*n[2]) )

        m2 = r1[:]
        print(len(m2))
        m2 = sorted(m2, key=lambda x:x[0])

        r = numpy.zeros((mr, nc))
        for key, vals in itertools.groupby(m2, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            print(len(vals))
            r[key[0], key[1]] = sum(vals)
        return r

    print(numpy.dot(M, N))
    print(mr(M,N))


'''
Question 4

Suppose we have the following relations:

   R           S
 A   B       B   C
 0   1       0   1
 1   2       1   2
 2   3       2   3

and we take their natural join by the algorithm of Section 2.3.7. Apply the Map
function to the tuples of these relations. Then, construct the elements that are
input to the Reduce function. Identify one of these elements in the list below.
'''
def question_4():
    R = [
        (0, 1),
        (1, 2),
        (2, 3),
    ]

    S = [
        (0, 1),
        (1, 2),
        (2, 3),
    ]

    def hash_join(R, S):
        h = {}
        for a, b in R:
            h.setdefault(b, []).append(a)

        j = []
        for b, c in S:
            if not h.has_key(b):
                continue
            for r in h[b]:
                j.append( (r, b, c) )

        return j

    def mr(R, S):
        m = []
        for a, b in R:
            m.append( (b, ('R', a)) )
        for b, c in S:
            m.append( (b, ('S', c)) )

        m = sorted(m, key=lambda x:x[0])

        r = []
        for key, vals in itertools.groupby(m, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            print(key, vals)
            rs = [x for x in vals if x[0] == 'R']
            ss = [x for x in vals if x[0] == 'S']
            for ri in rs:
                for si in ss:
                    r.append( (ri[1], key, si[1]) )
        return r

    print(hash_join(R, S))
    print(mr(R, S))


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


if  __name__ == '__main__':
    main()
