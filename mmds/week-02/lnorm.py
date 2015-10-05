from scipy.spatial import distance

def print_distances(x):
    a = (0, 0)
    b = (100, 40)

    print('({}, {}):'.format(x[0], x[1]))
    print('L1norm(0, 0): {0}'.format(round(distance.cityblock(x, a), 5)))
    print('L1norm(100, 40): {0}'.format(round(distance.cityblock(x, b), 5)))
    print('L2norm(0, 0): {0}'.format(round(distance.euclidean(x, a), 5)))
    print('L2norm(100, 40): {0}'.format(round(distance.euclidean(x, b), 5)))
    print('')


# Print results
print('Question 6')
x = (55, 5)
print_distances(x)

x = (59, 10)
print_distances(x)

x = (56, 15)
print_distances(x)

x = (50, 18)
print_distances(x)
