
"""http://adventofcode.com/ day 2 2015
I choose named tuples over a class since it has similar method access for readability and native interop wtih lists.
See ``shortest_two_sides`` where I am able to use ``sorted`` to get the two shortest dimansions of the package.
"""
from collections import namedtuple

Dimensions = namedtuple('Dimensions', ['l', 'w', 'h'])

data = []  # data loaded as tuples, [(22,11,55), (1,2,4), ...]
dd = [Dimensions(*d) for d in data]  # dimensions-data

# part 1
def sqft(d):
  """takes a Dimensions named tuple and returns the amount of wrapping paper needed to wrap"""
    return 2 * (d.l*d.w + d.l*d.h + d.h*d.w)


def extra(d):
  """takes a Dimensions named tuple and returns the extra sqft of wrapping paper"""
    return min(d.l * d.w, d.w * d.h, d.h * d.l)


def total_wrapping_per_package(d):
  """takes a Dimensions named tuple and return the total amount of wrapping paper needed"""
    return sqft(d) + extra(d)


def total_wrapping_for_all_packages(packages):
  """takes an interable of Dimensions and returns the total number of sqft needed to wrap all the presents"""
    return sum(total_wrapping_per_package(d) for d in packages)

# part 2
def shortest_two_sides(d):
    """ returns the shortes two sides.
    Usage:
        a, b = shortest_two_sides((4,3,1))
        # a = 1, b = 3
    """
    return sorted(d)[:2]


def feet_of_ribbon(d):
    """returns the totoal feet of ribbon needed to wrap a present"""
    a, b = shortest_two_sides(d)
    return a+a+b+b + d.l * d.w * d.h


def total_feet_of_ribbon(packages):
    """returns the total feet of ribbon for all packages"""
    return sum(feet_of_ribbon(d) for d in dd)