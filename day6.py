"""for http://adventofcode.com/day/6"""
import numpy as np


def generate_grid():
    """generates a fresh light grid with all the lights off"""
    return np.zeros((1000, 1000))


def count_lights(grid):
    """returns the number of lights on in the grid"""
    return int(np.sum(grid))


def turn_on(grid, start, end):
    """returns grid with lights turned on from start tuple (x1, y1) to end tuple (x2, y2)"""
    start_x, start_y = start
    end_x, end_y = end
    grid[start_x:end_x, start_y:end_y] = 1
    return grid


def turn_off(grid, start, end):
    """returns grid with lights turned off from start tuple (x1, y1) to end tuple (x2, y2)"""
    start_x, start_y = start
    end_x, end_y = end
    grid[start_x:end_x, start_y:end_y] = 0
    return grid
