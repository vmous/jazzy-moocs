# -*- coding: utf-8 -*-

import numpy
import scipy.spatial


'''
Question 1

Consider the diagonal matrix M =
1 0 0
0 2 0
0 0 0
. Compute its Moore-Penrose pseudoinverse, and then identify, in the list below,
the true statement about the elements of the pseudoinverse.
'''
def question_1():
    M = numpy.matrix([
        [1, 0, 0],
        [0, 2, 0],
        [0, 0, 0]
    ])

    # Construct the diagonal U
    a, U, c = numpy.linalg.svd(M)
    U = numpy.diag(U)
    pinv = numpy.linalg.pinv(U)

    print(pinv)

'''
Question 2

An ad publisher selects three ads to place on each page, in order from the top.
Click-through rates (CTR's) at each position differ for each advertiser, and
each advertiser has a different CTR for each position. Each advertiser bids for
click-throughs, and each advertiser has a daily budget, which may not be
exceeded. When a click-through occurs, the advertiser pays the amount they bid.
In one day, there are 101 click-throughs to be auctioned.

Here is a table of the bids, CTR's for positions 1, 2, and 3, and budget for each
advertiser.

Advertiser Bid   CTR1  CTR2  CTR3  Budget
A          $.10  .015  .010  .005  $1
B          $.09  .016  .012  .006  $2
C          $.08  .017  .014  .007  $3
D          $.07  .018  .015  .008  $4
E          $.06  .019  .016  .010  $5

The publisher uses the following strategy to allocate the three ad slots:
i.   Any advertiser whose budget is spent is ignored in what follows.
ii.  The first slot goes to the advertiser whose expected yield for the first
     slot (product of the bid and the CTR for the first slot) is the greatest.
     This advertiser is ignored in what follows.
iii. The second slot goes to the advertiser whose expected yield for the second
     slot (product of the bid and the CTR for the second slot) is the greatest.
     This advertiser is ignored in what follows.
iv.  The third slot goes to the advertiser whose expected yield for the third
     slot (product of the bid and the CTR for the third slot) is the greatest.

The same three advertisers get the three ad positions until one of two things
happens:
i.  An advertiser runs out of budget, or
ii. All 101 click-throughs have been obtained.

Either of these events ends one phase of the allocation. If a phase ends because
an advertiser ran out of budget, then they are assumed to get all the clicks
their budget buys. During the same phase, we calculate the number of
click-throughs received by the other two advertisers by assuming that all three
received click-throughs in proportion to their respective CTR's for their
positions (round to the nearest integer). If click-throughs remain, the
publisher reallocates all three slots and starts a new phase.

If the phase ends because all click-throughs have been allocated, assume that
the three advertisers received click-throughs in proportion to their respective
CTR's (again, rounding if necessary).

Your task is to simulate the allocation of slots and to determine how many
click-throughs each of the five advertisers get.
'''
def question_2():
    P = [0.10, 0.09, 0.08, 0.07, 0.06]
    B = [1, 2, 3, 4, 5]
    M = numpy.matrix([
        [0.015, 0.010, 0.005],
        [0.016, 0.012, 0.006],
        [0.017, 0.014, 0.007],
        [0.018, 0.015, 0.008],
        [0.019, 0.016, 0.010]
    ])

    CTR = [0, 0, 0, 0, 0]

    # Expected yield
    E = numpy.diag(P) * M

    def phase():
        while sum(CTR) < 101 and sum(B) > 0:
            #1st column
            r1 = numpy.argmax(E[:,0] == numpy.max(E[:,0]))
            if B[r1] != 0:
                B[r1] = B[r1] - P[r1]
                CTR[r1] = CTR[r1] + 1
            if B[r1] - P[r1] < 0:
                B[r1] = 0
                E[r1,0] = -1

            #2nd Column
            #Temp E
            T = E
            T[r1,1] = -1
            r2 = numpy.argmax(T[:,1] == numpy.max(T[:,1]))
            if B[r2] != 0:
                B[r2] = B[r2] - P[r2]
                CTR[r2] = CTR[r2] + M[r2,1]/M[r1,0]
            if B[r2] - P[r2] < 0:
                B[r2] = 0
                E[r2,0] = -1        
            #3rd Column
            T[r1,2] = -1
            T[r2,2] = -1
            r3 = numpy.argmax(T[:,2] == numpy.max(T[:,2]))
            if B[r3] != 0:
                B[r3] = B[r3] - P[r3]
                CTR[r3] = CTR[r3] + M[r3,2]/M[r1,0]
    
            if B[r3] - P[r3] < 0:
                B[r3] = 0
                E[r3,0] = -1
            if B[r1] == 0 or B[r2] == 0 or B[r3] == 0:
                break
        for i in range(0,5):
            CTR[i] = round(CTR[i])
        
    #Phase1
    phase()
    print(B)
    print(CTR)
    print('===')
    #Phase2
    phase()
    print(B)
    print(CTR)
    print('===')
    #Phase3
    phase()
    print(B)
    print(CTR)
    print('===')


'''
Question 3

In certain clustering algorithms, such as CURE, we need to pick a representative
set of points in a supposed cluster, and these points should be as far away from
each other as possible. That is, begin with the two furthest points, and at each
step add the point whose minimum distance to any of the previously selected
points is maximum.

Suppose you are given the following points in two-dimensional Euclidean space:
x = (0,0); y = (10,10), a = (1,6); b = (3,7); c = (4,3); d = (7,7), e = (8,2);
f = (9,5). Obviously, x and y are furthest apart, so start with these. You must
add five more points, which we shall refer to as the first, second,..., fifth
points in what follows. The distance measure is the normal Euclidean L2-norm.
Which of the following is true about the order in which the five points are
added?
'''
def question_3():
    x = (0, 0)
    y = (10, 10)
    a = (1, 6)
    b = (3, 7)
    c = (4, 3)
    d = (7, 7)
    e = (8, 2)
    f = (9, 5)

    cluster = [x, y]
    points = [a, b, c, d, e, f]
 
    while points:
        max_dist = -1
        max_idx = -1
        for i, p in enumerate(points):
            p_min_dist = min([scipy.spatial.distance.euclidean(p,c) for c in cluster])
            if p_min_dist > max_dist:
                max_dist = p_min_dist
                max_idx = i

        cluster.append(points.pop(max_idx))

    print(cluster)


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


if  __name__ == '__main__':
    main()
