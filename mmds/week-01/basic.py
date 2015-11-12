# -*- coding: utf-8 -*-


from math import ceil, sqrt
import numpy
import sys
sys.path.append('../page_rank')
from page_rank import page_rank, power_iteration


'''
Question 1

Consider three Web pages with the following links:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_pagerank2.gif

Suppose we compute PageRank with a β of 0.7, and we introduce the additional
constraint that the sum of the PageRanks of the three pages must be 3, to handle
the problem that otherwise any multiple of a solution will also be a solution.
Compute the PageRanks a, b, and c of the three pages A, B, and C, respectively.
Then, identify from the list below, the true statement.

Answer:
The rules for computing the next value of a, b, or c as we iterate are:

a <- .3
b <- .7(a/2) + .3
c <- .7(a/2+b+c) + .3

The reason is that a splits its PageRank between b and c, while b gives all of
its to c, and c keeps all its own. However, all PageRank is multiplied by .7
before distribution (the "tax"), and .3 is then added to each new PageRank.

In the limit, the assignments become equalities. That immediately tells us
a = .3. We can then use the second equation to discover b = .7*.3/2 + .3 = .405.
Finally, the third equation simplifies to c = .7(.555 + c) + .3, or .3c = .6885.
From this equation we get c = 2.295. It is now a simple matter to compute the
subs of each two of the variables: a+b = .705, a+c = 2.595, and b+c = 2.7.
'''
def question_1():
    b = 0.7
    M = numpy.matrix([[0, 0, 0], [1/2, 0, 0], [1/2, 1, 1]])
    r = numpy.matrix([1/3, 1/3, 1/3]).T
    S = numpy.matrix([(1 - b)/3, (1 - b)/3, (1 - b)/3]).T
    e = 1 / 10000

    r = page_rank(b, M, r, S, e)

    r = 3 * r

    a = r.flat[0]
    b = r.flat[1]
    c = r.flat[2]
    print('a + c = 2.035: {0}'.format(round(a + c, 3) == 2.035))
    print('b + c = 2.5: {0}'.format(round(b + c, 3) == 2.5))
    print('b + c = 2.7: {0}'.format(round(b + c, 3) == 2.7))
    print('a + b = 0.55:  {0}'.format(round(a + b, 3) == 0.55))


'''
Question 2

Consider three Web pages with the following links:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_pagerank3.gif

Suppose we compute PageRank with β=0.85. Write the equations for the PageRanks
a, b, and c of the three pages A, B, and C, respectively. Then, identify in the
list below, one of the equations.

Answer:
Here are the equations in the general form, where the PageRank of a node is set
equal to β times the fair share of the PageRank of each predecessor of that
node, plus (1-β) divided by the number of nodes (3), times the sum of the
PageRanks.

a = .85c + .05a + .05b + 0.05c
b = .425a + .05a + .05b + 0.05c
c = .85b + .425a + .05a + .05b + 0.05c

If we simplify so there is only one term for each variable, we get:

.95a = .9c + .05b
.95b = .475a + .05c
.95c = .9b + .475a 
'''
def question_2():
    b = 0.85
    M = numpy.matrix([[0, 0, 1], [1/2, 0, 0], [1/2, 1, 0]])
    r = numpy.matrix([1/3, 1/3, 1/3]).T
    S = numpy.matrix([(1 - b)/3, (1 - b)/3, (1 - b)/3]).T
    e = 1 / 10000

    r = page_rank(b, M, r, S, e)

    a = r.flat[0]
    b = r.flat[1]
    c = r.flat[2]
    print('c = .9b + .475a: {0}'.format(round(c, 3) ==  round(0.9 * b + 0.475 * a, 3)))
    print('.95c = .9b + .475a: {0}'.format(round(0.95 * c, 3) == round(0.9 * b + 0.475 * a,3)))
    print('a = c + .15b: {0}'.format(round(a, 3) == round(c + 0.15 * b, 3)))
    print('.85a = c + .15b: {0}'.format(round(0.85 * a, 3) == round(c + 0.15 * b, 3)))


