package jw.problems.adventofcode.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2017/day/21
 *
 * --- Day 21: Fractal Art ---
 *
 * You find a program trying to generate some art. It uses a strange process that involves repeatedly enhancing the detail of an image through a set of rules.
 *
 * The image consists of a two-dimensional square grid of pixels that are either on (#) or off (.). The program always begins with this pattern:
 *
 * .#.
 * ..#
 * ###
 *
 * Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a size of 3.
 *
 * Then, the program repeats the following process:
 *
 * If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and convert each 2x2 square into a 3x3 square by following the corresponding enhancement rule.
 * Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and convert each 3x3 square into a 4x4 square by following the corresponding enhancement rule.
 *
 * Because each square of pixels is replaced by a larger one, the image gains pixels and so its size increases.
 *
 * The artist's book of enhancement rules is nearby (your puzzle input); however, it seems to be missing rules. The artist explains that sometimes, one must rotate or flip the input pattern to find a match. (Never rotate or flip the output pattern, though.) Each pattern is written concisely: rows are listed as single units, ordered top-down, and separated by slashes. For example, the following rules correspond to the adjacent patterns:
 *
 * ../.#  =  ..
 * .#
 *
 * .#.
 * .#./..#/###  =  ..#
 * ###
 *
 * #..#
 * #..#/..../#..#/.##.  =  ....
 * #..#
 * .##.
 *
 * When searching for a rule to use, rotate and flip the pattern as necessary. For example, all of the following patterns match the same rule:
 *
 * .#.   .#.   #..   ###
 * ..#   #..   #.#   ..#
 * ###   ###   ##.   .#.
 *
 * Suppose the book contained the following two rules:
 *
 * ../.# => ##./#../...
 * .#./..#/### => #..#/..../..../#..#
 *
 * As before, the program begins with this pattern:
 *
 * .#.
 * ..#
 * ###
 *
 * The size of the grid (3) is not divisible by 2, but it is divisible by 3. It divides evenly into a single square; the square matches the second rule, which produces:
 *
 * #..#
 * ....
 * ....
 * #..#
 *
 * The size of this enhanced grid (4) is evenly divisible by 2, so that rule is used. It divides evenly into four squares:
 *
 * #.|.#
 * ..|..
 * --+--
 * ..|..
 * #.|.#
 *
 * Each of these squares matches the same rule (../.# => ##./#../...), three of which require some flipping and rotation to line up with the rule. The output for the rule is the same in all four cases:
 *
 * ##.|##.
 * #..|#..
 * ...|...
 * ---+---
 * ##.|##.
 * #..|#..
 * ...|...
 *
 * Finally, the squares are joined into a new grid:
 *
 * ##.##.
 * #..#..
 * ......
 * ##.##.
 * #..#..
 * ......
 *
 * Thus, after 2 iterations, the grid contains 12 pixels that are on.
 *
 * How many pixels stay on after 5 iterations?
 *
 * Your puzzle answer was 167.
 * --- Part Two ---
 *
 * How many pixels stay on after 18 iterations?
 *
 * Your puzzle answer was 2425195.
 */
public class Day21 {
    public static Pattern p = Pattern.compile("(.+) => (.+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in21"));
        Map<String, String> map = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String k = m.group(1);
            String v = m.group(2);
            parse(map, k, v);
        }
        sc.close();

        String root = ".#...####";
        StringBuilder sb = new StringBuilder(root);
        int numIterations = 18;
        for (int num = 0; num < numIterations; num++) {
            int charLen = (int) Math.sqrt(sb.length());
            int blockSize;
            if (charLen % 2 == 0) {
                blockSize = 2;
            } else {
                blockSize = 3;
            }
            int newBlockSize = blockSize + 1;
            int blockLen = charLen / blockSize;
            StringBuilder sb2 = new StringBuilder();
            int newlen = blockLen * newBlockSize;
            sb2.setLength(newlen * newlen);

            for (int i = 0; i < blockLen; i++) {
                for (int j = 0; j < blockLen; j++) {
                    StringBuilder sbTemp = new StringBuilder();
                    for (int k = blockSize * i; k < (blockSize * i) + blockSize; k++) {
                        for (int l = blockSize * j; l < (blockSize * j) + blockSize; l++) {
                            sbTemp.append(sb.charAt(k * charLen + l));
                        }
                    }
                    String newBlock = map.get(sbTemp.toString());
                    sbTemp = new StringBuilder(newBlock);
                    for (int k = newBlockSize * i, idx = 0; k < (newBlockSize * i) + newBlockSize; k++) {
                        for (int l = newBlockSize * j; l < (newBlockSize * j) + newBlockSize; l++, idx++) {
                            sb2.setCharAt((k * newlen + l), sbTemp.charAt(idx));
                        }
                    }
                }
            }
            sb = sb2;
        }

        int count = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '#')
                count++;
        }
        System.out.println(count);
    }

    public static void parse(Map<String, String> m, String k, String v) {
        k = k.replace("/", "");
        v = v.replace("/", "");
        String kf = twiddle(k, false);
        String kr = twiddle(k, true);
        String kfr = twiddle(kf, true);
        m.put(k, v);
        m.put(kf, v);
        m.put(kr, v);
        m.put(kfr, v);
        m.put(reverse(k), v);
        m.put(reverse(kf), v);
        m.put(reverse(kr), v);
        m.put(reverse(kfr), v);
    }

    public static int[][] rotateMap4 = {{0, 1}, {1, 3}, {3, 2}, {2, 0}};
    public static int[][] rotateMap9 = {
            {0, 2}, {2, 8}, {8, 6}, {6, 0},
            {1, 5}, {5, 7}, {7, 3}, {3, 1}, {4, 4}};

    public static int[][] flipMap4 = {{0, 1}, {1, 0}, {2, 3}, {3, 2}};
    public static int[][] flipMap9 = {
            {0, 2}, {1, 1}, {2, 0},
            {3, 5}, {4, 4}, {5, 3},
            {6, 8}, {7, 7}, {8, 6}};

    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    public static String twiddle(String str, boolean isRotate) {
        StringBuilder sb = new StringBuilder();
        int[][] map;
        if (str.length() == 4) {
            sb.setLength(4);
            if (isRotate)
                map = rotateMap4;
            else
                map = flipMap4;
        } else if (str.length() == 9) {
            sb.setLength(9);
            if (isRotate)
                map = rotateMap9;
            else
                map = flipMap9;
        } else {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < map.length; i++) {
            sb.setCharAt(map[i][1], str.charAt(map[i][0]));
        }
        return sb.toString();
    }

}