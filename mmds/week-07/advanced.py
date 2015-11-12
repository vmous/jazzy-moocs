# -*- coding: utf-8 -*-


import math
import numpy
import sys
sys.path.append('../hits')
from hits import hits_mmds


'''
Question 1

Suppose we have an LSH family h of (d1,d2,.6,.4) hash functions. We can use
three functions from h and the AND-construction to form a (d1,d2,w,x) family,
and we can use two functions from h and the OR-construction to form a
(d1,d2,y,z) family. Calculate w, x, y, and z, and then identify the correct
value of one of these in the list below. 
'''
def question_1():
    a = 0.6
    b = 0.4

    w = numpy.power(a, 3)
    x = numpy.power(b, 3)

    y = 1 - numpy.power(1 - a, 2)
    z = 1 - numpy.power(1 - b, 2)

    print('w = {:.6f}'.format(w))
    print('x = {:.6f}'.format(x))
    print('y = {:.6f}'.format(y))
    print('z = {:.6f}'.format(z))


'''
Question 2

Here are eight strings that represent sets:

s1 = abcef
s2 = acdeg
s3 = bcdefg
s4 = adfg
s5 = bcdfgh
s6 = bceg
s7 = cdfg
s8 = abcd

Suppose our upper limit on Jaccard distance is 0.2, and we use the indexing
scheme of Section 3.9.4 based on symbols appearing in the prefix (no position
or length information). For each of s1, s3, and s6, determine how many other
strings that string will be compared with, if it is used as the probe string.
Then, identify the true count from the list below. 
'''
def question_2():
    strs = {
        's1' : 'abcef',
        's2' : 'acdeg',
        's3' : 'bcdefg',
        's4' : 'adfg',
        's5' : 'bcdfgh',
        's6' : 'bceg',
        's7' : 'cdfg',
        's8' : 'abcd',
    }

    def index(vals, J):
        idx = {}
        for val in vals:
            n = int(numpy.floor(J * len(val) + 1))
            for i in range(n):
                idx.setdefault(val[i:i+1], []).append(val)
        return idx

    def search(idx, J, str):
        r = []
        n = int(numpy.floor(J * len(str) + 1))
        for i in range(n):
            r.extend(idx[str[i:i+1]])
        return r

    idx = index(strs.values(), 0.2)
    for v in ['s1', 's3', 's6']:
        val = strs[v]
        res = [x for x in search(idx, 0.2, val) if x != val]
        print('{}: {} ({})'.format(v, len(res), len(set(res))))


'''
Question 3

Consider the link graph

https://d396qusza40orc.cloudfront.net/mmds/images/otc_pagerank4.gif

First, construct the L, the link matrix, as discussed in Section 5.5 on the
HITS algorithm. Then do the following:

1. Start by assuming the hubbiness of each node is 1; that is, the vector h is (the transpose of) [1,1,1,1].
2. Compute an estimate of the authority vector a=LTh.
3. Normalize a by dividing all values so the largest value is 1.
4. Compute an estimate of the hubbiness vector h=La.
5. Normalize h by dividing all values so the largest value is 1.
6. Repeat steps 2-5.

Now, identify in the list below the true statement about the final estimates.
'''
def question_3():
    # Link matrix
    L = numpy.matrix([
        [0, 1, 1, 0],
        [1, 0, 0, 0],
        [0, 0, 0, 1],
        [0, 0, 1, 0]
    ])

    # Hubbiness
    h = numpy.matrix([[1, 1, 1, 1]]).T
    # Authoritativeness
    a = numpy.matrix([[0, 0, 0, 0]]).T

    (h, a) = hits_mmds(L, h, a, 2)

    print('Hubbiness:')
    print(h)
    print()
    print('Authoritativeness:')
    print(a)


'''
Question 4

Consider an implementation of the Block-Stripe Algorithm discussed in
Section 5.2 to compute page rank on a graph of N nodes (i.e., Web pages).
Suppose each page has, on average, 20 links, and we divide the new rank vector
into k blocks (and correspondingly, the matrix M into k stripes). Each stripe of
M has one line per "source" web page, in the format:

[source_id, degree, m, dest_1, ...., dest_m]

Notice that we had to add an additional entry, m, to denote the number of
destination nodes in this stripe, which of course is no more than the degree of
the node. Assume that all entries (scores, degrees, identifiers,...) are encoded
using 4 bytes.

There is an additional detail we need to account for, namely, locality of links.
As a very simple model, assume that we divide web pages into two disjoint sets:

1. Introvert pages, which link only to other pages within the same host as
   themselves.
2. Extrovert pages, which have links to pages across several hosts.

Assume a fraction x of pages (0 ≤ x ≤ 1) are introverts, and the rest are
extroverts. The blocks are arranged such that pages within a host are in the
same block. For simplicity, assume that the links from the extrovert pages are
spread uniformly across the k stripes (this is reasonably accurate for small
values of k).

Construct a formula that counts the amount of I/O per page rank iteration in
terms of N, x, and k. The 4-tuples below list combinations of N, k, x, and I/O
(in bytes). Pick the correct combination.

Note. There are some additional optimizations one can think of, such as striping
the old score vector, encoding introvert and extrovert pages using different
schemes, etc. For the purposes of working this problem, assume we don't do any
optimizations beyond the block-stripe algorithm discussed in class.
'''
def question_4():
    def f(k, x):
        return 4*(21 + k + 3*(x + (1 - x)*k))

    print([f(k, x) for k, x in [(2, 0.5), (2, 0.75), (3, 0.75), (3, 0.5)]]) 


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
