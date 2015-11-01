# -*- coding: utf-8 -*-

import math
import numpy
import scipy.spatial

'''
Question 1

We wish to cluster the following set of points: (25,125), (44,105), (29,97),
(35,63), (55,63), (42,57), (23,40), (64,37), (33,22), (55,20), (28,145),
(65,140), (50,130), (38,115), (55,118), (50,90), (43,83), (63,88), (50,60),
(50,30) into 10 clusters. We initially choose each of the green points (25,125),
(44,105), (29,97), (35,63), (55,63), (42,57), (23,40), (64,37), (33,22), and
(55,20) as a centroid. Assign each of the gold points to their nearest centroid.
Then, recompute the centroids of each of the clusters. Do any of the points then
get reassigned to a new cluster on the next round? Identify the true statement
in the list below. Each statement refers either to a centroid AFTER
recomputation of centroids (precise to one decimal place) or to a point that
gets reclassified.

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
            min_dist = float("inf")
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
(c,d) = (20,5), and the data truly belongs to two rectangular clusters.
Under what circumstances will the initial clustering be successful? That is,
under what conditions will all the yellow points be assigned to the centroid
(5,10), while all of the blue points are assigned to cluster (20,5))? Identify
in the list below, a pair of rectangles (described by their upper left corner,
UL, and their lower-right corner LR) that are successfully clustered.
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


'''
Question 3

Suppose we apply the BALANCE algorithm with bids of 0 or 1 only, to a situation
where advertiser A bids on query words x and y, while advertiser B bids on query
words x and z. Both have a budget of $2. Identify in the list below a sequence
of four queries that will certainly be handled optimally by the algorithm.
'''
def question_3():
    pass


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

Has several perfect matchings. Find all the perfect matchings and then identify,
in the list below, a pair of edges that can appear together in a perfect
matching.
'''
def question_5():
    pass


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
    print('Question 5:')
    question_5()
    print()

if __name__ == '__main__':
    main()
