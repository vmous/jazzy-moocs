# -*- coding: utf-8 -*-


import math
import numpy
import scipy.spatial


'''
Question 1

We wish to cluster the following set of points: (25,125), (44,105), (29,97),
(35,63), (55,63), (42,57), (23,40), (64,37), (33,22), (55,20), (28,145),
(65,140), (50,130), (38,115), (55,118), (50,90), (43,83), (63,88), (50,60),
(50,30) into 10 clusters:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_gold.gif

We initially choose each of the green points (25,125),
(44,105), (29,97), (35,63), (55,63), (42,57), (23,40), (64,37), (33,22), and
(55,20) as a centroid. Assign each of the gold points to their nearest centroid.
Then, recompute the centroids of each of the clusters. Do any of the points then
get reassigned to a new cluster on the next round? Identify the true statement
in the list below. Each statement refers either to a centroid AFTER
recomputation of centroids (precise to one decimal place) or to a point that
gets reclassified.

Question Explanation

(28,145) and (50,130) are assigned to the cluster of (25,125), so the new
centroid for this cluster is (34.3,133,3).

(43,83) is assigned to the cluster of (29,97). No other points are assigned to
this cluster, so the new centroid for this cluster is (36,90).

(50,60) is assigned to the cluster of (55,63). No other points are assigned to
this cluster, so the new centroid for this cluster is (52.5,61.5).

(50,30) is assigned to the cluster of (55,20). No other points are assigned to
this cluster, so the new centroid for this cluster is (52.5,25).

The remaining five gold points are assigned to the cluster of (44,105). The
centroid of the six points in this cluster is (52.5,109.3).
'''
def question_1():

    centroids = [(25,125),
                 (44,105),
                 (29,97),
                 (35,63),
                 (55,63),
                 (42,57),
                 (23,40),
                 (64,37),
                 (33,22),
                 (55,20)]

    points = [(25,125),
               (44,105),
               (29,97),
               (35,63),
               (55,63),
               (42,57),
               (23,40),
               (64,37),
               (33,22),
               (55,20),
               (28,145),
               (65,140),
               (50,130),
               (38,115),
               (55,118),
               (50,90),
               (43,83),
               (63,88),
               (50,60),
               (50,30)]

    iter_num = 2
    while iter_num > 0:
        iter_num -= 1

        res = [[] for _ in range(len(centroids))]

        for p in points:
            min_dist = float('inf')
            min_dim = -1
            for i, c in enumerate(centroids):
                dist = scipy.spatial.distance.euclidean(p, c)
                if dist < min_dist:
                    min_dim = i
                    min_dist = dist

            assert(min_dim >= 0)

            res[min_dim].append(p)

        print('Allocation: {}'.format(res))

        for i, r in enumerate(res):
            c_x = numpy.mean([x for (x, _) in r])
            c_y = numpy.mean([y for (_, y) in r])
            centroids[i] = (c_x, c_y)

        print('New centroids: {}'.format(centroids))


