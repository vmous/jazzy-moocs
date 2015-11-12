# -*- coding: utf-8 -*-


import numpy
import scipy.spatial


'''
Question 1

The figure below shows two positive points (purple squares) and two negative
points (green circles):

https://d396qusza40orc.cloudfront.net/mmds/images/otc_svm1.gif

That is, the training data set consists of:

(x1,y1) = ((5,4),+1)
(x2,y2) = ((8,3),+1)
(x3,y3) = ((7,2),-1)
(x4,y4) = ((3,3),-1)

Our goal is to find the maximum-margin linear classifier for this data. In easy
cases, the shortest line between a positive and negative point has a
perpendicular bisector that separates the points. If so, the perpendicular
bisector is surely the maximum-margin separator. Alas, in this case, the closest
pair of positive and negative points, x2 and x3, have a perpendicular bisector
that misclassifies x1 as negative, so that won't work.

The next-best possibility is that we can find a pair of points on one side
(i.e., either two positive or two negative points) such that a line parallel to
the line through these points is the maximum-margin separator. In these cases,
the limit to how far from the two points the parallel line can get is determined
by the closest (to the line between the two points) of the points on the other
side. For our simple data set, this situation holds.

Consider all possibilities for boundaries of this type, and express the boundary
as w.x+b=0, such that w.x+b≥1 for positive points x and w.x+b≤-1 for negative
points x. Assuming that w = (w1,w2), identify in the list below the true
statement about one of w1, w2, and b.

Question Explanation

In what follows, assume that v is the vertical dimension and u the horizontal
dimension in the diagram. There are only two possibilities: a parallel to either
the line through x1 and x2, or a parallel to the line through x3 and x4.

The line through x1 and x2 can be written as v=17/3-u/3. If we move this line
down (i.e., lower the constant 17/3) until it meets x3 or x4, we find it meets
x3 first, and the parallel line through x3 is v=13/3-u/3. Thus, the parallel
line between these two lines is v=5-u/3.

We must put this in the form w.x+b=0. That is, (1/3,1).(u,v)+(-5)=0. we must
scale the vector w=(1/3,1) and constant b=(-5) so that if (u,v) is x1 or x2, the
value of the left side is exactly +1, and if (u,v) is x3, the value is
exactly -1. Since the vertical distance between the boundary and the parallel
lines through x1 and x2 on one hand, and through x3 on the other, is 2/3, the
scaling factor must be 3/2. That is, w=(1/2,3/2) and b=(-15/2). You can check
that for these values, w.x+b is +1 when x=x1 or x=x2, and it is -1 when x=x3.
The value is less than -1 when x=x4, as must be the case.

We must do a similar analysis starting with the line through x3 and x4, which is
v=15/4-u/4. Of the positive points, x2 is the closer to this line, and the
parallel line through x2 is v=5-u/4. The midpoint of these lines is v=35/8-u/4.
When we put this boundary in the w.x+b form, and scale by 8/5, we are left with
the line (2/5,8/5).(u,v)+(-7)=0; i.e., w=(2/5,8/5) and b=(-7).

One of these lines is the maximum-margin separator, but which? It is the one
with the smaller length of w. The length of the vector (1/2,3/2) is
sqrt((1/2)2+(3/2)2) = sqrt(5/2). The length of the vector (2/5,8/5) is
sqrt((2/5)2+(8/5)2) = sqrt(68/25). Since 5/2<68/25, the former is the smaller.
That tells us the maximum-margin separator is v=5-u/3, or in vector form:
(1/2,3/2).x-(15/2)=0.
'''
def question_1():
    # Let w.x + b = 0 where w = (w1,w2)
    # Obtain the solutions by solving a linear system with the following
    # three equationa with three unknowns
    # 5w1 + 4w2 + b = 1
    # 8w1 + 3w2 + b = 1
    # 7w1 + 2w2 + b = -1

    A = numpy.matrix([
        [5, 4, 1],
        [8, 3, 1],
        [7, 2, 1],
    ])

    b = numpy.matrix([
        [1, 1, -1]
    ]).T

    print('The solutions of the system is:')
    print(numpy.linalg.solve(A, b))


