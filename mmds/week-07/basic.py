# -*- coding: utf-8 -*-


import numpy
import sys
sys.path.append('../page_rank')
from page_rank import page_rank


'''
Question 1

Compute the Topic-Specific PageRank for the following link topology. Assume that
pages selected for the teleport set are nodes 1 and 2 and that in the teleport
set, the weight assigned for node 1 is twice that of node 2. Assume further that
the teleport probability, (1 - beta), is 0.3. Which of the following statements
is correct?

https://d396qusza40orc.cloudfront.net/mmds/images/otc_pagerank4.gif
'''
def question_1():
    b = 0.7
    M = numpy.matrix([
        [0, 1, 0, 0],
        [1/2, 0, 0, 0],
        [1/2, 0, 0, 1],
        [0, 0, 1, 0]
    ])
    r = numpy.matrix([[1/4, 1/4, 1/4, 1/4]]).T
    S = numpy.matrix([[0.2, 0.1, 0, 0]]).T
    e = 1 / 10000

    r = page_rank(b, M, r, S, e)

    print(r)
    

'''
Question 2

The spam-farm architecture described in Section 5.4.1 suffers from the problem
that the target page has many links --- one to each supporting page. To avoid
that problem, the spammer could use the architecture shown below:

https://d396qusza40orc.cloudfront.net/mmds/images/otc_spamfarm1.gif

There, k "second-tier" nodes act as intermediaries. The target page t has only
to link to the k second-tier pages, and each of those pages links to m/k of the
m supporting pages. Each of the supporting pages links only to t (although most
of these links are not shown). Suppose the taxation parameter is β = 0.85, and
x is the amount of PageRank supplied from outside to the target page. Let n be
the total number of pages in the Web. Finally, let y be the PageRank of target
page t. If we compute the formula for y in terms of k, m, and n, we get a
formula with the form

y = ax + bm/n + ck/n

Note: To arrive at this form, it is necessary at the last step to drop a
low-order term that is a fraction of 1/n. Determine coefficients a, b, and c,
remembering that β is fixed at 0.85. Then, identify the value, correct to two
decimal places, for one of these coefficients.
'''
def question_2():
    # y: PageRank of target page
    # w: PageRank of each of the second-tier pages
    # z: PageRank of each of the supporting pages
    # then
    # y: PageRank of target page t
    #      (i) x (contribution from outside), and
    #     (ii) untaxed PageRank contribution z of the m supporting pages.
    #    (iii) its share of the tax
    #    y = x + βzm + (1 - β)/n                           (1)
    #
    # w: PageRank of each of the k second-tier pages
    #     (i) 1/k of untaxed PageRank of t
    #    (ii) its share of the tax
    #    w = βy1/k + (1 - β)/n                             (2)
    #
    # z: PageRank of each of the m supporting pages
    #     (i) 1/(m/k) = k/m of untaxed PageRank of page from the second tier
    #         that reaches the supporting page
    #    (ii) its share of the tax
    #    z = βwk/m + (1 - β)/n                             (3)
    #
    # Substituting (2) in (1) we get:
    #    y = x + β^2kw + β(1-β)m/n + (1-β)/n               (4)
    #
    # Substituting (3) in (4) we get:
    #    y = x + β^3y + β(1-β)m/n + β^2(1-β)k/n + (1-β)/n  (5)
    #
    # In (5), neglecting the last term (1-β)/n, per the directions in the
    # question, moving the term β^3y to the left and noting that
    # 1-β^3 = (1-β)(1+β+β^2), we get:
    #    y = (1/(1-β^3))x + (β/(1+β+β^2))(m/n) + (β^2/(1+β+β^2))(k/n)
    b = 0.85
    print('a = {:.2f}, b = {:.2f}, c = {:.2f}'.format(1/(1-b**3), b/(1+b+b**2), b**2/(1+b+b**2)))

def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()


if __name__ == '__main__':
    main()
