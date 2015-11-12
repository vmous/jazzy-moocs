# -*- coding: utf-8 -*-


'''
Question 1

Suppose we hash the elements of a set S having 20 members, to a bit array of
length 99. The array is initially all-0's, and we set a bit to 1 whenever a
member of S hashes to it. The hash function is random and uniform in its
distribution. What is the expected fraction of 0's in the array after hashing?
What is the expected fraction of 1's? You may assume that 99 is large enough
that asymptotic limits are reached. 

Question Explanation

The probability that a given bit is set to 1 is 1-e^(-20/99) (assuming that
(1-1/99)^99 is exactly 1/e). This formula is derived in the assoc-rules1.ppt
lides. Thus, the probability that the bit remains 0 is 1 minus this expression,
or e^(-20/99).
'''
def question_1():
    #Go read 4.3.3 of the book
    #Probability of any one element not hasing to a particular bit is 1 - 1/99
    #Probabilty that 20 element not hashing to a bit (1-1/99)^20 = ( 1 - 1/99 ) ^ (99*20/99) = e ^ -20/99 (Assume 99 is large enough)
    #P of 20 element hashing to a bit = 1 - e ^ -20/99
    pass


'''
Question 2

A certain Web mail service (like gmail, e.g.) has 108 users, and wishes to
create a sample of data about these users, occupying 1010 bytes. Activity at the
service can be viewed as a stream of elements, each of which is an email. The
element contains the ID of the sender, which must be one of the 108 users of the
service, and other information, e.g., the recipient(s), and contents of the
message. The plan is to pick a subset of the users and collect in the 1010 bytes
records of length 100 bytes about every email sent by the users in the selected
set (and nothing about other users).

The method of Section 4.2.4 will be used. User ID's will be hashed to a bucket
number, from 0 to 999,999. At all times, there will be a threshold t such that
the 100-byte records for all the users whose ID's hash to t or less will be
retained, and other users' records will not be retained. You may assume that
each user generates emails at exactly the same rate as other users. As a
function of n, the number of emails in the stream so far, what should the
threshold t be in order that the selected records will not exceed the 1010 bytes
available to store records? From the list below, identify the true statement
about a value of n and its value of t.

Question Explanation

Suppose that the fraction of users in the sample is p. That is, 108p is the
number of users whose records are stored. Since each user generates 10-8 of the
emails in the stream, when n emails have been seen, the number of records stored
is 108p10-8n = pn. Note that is number does not depend on the number of users
of the service.

Since each record is 100 bytes, we can store 1010/100 = 108 records. That is,
pn = 108, or p = 108/n. If the threshold is t, the fraction p of users that will
be in the selected set is (t+1)/1,000,000. That is,
(t+1)/1,000,000 = 108/n, or t = 1014/n - 1.
'''
def question_2():
    # (t+1)/10^6 fraction of users selected
    # (t+1)/10^6 * 10^8 users will be selected
    # n/10^8 number of email in the stream for each users
    # n/10^8 * 100 number of email space consumed for each users
    # ((t+1)/10^6 * 10^8) * (n/10^8*10) must be less than 10^10

    def space(n,t):
        s = (t+1.0)/(pow(10,6)) * pow(10,8) * n / pow(10,8) * 100
        print(s/pow(10,10))
    
    space(pow(10,10),100000)
    space(pow(10,14),1)
    space(pow(10,12),999)
    space(pow(10,13),9)


def main():
    print('Question 1:')
    question_1()
    print()
    print('Question 2:')
    question_2()
    print()

if __name__ == '__main__':
    main()