'''
Question 2

Consider the following training set of 16 points. The eight purple squares are
positive examples, and the eight green circles are negative examples.

https://d396qusza40orc.cloudfront.net/mmds/images/newsvm4.gif

                  [5,10]   [7,10]

[1,8]    [3,8]    (5,8)    [7,8]

[1,6]    [3,6]    (5,6)    (7,6)

(1,4)    [3,4]    (5,4)    (7,4)

(1,2)    (3,2)

We propose to use the diagonal line with slope +1 and intercept +2 as a decision
boundary, with positive examples above and negative examples below. However,
like any linear boundary for this training set, some examples are misclassified.
We can measure the goodness of the boundary by computing all the slack variables
that exceed 0, and then using them in one of several objective functions. In
this problem, we shall only concern ourselves with computing the slack
variables, not an objective function.

To be specific, suppose the boundary is written in the form w.x+b=0, where
w = (-1,1) and b = -2. Note that we can scale the three numbers involved as we
wish, and so doing changes the margin around the boundary. However, we want to
consider this specific boundary and margin.

Determine the slack for each of the 16 points. Then, identify the correct
statement in the list below.

Question Explanation

There are four misclassified points: (1,4), (3,4), (5,8), and (7,8). To find the
slack for a negative point, we write the requirement that w.x+b≤-1 as
w.x+b = -1+ξ. For the point (1,4), we have (-1,1).(1,4)-2 = -1+4-2 = 1 = -1+ξ,
from which it follows that ξ=2. A similar calculation gives the same result for
the other negative misclassified point, (5,8).

To find the slack for a positive point, we write the requirement that w.x+b≥1 as
w.x+b = 1-ξ. For the point (3,4), we have (-1,1).(3,4)-2 = -3+4-2 = -1 = 1-ξ,
from which it follows that ξ=2. A similar calculation gives the same result for
the other positive misclassified point, (7,8).

It is also possible that a correctly classified point has a slack greater
than 0. However, in this case, the correctly classified points that are closest
to the boundary, such as (1,2), are right on the margin, and so their slack
is 0. For example, (-1,1).(1,2)-2 = -1+2-2 = -1, so no slack is necessary. 
'''
def question_2():
    X = numpy.matrix([
        [5, 10],
        [7, 10],
        [1, 8],
        [3, 8],
        [5, 8],
        [7, 8],
        [1, 6],
        [3, 6],
        [5, 6],
        [7, 6],
        [1, 4],
        [3, 4],
        [5, 4],
        [7, 4],
        [1, 2],
        [3, 2]
    ])

    y = numpy.matrix([
        [1],
        [1],
        [1],
        [1],
        [-1],
        [1],
        [1],
        [1],
        [-1],
        [-1],
        [-1],
        [1],
        [-1],
        [-1],
        [-1],
        [-1]
    ])

    w = numpy.matrix([
        [-1],
        [1]
    ])

    b = -2

    e = X * w + b

    # Slack is then:
    # iff y = +1: 
    # e + 1 iff y = -1
    print(numpy.concatenate((y, X, e), axis=1))


'''
Question 3

Below we see a set of 20 points and a decision tree for classifying the points.

https://d396qusza40orc.cloudfront.net/mmds/images/otc_gold-small.gif

To be precise, the 20 points represent (Age,Salary) pairs of people who do or do
not buy gold jewelry. Age (appreviated A in the decision tree) is the x-axis,
and Salary (S in the tree) is the y-axis. Those that do are represented by gold
points, and those that do not by green points. The 10 points of gold-jewelry
buyers are:

(28,145), (38,115), (43,83), (50,130), (50,90), (50,60), (50,30), (55,118),
(63,88), and (65,140).

The 10 points of those that do not buy gold jewelry are:

(23,40), (25,125), (29,97), (33,22), (35,63), (42,57), (44, 105), (55,63),
(55,20), and (64,37).

Some of these points are correctly classified by the decision tree and some are
not. Determine the classification of each point, and then indicate in the list
below the point that is misclassified.

Question Explanation

The tree classifies as a non-buyer those points with Age
The rest of the space is classified as buyers. There is, however, a non-buyer
at (25,125), which is thus misclassified. However, all sixteen other points
are classified correctly.
'''
def question_3():
    buy = [(28,145), (38,115), (43,83), (50,130), (50,90), (50,60), (50,30), (55,118), (63,88),(65,140)]
    nobuy = [(23,40), (25,125), (29,97), (33,22), (35,63), (42,57), (44, 105), (55,63), (55,20), (64,37)]
    def tree(L):
        for l in L:
            print(l, end='')
            if (l[0] < 45):
                if (l[1] < 110):
                    print(': No Buy')
                else:
                    print(': Buy')
            else:
                if (l[1] < 75 ):
                    print(': No Buy')
                else:
                    print(': Buy')

    print('= Buys =')
    tree(buy)
    print()
    print('= Does not buy =')
    tree(nobuy)


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


if __name__ == '__main__':
    main()