'''
Question 2

When performing a k-means clustering, success depends very much on the initially
chosen points. Suppose that we choose two centroids (a,b) = (5,10) and
(c,d) = (20,5), and the data truly belongs to two rectangular clusters, as
suggested by the following diagram:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_sq-clust.gif

Under what circumstances will the initial clustering be successful? That is,
under what conditions will all the yellow points be assigned to the centroid
(5,10), while all of the blue points are assigned to cluster (20,5))? Identify
in the list below, a pair of rectangles (described by their upper left corner,
UL, and their lower-right corner LR) that are successfully clustered.

Question Explanation

The key observation is that all points to the left of the perpendicular bisector
of the line from (a,b) = (5,10) to (c,d) = (20,5) will be clustered with (a,b),
while those to the right will be clustered with (c,d). Examine the diagram below:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_sq-clust-bis.gif

Notice that for all the yellow points to lie to the left of the perpendicular
bisector, it is both necessary and sufficient that the lower-right corner of the
yellow rectangle is to the left of that line. Similarly, it is necessary and
sufficient that the upper-right corner of the blue rectangle lie to the right
of that line.

Suppose (x,y) is the lower-right corner of the yellow. Then the distance between
(x,y) and (5,10) is sqrt((x-5)2+(y-10)2), and the distance from (x,y) to (20,5)
is sqrt((x-20)2+(y-5)2). Since we want (x,y) to be closer to (5,10), we need
(x-5)2+(y-10)2 < (x-5)2+(y-10)2, or x < 10+y/3. Likewise, for the upper-left
corner of the blue to be to the right of the bisector, we need the opposite,
x > 10+y/3 when (x,y) is the upper-left corner.
'''
def question_2():
    ab = (5,10)
    cd = (20,5)

    def is_rec_closer_to_one_than_other(ullr, one, other):
        ul = ullr[0]
        lr = ullr[1]
        assert(ul[0] < lr[0])
        assert(ul[1] > lr[1])

        for x in range(ul[0], lr[0] + 1):
            if scipy.spatial.distance.euclidean((x, lr[1]), one) > scipy.spatial.distance.euclidean((x, lr[1]), other) or \
               scipy.spatial.distance.euclidean((x, ul[1]), one) > scipy.spatial.distance.euclidean((x, ul[1]), other):
                return False

        for y in range(lr[1], ul[1] + 1):
            if scipy.spatial.distance.euclidean((ul[0], y), one) > scipy.spatial.distance.euclidean((ul[0], y), other) or \
               scipy.spatial.distance.euclidean((lr[0], y), one) > scipy.spatial.distance.euclidean((lr[0], y), other):
                return False

        return True
    
    yellow = [[(6, 15), (13, 7)],
              [(3, 3), (10, 1)],
              [(3, 3), (10, 1)],
              [(7, 8), (12, 5)]]

    blue = [[(16, 16), (18, 5)],
            [(15, 14), (20, 10)],
            [(13, 10), (16, 4)],
            [(13, 10), (16, 4)]]

    for yb in zip(yellow, blue):
        if is_rec_closer_to_one_than_other(yb[0], ab, cd) and is_rec_closer_to_one_than_other(yb[1], cd, ab):
            print('Found! Yellow: {} and Blue: {}'.format(yb[0], yb[1]))

from matplotlib import pyplot
from matplotlib.patches import Rectangle
def question_2_1():
    centroids = numpy.array([(5,10), (20,5)])

    def plot_pic(Yellow, Blue):
        pyplot.plot(centroids[0,0],centroids[0,1], 'yo')
        pyplot.plot(centroids[1,0],centroids[1,1], 'bo')
        pyplot.axes().set_xlim(0,25)
        pyplot.axes().set_ylim(0,25)
        pyplot.gca().add_patch(Rectangle((Yellow[0][0],Yellow[1][1]),Yellow[1][0]-Yellow[0][0],Yellow[0][1]-Yellow[1][1], facecolor='yellow'))
        pyplot.gca().add_patch(Rectangle((Blue[0][0],Blue[1][1]),Blue[1][0]-Blue[0][0],Blue[0][1]-Blue[1][1], facecolor='blue'))

    def plot_perpendicular_bisector(centroids):
        midpoint = ((centroids[0][0]+centroids[1][0])/2,  (centroids[0][1]+centroids[1][1])/2)
        print(midpoint)
        slope = ((centroids[1][1]-centroids[0][1])/(centroids[1][0]-centroids[0][0]))
        slope_perp = -1/slope
        b = midpoint[1] - slope_perp * midpoint[0]
        pyplot.plot(numpy.linspace(0,25,1000), numpy.linspace(0,25,1000) * slope_perp + b, 'r')

    Yellow1 = ((7,8), (12,5))
    Blue1 = ((13,10), (16,4))
    plot_perpendicular_bisector(centroids)
    plot_pic(Yellow1, Blue1)
    pyplot.show()

    Yellow2 = ((7,12),(12,8))
    Blue2 = ((16,19), (25,12))
    plot_perpendicular_bisector(centroids)
    plot_pic(Yellow2, Blue2)
    pyplot.show()

    Yellow3 = ((6,7),(11,4))
    Blue3 = ((14,10),(23,6) )
    plot_perpendicular_bisector(centroids)
    plot_pic(Yellow3, Blue3)
    pyplot.show()

    Yellow3 = ((3,15),(13,7))
    Blue3 = ((14,10),(23,6) )
    plot_perpendicular_bisector(centroids)
    plot_pic(Yellow3, Blue3)
    pyplot.show()


