package jw.problems.adventofcode.aoc2017;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/11
 *
 * --- Day 11: Hex Ed ---
 *
 * Crossing the bridge, you've barely reached the other side of the stream when a program comes up to you, clearly in distress. "It's my child process," she says, "he's gotten lost in an infinite grid!"
 *
 * Fortunately for her, you have plenty of experience with infinite grids.
 *
 * Unfortunately for you, it's a hex grid.
 *
 * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be found to the north, northeast, southeast, south, southwest, and northwest:
 *
 * \ n  /
 * nw +--+ ne
 * /    \
 * -+      +-
 * \    /
 * sw +--+ se
 * / s  \
 *
 * You have the path the child process took. Starting where he started, you need to determine the fewest number of steps required to reach him. (A "step" means to move from the hex you are in to any adjacent hex.)
 *
 * For example:
 *
 * ne,ne,ne is 3 steps away.
 * ne,ne,sw,sw is 0 steps away (back where you started).
 * ne,ne,s,s is 2 steps away (se,se).
 * se,sw,se,sw,sw is 3 steps away (s,s,sw).
 *
 * Your puzzle answer was 715.
 * --- Part Two ---
 *
 * How many steps away is the furthest he ever got from his starting position?
 *
 * Your puzzle answer was 1512.
 */
public class Day11 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in11"));
        String line = sc.nextLine();
        Point p = new Point();
        int max = Integer.MIN_VALUE;
        for (String dir : line.split(",")) {
            if (dir.equals("se")) {
                p.y++;
                p.x++;
            } else if (dir.equals("sw")) {
                p.y++;
                p.x--;
            } else if (dir.equals("s")) {
                p.y += 2;
            } else if (dir.equals("ne")) {
                p.y--;
                p.x++;
            } else if (dir.equals("nw")) {
                p.y--;
                p.x--;
            } else if (dir.equals("n")) {
                p.y -= 2;
            }
            int d = getHexDist(p);
            if (d > max) {
                max = d;
            }
        }
        sc.close();
        System.out.println(p);
        System.out.println(getHexDist(p));
        System.out.println(max);
    }

    public static int getHexDist(Point p) {
        int y = Math.abs(p.y);
        int x = Math.abs(p.x);
        int dist = 0;
        if (y >= x) {
            dist += x;
            dist += (y - x) / 2;
        } else {
            dist = x;
        }
        return dist;
    }
}