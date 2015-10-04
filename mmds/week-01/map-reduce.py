from math import ceil, sqrt

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
print('Question 4')
print('(2,47): {0}'.format(reduced_pairs[2] == 47))
print('(5,49): {0}'.format(reduced_pairs[5] == 49))
print('(2,102): {0}'.format(reduced_pairs[2] == 102))
print('(5,45): {0}'.format(reduced_pairs[5] == 45))
