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

Question Explanation

Each m_ij is multiplied by v_j, and this product forms the value of a key-value
pair that has key i, the row number. Thus, in row-major order, the sixteen
key-value pairs produced are:

(1,1) (1,4) (1,9) (1,16)
(2,5) (2,12) (2,21) (2,32)
(3,9) (3,20) (3,33) (3,48)
(4,13) (4,28) (4,45) (4,64)
'''
def question_1():
    M = numpy.matrix([
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 15, 16],
    ])

    v = numpy.matrix([
        [1, 2, 3, 4]
    ]).T

    mr, mc = M.shape

    def map():
        t = []
        for i in range(mc):
            for j in range(mr):
                t.append((i, M.item(i, j) * v.item(j)))

        for x in t:
            print('({}, {})'.format(x[0]+1, x[1]))

        return t

    def reduce():
        r = numpy.zeros((mr, 1))
        r = numpy.asmatrix(r)
        for key, vals in itertools.groupby(t, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            r[key] = sum(vals)
            print('{}, {}'.format(key, sum(vals)))

        return r

    print(M * v)

    t = map()
    t = sorted(t, key=lambda x:x[0])
    r = reduce()

    print(r)


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

Question Explanation

In the Map function, each element of M is turned into a pair whose key is itself
a pair consisting of a row number of M and a column number of N. Thus, there are
xz different keys. We get a particular key (i,k) from each value j that is a
column number of M. Thus, M is turned into xyz key-value pairs. The Map function
also turns elements of N into pairs with the same key values, so xz is the total
number of different keys that Map produces. We get a pair with key (i,k) from each
value j that is a row number of N. Thus, there are another xyz key-value pairs
produced from N, and therefore a total of 2xyz key-value pairs output from the
Map function.
As input to the Reduce function, these 2xyz pairs are sorted by key. Since the
number of keys is xz, and each key is associated with y elements of M and y
elements of N, the lists in the input elements for Reduce have length 2y.
'''
def question_2():
    M = numpy.matrix([
        [1, 2, 3],
        [4, 5, 6],
    ])

    N = numpy.matrix([
        [20, 30, 30, 40, 50],
        [60, 70, 80, 90, 21],
        [31, 41, 51, 61, 71],
    ])

    mr, mc = M.shape
    nr, nc = N.shape

    # x = 2
    # y = 3
    # z = 5

    def map():
        t = []

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

        return t

    def reduce():
        r = numpy.zeros((mr, nc))  
        r = numpy.asmatrix(r)
        #reduce
        for key, vals in itertools.groupby(t, key=lambda x:x[0]):
            vals = [x[1] for x in vals]
            print('reducer {} has {} tuples'.format(key, len(vals)))
            a = sorted([x for x in vals if x[0] == 'M'], key=lambda x:x[1])
            b = sorted([x for x in vals if x[0] == 'N'], key=lambda x:x[1])

            v = zip([x[2] for x in a], [x[2] for x in b])

            i, j = key
            r[i, j] = sum([k*v for k,v in v])

        return r

    print(M * N)

    t = map()
    t = sorted(t, key=lambda x:x[0])
    print(len(t))
    r = reduce()
    print(r)


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

Question Explanation

Consider the first Map function. Each element of M is mapped to one pair, and
this pair has a key equal to its column number. There are y different columns
of M, and therefore that number of keys. Each column of M has x rows, and
therefore, M is transformed into x different pairs for each key. Similarly,
each element of N is mapped to a single pair, and the key of this pair is one
of its y row numbers. Thus, there are only y different keys among all the pairs
generated from M and N. Each row of N has z elements, so z pairs are produced
from N for each key. We conclude that the output of the first Map function has
y different keys, each key appears in x+z different pairs, and the total number
of pairs is y(x+z).

The first Reduce function has input consisting of y elements. Each element is a
key (i.e., a number that is a column of M and a row of N, associated with a
list of values giving that column of M and that row of N. The Reduce function
pairs the x elements of M on that list with the z elements of N on the same list,
producing another list associated with the same key. On that list is one element
for each of the xz pairs --- one element from M and the other from N. Thus,
the output of the first Reduce has y pairs, each with a list of length xz.

The second Map function takes each of the pairs from the first Reduce and turns
each element on the pair's list into a key-value pair. Since there are a y input
pairs, and each has a list of length xz, the number of key-value pairs output
from the second Map is xyz. Notice that a key consists of a row number from M
and a column number from N. Therefore, there are xz different keys, and there
are y different key-value pairs for each key.
'''
def question_3():
    M = numpy.matrix([
        [1, 2, 3],
        [4, 5, 6],
    ])

    N = numpy.matrix([
        [20, 30, 30, 40, 50],
        [60, 70, 80, 90, 21],
        [31, 41, 51, 61, 71],
    ])

    mr, mc = M.shape
    nr, nc = N.shape

    # x = 2
    # y = 3
    # z = 5

    # x + z = 7, got 7 YES
    # x + z = 7, got 3 NO 
    # z = 5, got 30 NO
    # x+z = 7, got 30 NO

    def map():
        m1 = []
        for i in range(mr):
            for j in range(mc):
                m1.append( (j, ('M', i, M[i,j])) )

        for j in range(nr):
            for k in range(nc):
                m1.append( (j, ('N', k, N[j,k])) )

        return m1

    def reduce():
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

    print(M * N)
    m1 = map()
    m1 = sorted(m1, key=lambda x:x[0])
    r = reduce()
    print(r)


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

Question Explanation

Map turns the tuples of R into (1,(R,0)), (2,(R,1)), and (3,(R,2)). The reason
is that each tuple has its B-value made the key, with an associated value that
is R paired with the A-value of that tuple. Similarly, the tuples of S are
turned into (0,(S,1)), (1,(S,2)), and (2,(S,3)). That is, each S-tuple has its
B-value made the key, with an associated value that is S paired with the C-value
of that tuple.

We then sort by key and for each key we construct an associated list of all
values for that key. There are four keys, 0 through 3 in this example. The
elements thus formed for these keys, and which are each input to one of the
Reduce tasks, are:

(0, [(S,1)])
(1, [(R,0), (S,2)])
(2, [(R,1), (S,3)])
(3, [(R,2)])
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
            if b not in h:
                continue
            for r in h[b]:
                j.append( (r, b, c) )

        return j

    def map():
        m = []
        for a, b in R:
            m.append( (b, ('R', a)) )
        for b, c in S:
            m.append( (b, ('S', c)) )

        return m

    def reduce():
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
    m = map()
    m = sorted(m, key=lambda x:x[0])
    r = reduce()
    print(r)


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
