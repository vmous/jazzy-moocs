#Quiz 2B
#Question 1
import math
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

#Quiz2C
#Question1
#No Idea, See discussion forum for expert replies
#1st pass size of bucket pairs + count
#Bitmap of 2nd pass P/32 pairs in bucket 12 bytes per pairs
import math
S = 200000000
P = 1600000000
def qns2c(S,P):
    
    return (1000000 * 12.0) + P / S


print('{}'.format(qns2c(1000000000,20000000000)))
print('{}'.format(qns2c(300000000,750000000)))
print('{}'.format(qns2c(1000000000,10000000000)))
print('{}'.format(qns2c(200000000,400000000)))

print('{}'.format(1800000000/300000000))

# <codecell>

#Question2
#Toivonnen
#Itemset {A,B,C,D,E,F,G,H}
#Frequent Itemset {A,B}, {A,C}, {A,D}, {B,C}, {E}, {F}
#Negative border is not frequent and its subset are
#Just find answer which consist of frequent and not frequent subsets or not frequent subsets
