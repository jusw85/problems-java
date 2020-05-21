package jw.problems.adventofcode.aoc2015;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * http://adventofcode.com/2015/day/3
 *
 * --- Day 3: Perfectly Spherical Houses in a Vacuum ---
 *
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 *
 * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.
 *
 * However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?
 *
 * For example:
 *
 * > delivers presents to 2 houses: one at the starting location, and one to the east.
 * ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 * ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 *
 * Your puzzle answer was 2565.
 * --- Part Two ---
 *
 * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.
 *
 * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.
 *
 * This year, how many houses receive at least one present?
 *
 * For example:
 *
 * ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
 * ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
 * ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
 *
 * Your puzzle answer was 2639.
 */
public class Day3 {

    public static void main(String[] args) throws FileNotFoundException {
        part1();
        part2();
    }

    public static void part2() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2015/in3"));
        String line = sc.nextLine();
        sc.close();

        int[][] grid = new int[10000][10000];
        Point[] ps = new Point[2];
        ps[0] = new Point(5000, 5000);
        ps[1] = new Point(5000, 5000);
        grid[5000][5000]++;
        int idx = 0;
        for (char c : line.toCharArray()) {
            switch (c) {
                case '^':
                    ps[idx].y--;
                    break;
                case '<':
                    ps[idx].x--;
                    break;
                case '>':
                    ps[idx].x++;
                    break;
                case 'v':
                    ps[idx].y++;
                    break;
            }
            grid[ps[idx].y][ps[idx].x]++;
            idx = (idx + 1) % 2;
        }

        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    public static void part1() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2015/in3"));
        String line = sc.nextLine();
        sc.close();

        int[][] grid = new int[10000][10000];
        int cy = 5000;
        int cx = 5000;
        grid[cy][cx]++;
        for (char c : line.toCharArray()) {
            switch (c) {
                case '^':
                    cy--;
                    break;
                case '<':
                    cx--;
                    break;
                case '>':
                    cx++;
                    break;
                case 'v':
                    cy++;
                    break;
            }
            grid[cy][cx]++;
        }

        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

}