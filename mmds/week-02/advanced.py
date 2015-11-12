# -*- coding: utf-8 -*-





'''
Question 1

Suppose we perform the PCY algorithm to find frequent pairs, with market-basket
data meeting the following specifications:

 * s, the support threshold, is 10,000.
 * There are one million items, which are represented by the
   integers 0,1,...,999999.
 * There are 250,000 frequent items, that is, items that occur 10,000 times
   or more.
 * There are one million pairs that occur 10,000 times or more.
 * There are P pairs that occur exactly once and consist of 2 frequent items.
 * No other pairs occur at all.
 * Integers are always represented by 4 bytes.
 * When we hash pairs, they distribute among buckets randomly, but as evenly as
   possible; i.e., you may assume that each bucket gets exactly its fair share
   of the P pairs that occur once.

Suppose there are S bytes of main memory. In order to run the PCY algorithm
successfully, the number of buckets must be sufficiently large that most buckets
are not large. In addition, on the second pass, there must be enough room to
count all the candidate pairs. As a function of S, what is the largest value
of P for which we can successfully run the PCY algorithm on this data?
Demonstrate that you have the correct formula by indicating which of the
following is a value for S and a value for P that is approximately
(i.e., to within 10%) the largest possible value of P for that S.

Question Explanation

On the first pass, we need 4,000,000 bytes to count items, and the remaining
space is used for buckets. Since we need 4 bytes/bucket, we can use
S/4 - 1,000,000 buckets. As S is much larger than a million in all choices,
we shall approximate the number of buckets as S/4.

The P infrequent pairs that occur exactly once are expected to distribute evenly
among buckets, so there are 4P/S of these per bucket. In order that buckets
containing only infrequent pairs be infrequent, we need 4P/S < 10,000,
or P < 2500S. This relationship holds easily in all choices, so we shall assume
that only buckets containing one of the 1,000,000 frequent pairs are frequent
buckets. The number of pairs in a frequent bucket is thus 1+4P/S. Since 4P/S is
much larger than 1 in all choices, we shall estimate the number of candidate
pairs as 4,000,000P/S. Note that all these pairs consist of two frequent items,
so none are excluded from counting during the second pass.

On the second pass, we can neglect the space needed to store the frequent items.
PCY requires a hash table of candidate pairs, so we use 12 bytes for each of the
4,000,000P/S candidate pairs. In order for there to be enough space for all
these counts, we need S >= 48,000,000P/S, or P < S2/48,000,000. Since we are
asked for the largest possible P, we equate the two sides. For instance,
if S = 200,000,000, then P = 4*1016/4.8*107, or about P = 833,000,000. 
'''
def question_1():
    #No Idea, See discussion forum for expert replies
    #1st pass size of bucket pairs + count
    #Bitmap of 2nd pass P/32 pairs in bucket 12 bytes per pairs
    S = 200000000
    P = 1600000000
    def qns2c(S,P):
        return (1000000 * 12.0) + P / S

    print('{}'.format(qns2c(1000000000,20000000000)))
    print('{}'.format(qns2c(300000000,750000000)))
    print('{}'.format(qns2c(1000000000,10000000000)))
    print('{}'.format(qns2c(200000000,400000000)))

    print('{}'.format(1800000000/300000000))


'''
Question 2

During a run of Toivonen's Algorithm with set of items
{A,B,C,D,E,F,G,H} a sample is found to have the following maximal frequent
itemsets: {A,B}, {A,C}, {A,D}, {B,C}, {E}, {F}. Compute the negative border.
Then, identify in the list below the set that is NOT in the negative border.
'''
def question_2():
    #Toivonnen
    #Itemset {A,B,C,D,E,F,G,H}
    #Frequent Itemset {A,B}, {A,C}, {A,D}, {B,C}, {E}, {F}
    #Negative border is not frequent and its subset are
    #Just find answer which consist of frequent and not frequent subsets or not frequent subsets
    pass


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()


if  __name__ == '__main__':
    main()
