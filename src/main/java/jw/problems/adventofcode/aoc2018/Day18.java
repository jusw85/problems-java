package jw.problems.adventofcode.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * https://adventofcode.com/2018/day/18
 *
 * --- Day 18: Settlers of The North Pole ---
 *
 * On the outskirts of the North Pole base construction project, many Elves are collecting lumber.
 *
 * The lumber collection area is 50 acres by 50 acres; each acre can be either open ground (.), trees (|), or a lumberyard (#). You take a scan of the area (your puzzle input).
 *
 * Strange magic is at work here: each minute, the landscape looks entirely different. In exactly one minute, an open acre can fill with trees, a wooded acre can be converted to a lumberyard, or a lumberyard can be cleared to open ground (the lumber having been sent to other projects).
 *
 * The change to each acre is based entirely on the contents of that acre as well as the number of open, wooded, or lumberyard acres adjacent to it at the start of each minute. Here, "adjacent" means any of the eight acres surrounding that acre. (Acres on the edges of the lumber collection area might have fewer than eight adjacent acres; the missing acres aren't counted.)
 *
 * In particular:
 *
 * An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
 * An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
 * An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.
 *
 * These changes happen across all acres simultaneously, each of them using the state of all acres at the beginning of the minute and changing to their new form by the end of that same minute. Changes that happen during the minute don't affect each other.
 *
 * For example, suppose the lumber collection area is instead only 10 by 10 acres with this initial configuration:
 *
 * Initial state:
 * .#.#...|#.
 * .....#|##|
 * .|..|...#.
 * ..|#.....#
 * #.#|||#|#|
 * ...#.||...
 * .|....|...
 * ||...#|.#|
 * |.||||..|.
 * ...#.|..|.
 *
 * After 1 minute:
 * .......##.
 * ......|###
 * .|..|...#.
 * ..|#||...#
 * ..##||.|#|
 * ...#||||..
 * ||...|||..
 * |||||.||.|
 * ||||||||||
 * ....||..|.
 *
 * After 2 minutes:
 * .......#..
 * ......|#..
 * .|.|||....
 * ..##|||..#
 * ..###|||#|
 * ...#|||||.
 * |||||||||.
 * ||||||||||
 * ||||||||||
 * .|||||||||
 *
 * After 3 minutes:
 * .......#..
 * ....|||#..
 * .|.||||...
 * ..###|||.#
 * ...##|||#|
 * .||##|||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 *
 * After 4 minutes:
 * .....|.#..
 * ...||||#..
 * .|.#||||..
 * ..###||||#
 * ...###||#|
 * |||##|||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 *
 * After 5 minutes:
 * ....|||#..
 * ...||||#..
 * .|.##||||.
 * ..####|||#
 * .|.###||#|
 * |||###||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 *
 * After 6 minutes:
 * ...||||#..
 * ...||||#..
 * .|.###|||.
 * ..#.##|||#
 * |||#.##|#|
 * |||###||||
 * ||||#|||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 *
 * After 7 minutes:
 * ...||||#..
 * ..||#|##..
 * .|.####||.
 * ||#..##||#
 * ||##.##|#|
 * |||####|||
 * |||###||||
 * ||||||||||
 * ||||||||||
 * ||||||||||
 *
 * After 8 minutes:
 * ..||||##..
 * ..|#####..
 * |||#####|.
 * ||#...##|#
 * ||##..###|
 * ||##.###||
 * |||####|||
 * ||||#|||||
 * ||||||||||
 * ||||||||||
 *
 * After 9 minutes:
 * ..||###...
 * .||#####..
 * ||##...##.
 * ||#....###
 * |##....##|
 * ||##..###|
 * ||######||
 * |||###||||
 * ||||||||||
 * ||||||||||
 *
 * After 10 minutes:
 * .||##.....
 * ||###.....
 * ||##......
 * |##.....##
 * |##.....##
 * |##....##|
 * ||##.####|
 * ||#####|||
 * ||||#|||||
 * ||||||||||
 *
 * After 10 minutes, there are 37 wooded acres and 31 lumberyards. Multiplying the number of wooded acres by the number of lumberyards gives the total resource value after ten minutes: 37 * 31 = 1147.
 *
 * What will the total resource value of the lumber collection area be after 10 minutes?
 *
 * Your puzzle answer was 535522.
 * --- Part Two ---
 *
 * This important natural resource will need to last for at least thousands of years. Are the Elves collecting this lumber sustainably?
 *
 * What will the total resource value of the lumber collection area be after 1000000000 minutes?
 *
 * Your puzzle answer was 210160.
 */
public class Day18 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in18"));
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        char[][] grid = new char[lines.size() + 2][lines.size() + 2];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[i].length - 1) {
                    grid[i][j] = ' ';
                } else {
                    grid[i][j] = lines.get(i - 1).charAt(j - 1);
                }
            }
        }

//        part1(grid);
        part2(grid);
    }

    public static void part2(char[][] grid) {
        List<Integer> resources = new ArrayList<>();
        for (int tick = 0; tick < 10000; tick++) {
            char[][] newgrid = new char[grid.length][grid[0].length];
            copy(grid, newgrid);
            for (int i = 1; i < grid.length - 1; i++) {
                for (int j = 1; j < grid[i].length - 1; j++) {
                    mutate(grid, newgrid, i, j);
                }
            }
            copy(newgrid, grid);

            if (tick >= 3000) {
                int val = getResource(grid);
                if (resources.contains(val)) {
                    int idx = (1000000000 - 3000) % resources.size();
                    System.out.println(resources.get(idx - 1));
                    break;
                }
                resources.add(val);
            }
        }
    }

    public static void part1(char[][] grid) {
        for (int tick = 0; tick < 10; tick++) {
            char[][] newgrid = new char[grid.length][grid[0].length];
            copy(grid, newgrid);
            for (int i = 1; i < grid.length - 1; i++) {
                for (int j = 1; j < grid[i].length - 1; j++) {
                    mutate(grid, newgrid, i, j);
                }
            }
            copy(newgrid, grid);
        }
        System.out.println(getResource(grid));
    }

    public static int getResource(char[][] grid) {
        int numTree = 0;
        int numLumber = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                switch (grid[i][j]) {
                    case '#':
                        numLumber++;
                        break;
                    case '|':
                        numTree++;
                        break;
                }
            }
        }
        return numTree * numLumber;
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

    public static void mutate(char[][] grid, char[][] newgrid, int i, int j) {
        int numTree = 0;
        int numLumber = 0;
        int numOpen = 0;
        for (int[] adjacent : adjacents) {
            switch (grid[i + adjacent[0]][j + adjacent[1]]) {
                case '.':
                    numOpen++;
                    break;
                case '#':
                    numLumber++;
                    break;
                case '|':
                    numTree++;
                    break;
            }
        }
        if (grid[i][j] == '.' && numTree >= 3) {
            newgrid[i][j] = '|';
        } else if (grid[i][j] == '|' && numLumber >= 3) {
            newgrid[i][j] = '#';
        } else if (grid[i][j] == '#' && !(numLumber >= 1 && numTree >= 1)) {
            newgrid[i][j] = '.';
        }
    }

    public static void pprint(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}
