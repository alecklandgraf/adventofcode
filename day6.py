"""for http://adventofcode.com/day/6"""
import numpy as np


def generate_grid():
    """generates a fresh light grid with all the lights off"""
    return int(np.zeros((1000,1000)))


def count_lights(grid):
    """returns the number of lights on in the grid"""
    return np.sum(grid)


def turn_on(grid, start, end):
    """returns grid with lights turned on from start tuple (x1, y1) to end tuple (x2, y2)"""