'''
Question 3

Suppose we apply the BALANCE algorithm with bids of 0 or 1 only, to a situation
where advertiser A bids on query words x and y, while advertiser B bids on query
words x and z. Both have a budget of $2. Identify in the list below a sequence
of four queries that will certainly be handled optimally by the algoriethm.

Question Explanation

The explanations are associated with the various incorrect choices.
'''
def question_3():
    def maxbudget(bidders):
        s = sorted(bidders, key=lambda x:-x[2])
        return [x for x in s if x[2] == s[0][2]]

    def sim(qs, bidders, tie=0):
        R = 0
        for q in qs:
            sel = [b for b in bidders if b[1].find(q) >= 0 and b[2] > 0]
            if len(sel) == 0:
                continue
            sel = maxbudget(sel)
            if len(sel) > 1:
                s = sel[tie]
            else:
                s = sel[0]
            R += 1
            s[2] -= 1
        return R

    for sol in ['yxxy', 'xyzx', 'xzzx', 'xxxz']:
        r = min(
            [sim(sol, [ ['A', 'xy', 2], ['B', 'xz', 2] ], t) for t in [0, 1]]
        )
        print('{} => {}'.format(sol, r))


'''
Question 4

The set cover problem is: given a list of sets, find a smallest collection of
these sets such that every element in any of the sets is in at least one set of
the collection. As we form a collection, we say an element is covered if it is
in at least one set of the collection. Note: In this problem, we shall represent
sets by concatenating their elements, without brackets or commas. For example,
{A,B} will be represented simply as AB. There are many greedy algorithms that
could be used to pick a collection of sets that is close to as small as possible.
Here are some that you will consider in this problem. Dumb: Select sets for the
collection in the order in which they appear on the list. Stop when all elements
are covered. Simple: Consider sets in the order in which they appear on the
list. When it is considered, select a set if it has at least one element that is
not already covered. Stop when all elements are covered. Largest-First: Consider
sets in order of their size. If there are ties, break the tie in favor of the
one that appears first on the list. When it is considered, select a set if it
has at least one element that is not already covered. Stop when all elements are
covered. Most-Help: Consider sets in order of the number of elements they
contain that are not already covered. If there are ties, break the tie in favor
of the one that appears first on the list. Stop when all elements are covered.
Here is a list of sets: AB, BC, CD, DE, EF, FG, GH, AH, ADG, ADF First,
determine the optimum solution, that is, the fewest sets that can be selected
for a collection that covers all eight elements A,B,...,H. Then, determine the
sizes of the collections that will be constructed by each of the four algorithms
mentioned above. Compute the ratio of the size returned by the algorithm to the
optimum size, and identify one of these ratios in the list below, correct to two
decimal places.

Question Explanation

First, there are several collections of four sets that cover all elements. An
example is {AB, CD, EF, GH}. There can be no smaller collection. To see why, if
there are only three sets in the collection, then the sum of the sizes of these
three sets must be at least 8, so two of the three sets must be ADG and ADF. But
only four elements are covered by these two sets, so at least two more sets must
be in the collection. Algortithm Dumb picks the first seven sets, AB, BC, CD, DE,
EF, FG, GH. At that point, all eight elements are covered, so it stops with a
collection of size 7. The ratio for Dumb is therefore 1.75. Algorithm Simple
considers the same sets, and accepts each one, because each has an element not
yet covered. Thus, its ratio is also 1.75. Algorithm Largest-First begins by
taking ADG for the collection. It then considers ADF, and adds it to the
collection because F is not yet covered. Then, it considers the sets of size 2
in the order listed. AB is accepted because it covers B. BC is accepted because
it covers C. CD is not accepted because C and D are both covered already. DE is
accepted because it covers E. Then EF and FG are not accepted, but GH is
accepted because it covers H. Thus, the collection constructed by Largest-First is
{ADG, ADF, AB, BC, DE, GH}, and its ratio is 1.50. Algorithm Most-Help also begins
by taking ADG for the collection. But now there are two sets that add 2 to the
collection: BC and EF. BC is taken first, because it precedes EF on the list, but
then EF is taken. At this point, only H is missing, and the first set on the list
that has H is GH, so that set is taken next. The collection constructed by
Most-Help is {ADG, BC, EF, GH}, and its ratio is 1.00.
'''
def question_4():
    list_of_sets = ['AB', 'BC', 'CD', 'DE', 'EF', 'FG', 'GH', 'AH', 'ADG', 'ADF']
    print('List of sets: {}'.format(' '.join(list_of_sets)))
    print('Optimal')
    optimal = ['AB', 'CD', 'EF', 'GH']
    print('{}: length {}'.format(' '.join(optimal), len(optimal)))
    print('Dumb: Select sets for the collection in the order in which they appear on the list. Stop when all elements are covered.')
    dumb = ['AB', 'BC', 'CD', 'DE', 'EF', 'FG', 'GH']
    print('{}, length: {}, dumb/optimal: {}'.format(' '.join(dumb), len(dumb), len(dumb)/len(optimal)))
    print('Simple: Consider sets in the order in which they appear on the list. When it is considered, select a set if it has at least one element that is not already covered. Stop when all elements are covered.')
    simple = ['AB', 'BC', 'CD', 'DE', 'EF', 'FG', 'GH']
    print('{}, length: {}, simple/optimal:{}'.format(' '.join(simple), len(simple), len(simple)/len(optimal)))
    print('Largest-First: Consider sets in order of their size. If there are ties, break the tie in favor of the one that appears first on the list. When it is considered, select a set if it has at least one element that is not already covered. Stop when all elements are covered.')
    largest_first = ['ADG', 'ADF', 'AB', 'BC', 'DE', 'GH']
    print('{}, length: {}, largest-first/optimal:{}'.format(' '.join(largest_first), len(largest_first), len(largest_first)/len(optimal)))
    print('Most-Help: Consider sets in order of the number of elements they contain that are not already covered. If there are ties, break the tie in favor of the one that appears first on the list. Stop when all elements are covered.')
    most_help = ['ADG', 'BC', 'EF', 'GH']
    print('{}, length: {}, most-help/optimal:{}'.format(' '.join(most_help), len(most_help), len(most_help)/len(optimal)))


'''
Question 5

This bipartite graph:

https://d396qusza40orc.cloudfront.net/mmds/images/otc.gif

Has several perfect matchings. Find all the perfect matchings and then identify,
in the list below, a pair of edges that can appear together in a perfect
matching.

Question Explanation

Suppose a0 is matched with b0. Then a2 can only match with b4. So a4 is forced
to match with b3. Next, a1 is forced to match with b2, and finally, a3 is forced
to match with b1. That gives us one perfect matching.

Conversly, suppose a0 is matched with b1. Then a3 can only match with b2. So a1
is forced to match with b3. Next, a4 is forced to match with b4, and finally,
a2 is forced to match with b0. Therefore, there are only two perfect matchings
--- this one and the one in the previous paragraph.
'''
def question_5():
    # Try all the answers
    pass


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    question_2_1()
    print()
    print('Question 3:')
    question_3()
    print()
    print('Question 4:')
    question_4()
    print()
    print('Question 5:')
    question_5()
    print()

if __name__ == '__main__':
    main()
