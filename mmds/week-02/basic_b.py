# -*- coding: utf-8 -*-


'''
Question 1

Suppose we have transactions that satisfy the following assumptions:

 * s, the support threshold, is 10,000.
 * There are one million items, which are represented by the
   integers 0,1,...,999999.
 * There are N frequent items, that is, items that occur 10,000 times or more.
 * There are one million pairs that occur 10,000 times or more.
 * There are 2M pairs that occur exactly once. M of these pairs consist of two
   frequent items, the other M each have at least one nonfrequent item.
 * No other pairs occur at all.
 * Integers are always represented by 4 bytes.

Suppose we run the a-priori algorithm to find frequent pairs and can choose on
the second pass between the triangular-matrix method for counting candidate
pairs (a triangular array count[i][j] that holds an integer count for each pair
of items (i, j) where i < j) and a hash table of item-item-count triples.
Neglect in the first case the space needed to translate between original item
numbers and numbers for the frequent items, and in the second case neglect the
space needed for the hash table. Assume that item numbers and counts are always
4-byte integers.
As a function of N and M, what is the minimum number of bytes of main memory
needed to execute the a-priori algorithm on this data? Demonstrate that you have
the correct formula by selecting, from the choices below, the triple consisting
of values for N, M, and the (approximate, i.e., to within 10%) minumum number of
bytes of main memory, S, needed for the a-priori algorithm to execute with this
data.

Question Explanation

On the first pass, we need 4,000,000 bytes to count the 1,000,000 items. This
number is tiny compared with the amount needed on the second pass in all choices
appearing in this question, so we shall ignore the first pass. On the second
pass, we need 4N bytes to store the ID's of the N frequent items. This amount is
also tiny compared to the space needed to count pairs in all choices for this
question, so we shall neglect it.
If we use a triangular table to store the counts of pairs of frequent items, we
need 4(N choose 2) or about 2N2 bytes. If we use a hash table to count only the
frequent pairs that occur, we need 12 bytes per occurring pair. The number of
pairs that occur is 1,000,000 frequent pairs, plus M pairs that are not
frequent, but consist of two frequent items. Thus, the form of correct answers
will be:
(N, M, min(2N2, 12(1,000,000 + M))
For instance, with N = 100,000 and M = 100,000,000, S is approximately
min(2*100,000*100,000, 12(1,000,000 + 100,000,000)) = min(20,000,000,000, 1,212,000,000)
or approximately 1.2 billion. Note that in this case the hash table is far
better than the triangular array. 
'''
def question_1():
    #Either a triangular matrix (n)(n-1) / 2 or a hash table 2M for 2nd pass
    def qns1(N,M,S):
        triangle = (N*N-1/2)*4
        hashtable = 2 * M * 4
        print('triangle/S : {0}'.format(triangle/float(S)))
        print('hashtable/S: {0}'.format(hashtable/float(S)))
        print('S: {0}'.format(S))
        print('')
 
    qns1(20000,60000000,1000000000)
    qns1(10000,40000000,200000000)
    qns1(10000,50000000,600000000)
    qns1(30000,100000000,500000000)


    #Qns2
    #Item i is in basket j iif j/i
    #i is a mutiple of j
    #100% confidence means support(LHS U RHS) / support(LHS)
    dict = {}
    for j in range(1,100+1):
        list = []
        for i in range(1,j+1):
            if ((j)%(i) == 0):
                list.append(i)
        dict[j] = list

    for k, v in dict.items():
        if k in set([14, 12, 96, 4]):
            print(k, v)


'''
Question 2

Imagine there are 100 baskets, numbered 1,2,...,100, and 100 items, similarly
numbered. Item i is in basket j if and only if i divides j evenly. For example,
basket 24 is the set of items {1,2,3,4,6,8,12,24}. Describe all the association
rules that have 100% confidence. Which of the following rules has
100% confidence?

Question Explanation

In order for the confidence to be 100%, every basket b that contains all the
items on the left must contain the item on the right. Since membership in
baskets is defined by divisibility, what we're really looking for is that
every integer b that is divisible by all the numbers on the left is also
divisible by the number on the right. For example, the rule
{4,6} → 12 has 100% confidence, because if b is divisible by 4 and 6, it has at
least two factors 2 and at least one factor 3. That means it is divisible
by 2*2*3 = 12.  
'''
def question_2():
    pass


'''
Question 3

Suppose ABC is a frequent itemset and BCDE is NOT a frequent itemset. Given this
information, we can be sure that certain other itemsets are frequent and sure
that certain itemsets are NOT frequent. Other itemsets may be either frequent or
not. Which of the following is a correct classification of an itemset?

Question Explanation

All subsets of ABC are frequent and all supersets of BCDE are not frequent. Any
other itemset can be either frequent or not. 
'''
def question_3():
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


if  __name__ == '__main__':
    main()
