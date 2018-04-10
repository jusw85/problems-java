package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * http://adventofcode.com/2016/day/3
 *
 * --- Day 3: Squares With Three Sides ---
 *
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * Your puzzle answer was 983.
 * --- Part Two ---
 *
 * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.
 *
 * For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:
 *
 * 101 301 501
 * 102 302 502
 * 103 303 503
 * 201 401 601
 * 202 402 602
 * 203 403 603
 *
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 *
 * Your puzzle answer was 1836.
 */
public class Day3 {

    public static void main(String[] args) throws FileNotFoundException {
        part1();
        part2();
    }

    public static void part1() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in3"));
        int count = 0;
        while (sc.hasNextLine()) {
            Scanner sc2 = new Scanner(sc.nextLine());
            int l1 = sc2.nextInt();
            int l2 = sc2.nextInt();
            int l3 = sc2.nextInt();
            if (isValid(l1, l2, l3)) {
                count++;
            }
            sc2.close();
        }
        System.out.println(count);
        sc.close();
    }

    public static void part2() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in3"));
        int count = 0;
        while (sc.hasNextLine()) {
            int[][] t = new int[3][3];
            for (int i = 0; i < 3; i++) {
                Scanner sc2 = new Scanner(sc.nextLine());
                for (int j = 0; j < 3; j++) {
                    t[j][i] = sc2.nextInt();
                }
                sc2.close();
            }
            for (int i = 0; i < 3; i++) {
                if (isValid(t[i][0], t[i][1], t[i][2])) {
                    count++;
                }
            }
        }
        System.out.println(count);
        sc.close();
    }

    public static boolean isValid(int l1, int l2, int l3) {
        return (l1 + l2 > l3 && l2 + l3 > l1 && l1 + l3 > l2);
    }
}