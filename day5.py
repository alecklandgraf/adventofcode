"""for http://adventofcode.com/day/5

data was loaded by the following:

with open(file_path, 'r') as f:
    data = f.readlines()
data = [s.strip() for s in data]
"""

def has_double_letter(s):
    """True if a letter is repeated in s, e.g. 'aa', 'aab', 'asdasdasdzz'"""
    last_letter = None
    for l in s:
        if l == last_letter:
            return True
        last_letter = l
    return False


def has_three_vowels(s):
    """True if s cotains three or more vowels"""
    count = 0
    for v in 'aeiou':
        count += s.count(v)
    return count >= 3


def has_bad_word(s):
    """True if a bad word is in s"""
    bad = ('ab', 'cd', 'pq', 'xy')
    return any(b in s for b in bad)


def part1(data):
    """Usage: part1(data)"""
    nice_strings = 0
    for s in data:
        if (not has_bad_word(s)) and has_double_letter(s) and has_three_vowels(s):
            nice_strings += 1
    print 'Day 5 Part 1 answer: {}'.format(nice_strings)


def has_pairs(s):
    """True if a two letter pair is repeated in s"""
    for i in range(len(s)):
        t = s[i:i+2]
        if len(t) < 2:
            return False
        if s.find(t, i+2) > -1:
            return True


def has_sandwich(s):
    """True if a letter is repeated with a letter inbetween it in s
    e.g. 'aaa', 'aba', 'efe', 'xux'
    """
    for i, l in enumerate(s):
        if i + 2 == len(s):
            return False
        if s[i+2] == l:
            return True


def part2(data):
    """Usage: part2(data)"""
    nice_strings = 0
    for s in data:
        if has_pairs(s) and has_sandwich(s):
            nice_strings += 1
    print 'Day 5 Part 2 answer: {}'.format(nice_strings)