'''
Question 3

Consider three Web pages with the following links:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_pagerank3.gif

Assuming no "taxation," compute the PageRanks a, b, and c of the three pages
A, B, and C, using iteration, starting with the "0th" iteration where all three
pages have rank a = b = c = 1. Compute as far as the 5th iteration, and also
determine what the PageRanks are in the limit. Then, identify the true statement
from the list below.

Answer:
The rules for how PageRank passes around the graph can be expressed as:

a <- c
b <- a/2
c <- a/2 + b

Here is the table of the effect of applying these rules for 5 interations:

Iteration 0   1   2   3   4   5
a         1   1   3/2 1   5/4 5/4
b         1   1/2 1/2 3/4 1/2 5/8
c         1   3/2 1   5/4 5/4 9/8

The above table gives you the values for the 4th and 5th iterations, which are
some of the values asked for. The other options involve the values in the limit.
For this simple graph, the solution, where the arrow is replaced by equality in
the rules, is easy to calculate. The first rule tells us a=c, and the second
rule says b is half a (and therefore half c). That is, a:b:c is 2:1:2. Since the
sum of the three values is 3, it must be that a = c = 6/5, and b = 3/5.
'''
def question_3():
    b = 1
    M = numpy.matrix([[0, 0, 1], [1/2, 0, 0], [1/2, 1, 0]])
    r = numpy.matrix([1, 1, 1]).T
    S = numpy.matrix([0, 0, 0]).T
    e = 1 / 10000

    iteration = 1
    # Execute power interation until the Pagerank is processed
    while True:
        r, converged = power_iteration(b, M, r, S, e)
        if iteration == 5:
            a_5 = round(r.flat[0], 3)
            b_5 = round(r.flat[1], 3)
            c_5 = round(r.flat[2], 3)
        if converged:
            a = round(r.flat[0], 3)
            b = round(r.flat[1], 3)
            c = round(r.flat[2], 3)
            break
        iteration = iteration + 1

    print('Pagerank at 5th iteration: a={0}, b={1}, c={2}'.format(a_5, b_5, c_5))
    print('Pagerank at limit: a={0}, b={1}, c={2}'.format(a, b, c))
    print('After iteration 5, c = 9/8: {0}'.format(c_5 == 9 / 8))
    print('After iteration 5, c = 7/4: {0}'.format(c_5 == 7/4))
    print('After iteration 5, a = 21/16: {0}'.format(a_5 == 21/16))
    print('In the limit, b = 1/2: {0}'.format(b == 1/3))


'''
Question 4

Suppose our input data to a map-reduce operation consists of integer values
(the keys are not important). The map function takes an integer i and produces
the list of pairs (p,i) such that p is a prime divisor of i. For example,
map(12) = [(2,12), (3,12)].

The reduce function is addition. That is, reduce(p, [i1, i2, ...,ik])
is (p,i1+i2+...+ik).

Compute the output, if the input is the set of integers 15, 21, 24, 30, 49.
Then, identify, in the list below, one of the pairs in the output.

Answer:
Map does the following:
15 -> (3,15), (5,15) 21 -> (3,21), (7,21) 24 -> (2,24), (3,24) 30 -> (2.30), (3,30), (5,30) 49 -> (7.49)
We then group by keys, giving: (2, [24, 30]) (3, [15, 21, 24, 30]) (5, [15, 30]) (7, [21, 49]) Finally, we add the elements of each list, giving the result (2,54), (3,90), (5,45), (7,70). 
'''
def question_4():
    # Get the prime divisors of a number.
    # This function come from http://codereview.stackexchange.com/questions/19509/functional-prime-factor-generator
    def factor(n):
        if n <= 1: return []
        prime = next((x for x in range(2, ceil(sqrt(n))+1) if n%x == 0), n)
        result = [prime] + factor(n//prime)
        # This algorithm return duplicated values. Let's return an array of unique values
        result = list(set(result))
        return result

    # Set the list of integer to process
    integers = [15, 21, 24, 30, 49]

    # Store pairs
    pairs = []

    # Map function creating every pairs
    def map(n):
        factors = factor(n)
        map_keys = []
        for num in factors:
            map_keys.append((num, n))
        return map_keys

    # Reduce function summing the values of every keys
    def reduce(pairs):
        result = dict()
        last_key = None
        for pair in pairs:
            key = pair[0]
            if key == last_key:
                result[key] += pair[1]
            else:
                result[key] = pair[1]
            last_key = key
        return result

    # Create pairs by calling map function for every integers
    for integer in integers:
        tuples = map(integer)
        for single_tuple in tuples:
            pairs.append(single_tuple)

    # Sort pairs by key
    pairs = sorted(pairs, key=lambda key: key[0])

    # Reduce pairs to get the results
    reduced_pairs = reduce(pairs)

    # Print results
    print('(2,47): {0}'.format(reduced_pairs[2] == 47))
    print('(5,49): {0}'.format(reduced_pairs[5] == 49))
    print('(2,102): {0}'.format(reduced_pairs[2] == 102))
    print('(5,45): {0}'.format(reduced_pairs[5] == 45))
    

if __name__ == '__main__':
    print('Question 1')
    question_1()
    print()
    print('Question 2')
    question_2()
    print()
    print('Question 3')
    question_3()
    print()
    print('Question 4')
    question_4()
    print()
