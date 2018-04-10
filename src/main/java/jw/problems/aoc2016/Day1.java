package jw.problems.aoc2016;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * http://adventofcode.com/2016/day/1
 *
 * --- Day 1: No Time for a Taxicab ---
 *
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and face North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination. Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?
 *
 * For example:
 *
 * Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 * R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 * R5, L5, R5, R3 leaves you 12 blocks away.
 *
 * How many blocks away is Easter Bunny HQ?
 *
 * Your puzzle answer was 252.
 * --- Part Two ---
 *
 * Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.
 *
 * How many blocks away is the first location you visit twice?
 *
 * Your puzzle answer was 143.
 */
public class Day1 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in1"));
        String str = sc.nextLine();
        sc.close();

        Point p = new Point(0, 0);
        String faces = "NESW";
        int faceIdx = 0;
        char face;

        Set<Point> visited = new HashSet<>();
        visited.add(p);
        boolean found = false;
        Point repeatPoint = new Point();

        String[] dirs = str.split(",");
        for (String dir : dirs) {
            dir = dir.trim();
            char turn = dir.charAt(0);
            int length = Integer.parseInt(dir.substring(1));

            if (turn == 'L') {
                faceIdx--;
            } else { // if (turn == 'R')
                faceIdx++;
            }
            faceIdx = (faceIdx + faces.length()) % faces.length();
            face = faces.charAt(faceIdx);

            for (int i = 0; i < length; i++) {
                switch (face) {
                    case 'N':
                        p.y += 1;
                        break;
                    case 'E':
                        p.x += 1;
                        break;
                    case 'S':
                        p.y -= 1;
                        break;
                    case 'W':
                    default:
                        p.x -= 1;
                        break;
                }

                if (!found) {
                    if (visited.contains(p)) {
                        found = true;
                        repeatPoint = new Point(p);
                    }
                    visited.add(new Point(p));
                }
            }
        }
        System.out.println(p);
        System.out.println(Math.abs(p.x) + Math.abs(p.y));

        System.out.println(repeatPoint);
        System.out.println(Math.abs(repeatPoint.x) + Math.abs(repeatPoint.y));
    }

}