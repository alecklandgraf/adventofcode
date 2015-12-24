"""for http://adventofcode.com/day/6"""
import numpy as np
from collections import namedtuple


ParsedLine = namedtuple('ParsedLine', ['op', 'start', 'end'])


def read_data():
    """reads and loads the contents of day6.txt, returns a list of the contents"""
    with open('day6.txt', 'r') as f:
        data = f.readlines()
    data = [line.strip() for line in data]
    return data


def generate_grid():
    """generates a fresh light grid with all the lights off"""
    return np.zeros((1000, 1000), dtype=np.bool)


def count_lights(grid):
    """returns the number of lights on in the grid"""
    return int(np.sum(grid))


def turn_on(grid, start, end):
    """returns grid with lights turned on from start tuple (x1, y1) to end tuple (x2, y2)"""
    start_x, start_y = start
    end_x, end_y = end
    grid[start_x:end_x + 1, start_y:end_y + 1] = True
    return grid


def turn_off(grid, start, end):
    """returns grid with lights turned off from start tuple (x1, y1) to end tuple (x2, y2)"""
    start_x, start_y = start
    end_x, end_y = end
    grid[start_x:end_x + 1, start_y:end_y + 1] = False
    return grid


def toggle(grid, start, end):
    """returns a grid of the with the cells toggled from start tuple (x1, y1) to end tuple (x2, y2)"""
    start_x, start_y = start
    end_x, end_y = end
    grid[start_x:end_x + 1, start_y:end_y + 1] = np.invert(grid[start_x:end_x + 1, start_y:end_y + 1])
    return grid


def parse_line(line):
    """parses a line of day6.txt in the format:
    "turn off 339,840 through 341,844"
    "toggle 918,857 through 944,886"
    "turn on 68,419 through 86,426"

    Usage:
        >>> parse_line("turn on 68,419 through 86,426")
        ParsedLine(op='turn on', start=(68, 419), end=(86, 426))

    returns: namedtuple(op='toggle|turn on|turn off', start=start_tuple, end=end_tuple)
    """
    # could get fancier with this, e.g. for op in ('toggle', 'turn on', 'turn off'): if ...
    if 'toggle' in line:
        op = 'toggle'
    if 'turn on' in line:
        op = 'turn on'
    if 'turn off' in line:
        op = 'turn off'

    parsed_line = ParsedLine(op, (0,0), (0,0))

    return parsed_line
