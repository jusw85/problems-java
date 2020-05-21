package jw.problems.adventofcode.aoc2017;

import java.awt.Point;

/**
 * http://adventofcode.com/2017/day/3
 *
 * --- Day 3: Spiral Memory ---
 *
 * You come across an experimental new kind of memory stored on an infinite two-dimensional grid.
 *
 * Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then counting up while spiraling outward. For example, the first few squares are allocated like this:
 *
 * 17  16  15  14  13
 * 18   5   4   3  12
 * 19   6   1   2  11
 * 20   7   8   9  10
 * 21  22  23---> ...
 *
 * While this is very space-efficient (no squares are skipped), requested data must be carried back to square 1 (the location of the only access port for this memory system) by programs that can only move up, down, left, or right. They always take the shortest path: the Manhattan Distance between the location of the data and square 1.
 *
 * For example:
 *
 * Data from square 1 is carried 0 steps, since it's at the access port.
 * Data from square 12 is carried 3 steps, such as: down, left, left.
 * Data from square 23 is carried only 2 steps: up twice.
 * Data from square 1024 must be carried 31 steps.
 *
 * How many steps are required to carry the data from the square identified in your puzzle input all the way to the access port?
 *
 * Your puzzle answer was 480.
 * --- Part Two ---
 *
 * As a stress test on the system, the programs here clear the grid and then store the value 1 in square 1. Then, in the same allocation order as shown above, they store the sum of the values in all adjacent squares, including diagonals.
 *
 * So, the first few squares' values are chosen as follows:
 *
 * Square 1 starts with the value 1.
 * Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
 * Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
 * Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4.
 * Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
 *
 * Once a square is written, its value does not change. Therefore, the first few squares would receive the following values:
 *
 * 147  142  133  122   59
 * 304    5    4    2   57
 * 330   10    1    1   54
 * 351   11   23   25   26
 * 362  747  806--->   ...
 *
 * What is the first value written that is larger than your puzzle input?
 *
 * Your puzzle answer was 349975.
 */
public class Day3 {

    public static void main(String[] args) {
        int i = 347991;
        System.out.println(getDist(i));
        fill();
    }

    public static int getDist(int in) {
        in -= 2;
        if (in < 0) {
            return 0;
        }
        int i = 0;
        while (in >= (4 * i * (i + 1))) {
            i++;
        }
        int startIdx = 4 * i * (i - 1);
        int len = i * 2;
        int subIdx = (in - startIdx) % len;

        if (subIdx < i) {
            return ((2 * i) - 1) - subIdx;
        } else {
            return (subIdx + 1);
        }
    }

    public static void fill() {
        int len = 9;
        int[][] m = new int[len][len];
        int y = len / 2;
        int x = len / 2;
        m[y][x] = 1;

        Point curr = new Point(x, y);
        while (curr != null) {
            fillCell(m, curr.y, curr.x);
            curr = nextCell(m, curr.y, curr.x);
        }

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + ",");
            }
            System.out.println();
        }
    }

    public static Point nextCell(int[][] m, int y, int x) {
        int len = m.length;
        Point origin = new Point(len / 2, len / 2);
        if (y == x && y >= origin.y) {
            if (x == len - 1) {
                return null;
            }
            return new Point(x + 1, y);
        }
        if (x > 0 && m[y][x - 1] > 0 && m[y - 1][x] <= 0) {
            return new Point(x, y - 1);
        } else if (y < len - 1 && m[y + 1][x] > 0 && m[y][x - 1] <= 0) {
            return new Point(x - 1, y);
        } else if (x < len - 1 && m[y][x + 1] > 0 && m[y + 1][x] <= 0) {
            return new Point(x, y + 1);
        } else if (y > 0 && m[y - 1][x] > 0 && m[y][x + 1] <= 0) {
            return new Point(x + 1, y);
        }
        return null;
    }

    public static void fillCell(int[][] m, int y, int x) {
        if (m[y][x] > 0) {
            return;
        }
        int c = 0;
        boolean y0 = y > 0;
        boolean x0 = x > 0;
        boolean yl = y < m.length - 1;
        boolean xl = x < m[y].length - 1;
        if (y0 && x0)
            c += m[y - 1][x - 1];
        if (y0)
            c += m[y - 1][x];
        if (y0 && xl)
            c += m[y - 1][x + 1];
        if (x0)
            c += m[y][x - 1];
        if (xl)
            c += m[y][x + 1];
        if (yl && x0)
            c += m[y + 1][x - 1];
        if (yl)
            c += m[y + 1][x];
        if (yl && xl)
            c += m[y + 1][x + 1];
        m[y][x] = c;
    }
}