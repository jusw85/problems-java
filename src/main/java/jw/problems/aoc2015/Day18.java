package jw.problems.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * http://adventofcode.com/2015/day/18
 *
 * --- Day 18: Like a GIF For Your Yard ---
 *
 * After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed. You arrange them in a 100x100 grid.
 *
 * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few lights, he says, you'll have to resort to animation.
 *
 * Start by setting your lights to the included initial configuration (your puzzle input). A # means "on", and a . means "off".
 *
 * Then, animate your grid in steps, where each step decides the next configuration based on the current one. Each light's next state (either on or off) depends on its current state and the current states of the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight neighbors; the missing ones always count as "off".
 *
 * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light marked B, which is on an edge, only has the neighbors marked 1 through 5:
 *
 * 1B5...
 * 234...
 * ......
 * ..123.
 * ..8A4.
 * ..765.
 *
 * The state a light should have next is based on its current state (on or off) plus the number of neighbors that are on:
 *
 * A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
 * A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
 *
 * All of the lights update simultaneously; they all consider the same current state before moving to the next.
 *
 * Here's a few steps from an example configuration of another 6x6 grid:
 *
 * Initial state:
 * .#.#.#
 * ...##.
 * #....#
 * ..#...
 * #.#..#
 * ####..
 *
 * After 1 step:
 * ..##..
 * ..##.#
 * ...##.
 * ......
 * #.....
 * #.##..
 *
 * After 2 steps:
 * ..###.
 * ......
 * ..###.
 * ......
 * .#....
 * .#....
 *
 * After 3 steps:
 * ...#..
 * ......
 * ...#..
 * ..##..
 * ......
 * ......
 *
 * After 4 steps:
 * ......
 * ......
 * ..##..
 * ..##..
 * ......
 * ......
 *
 * After 4 steps, this example has four lights on.
 *
 * In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?
 *
 * Your puzzle answer was 821.
 * --- Part Two ---
 *
 * You flip the instructions over; Santa goes on to point out that this is all just an implementation of Conway's Game of Life. At least, it was, until you notice that something's wrong with the grid of lights you bought: four lights, one in each corner, are stuck on and can't be turned off. The example above will actually run like this:
 *
 * Initial state:
 * ##.#.#
 * ...##.
 * #....#
 * ..#...
 * #.#..#
 * ####.#
 *
 * After 1 step:
 * #.##.#
 * ####.#
 * ...##.
 * ......
 * #...#.
 * #.####
 *
 * After 2 steps:
 * #..#.#
 * #....#
 * .#.##.
 * ...##.
 * .#..##
 * ##.###
 *
 * After 3 steps:
 * #...##
 * ####.#
 * ..##.#
 * ......
 * ##....
 * ####.#
 *
 * After 4 steps:
 * #.####
 * #....#
 * ...#..
 * .##...
 * #.....
 * #.#..#
 *
 * After 5 steps:
 * ##.###
 * .##..#
 * .##...
 * .##...
 * #.#...
 * ##...#
 *
 * After 5 steps, this example now has 17 lights on.
 *
 * In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on state, how many lights are on after 100 steps?
 *
 * Your puzzle answer was 886.
 */
public class Day18 {

    public static char[][] parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            lines.add(line);
        }
        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    public static char[][] addBorder(char[][] in) {
        char[][] out = new char[in.length + 2][in[0].length + 2];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                if (i == 0 || j == 0 ||
                        i == out.length - 1 || j == out[i].length - 1) {
                    out[i][j] = '~';
                } else {
                    out[i][j] = in[i - 1][j - 1];
                }
            }
        }
        return out;
    }

    public static void pprint(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void copy(char[][] from, char[][] to) {
        for (int i = 0; i < from.length; i++) {
            for (int j = 0; j < from[i].length; j++) {
                to[i][j] = from[i][j];
            }
        }
    }

    public static int[][] adjacents =
            {{-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, {0, 1},
                    {1, -1}, {1, 0}, {1, 1},};

    public static void mutate(char[][] grid, char[][] newgrid, int i, int j, boolean isStuck) {
        if (isStuck && ((i == 1 && j == 1) ||
                (i == 1 && j == grid[i].length - 2) ||
                (i == grid.length - 2 && j == 1) ||
                (i == grid.length - 2 && j == grid[i].length - 2))) {
            newgrid[i][j] = '#';
            return;
        }
        int numOn = 0;
        int numOff = 0;
        for (int[] adjacent : adjacents) {
            switch (grid[i + adjacent[0]][j + adjacent[1]]) {
                case '#':
                    numOn++;
                    break;
                case '.':
                    numOff++;
                    break;
            }
        }

        if (grid[i][j] == '#' && !(numOn == 2 || numOn == 3)) {
            newgrid[i][j] = '.';
        } else if (grid[i][j] == '.' && numOn == 3) {
            newgrid[i][j] = '#';
        }
    }

    public static int countLights(char[][] grid) {
        int num = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '#') {
                    num++;
                }
            }
        }
        return num;
    }

    public static void simulate(char[][] grid, boolean isStuck) {
        for (int tick = 0; tick < 100; tick++) {
            char[][] newgrid = new char[grid.length][grid[0].length];
            copy(grid, newgrid);
            for (int i = 1; i < grid.length - 1; i++) {
                for (int j = 1; j < grid[i].length - 1; j++) {
                    mutate(grid, newgrid, i, j, isStuck);
                }
            }
            copy(newgrid, grid);
        }
        System.out.println(countLights(grid));
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] grid = parseInput("./etc/aoc2015/in18a");
        grid = addBorder(grid);
        simulate(grid, false);

        char[][] grid2 = parseInput("./etc/aoc2015/in18b");
        grid2 = addBorder(grid2);
        simulate(grid2, true);
    }

}