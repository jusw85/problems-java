package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/21
 *
 * --- Day 21: Scrambled Letters and Hash ---
 *
 * The computer system you're breaking into uses a weird scrambling function to store its passwords. It shouldn't be much trouble to create your own scrambled password so you can add it to the system; you just have to implement the scrambler.
 *
 * The scrambling function is a series of operations (the exact list is provided in your puzzle input). Starting with the password to be scrambled, apply each operation in succession to the string. The individual operations behave as follows:
 *
 * swap position X with position Y means that the letters at indexes X and Y (counting from 0) should be swapped.
 * swap letter X with letter Y means that the letters X and Y should be swapped (regardless of where they appear in the string).
 * rotate left/right X steps means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
 * rotate based on position of letter X means that the whole string should be rotated to the right based on the index of letter X (counting from 0) as determined before this instruction does any rotations. Once the index is determined, rotate the string to the right one time, plus a number of times equal to that index, plus one additional time if the index was at least 4.
 * reverse positions X through Y means that the span of letters at indexes X through Y (including the letters at X and Y) should be reversed in order.
 * move position X to position Y means that the letter which is at index X should be removed from the string, then inserted such that it ends up at index Y.
 *
 * For example, suppose you start with abcde and perform the following operations:
 *
 * swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
 * swap letter d with letter b swaps the positions of d and b: edcba.
 * reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
 * rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
 * move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
 * move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
 * rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
 * rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
 *
 * After these steps, the resulting scrambled password is decab.
 *
 * Now, you just need to generate a new scrambled password and you can access the system. Given the list of scrambling operations in your puzzle input, what is the result of scrambling abcdefgh?
 *
 * Your puzzle answer was gbhcefad.
 * --- Part Two ---
 *
 * You scrambled the password correctly, but you discover that you can't actually modify the password file on the system. You'll need to un-scramble one of the existing passwords by reversing the scrambling process.
 *
 * What is the un-scrambled version of the scrambled password fbgdceah?
 *
 * Your puzzle answer was gahedfcb.
 */
public class Day21 {

    public static Pattern p1 = Pattern.compile("swap position (\\d+) with position (\\d+)");
    public static Pattern p2 = Pattern.compile("swap letter (.+) with letter (.+)");
    public static Pattern p3 = Pattern.compile("rotate (.+) (\\d+) steps?");
    public static Pattern p4 = Pattern.compile("rotate based on position of letter (.+)");
    public static Pattern p5 = Pattern.compile("reverse positions (\\d+) through (\\d+)");
    public static Pattern p6 = Pattern.compile("move position (\\d+) to position (\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in21"));
        List<String> lines = new ArrayList();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();

        String input;
        input = "abcdefgh";
        forward(lines, input);
        input = "fbgdceah";
        backward(lines, input);
    }

    public static void backward(List<String> lines, String input) {
        StringBuilder sb = new StringBuilder(input);
        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            Matcher m1 = p1.matcher(line);
            Matcher m2 = p2.matcher(line);
            Matcher m3 = p3.matcher(line);
            Matcher m4 = p4.matcher(line);
            Matcher m5 = p5.matcher(line);
            Matcher m6 = p6.matcher(line);
            if (m1.matches()) {
                int p1 = Integer.parseInt(m1.group(1));
                int p2 = Integer.parseInt(m1.group(2));
                swapPwithP(sb, p2, p1);
            } else if (m2.matches()) {
                char c1 = m2.group(1).charAt(0);
                char c2 = m2.group(2).charAt(0);
                swapLwithL(sb, c1, c2);
            } else if (m3.matches()) {
                boolean isLeft = m3.group(1).equals("left");
                int steps = Integer.parseInt(m3.group(2));
                rotate(sb, !isLeft, steps);
            } else if (m4.matches()) {
                char c = m4.group(1).charAt(0);
                for (int j = 1; j < sb.length(); j++) {
                    StringBuilder sb2 = new StringBuilder(sb);
                    rotate(sb2, true, j);
                    rotateLetter(sb2, c);
                    if (sb2.toString().equals(sb.toString())) {
                        rotate(sb, true, j);
                        break;
                    }
                }
            } else if (m5.matches()) {
                int p1 = Integer.parseInt(m5.group(1));
                int p2 = Integer.parseInt(m5.group(2));
                reverse(sb, p1, p2);
            } else if (m6.matches()) {
                int p1 = Integer.parseInt(m6.group(1));
                int p2 = Integer.parseInt(m6.group(2));
                movePtoP(sb, p2, p1);
            }
        }
        System.out.println(sb);
    }

    public static void forward(List<String> lines, String input) {
        StringBuilder sb = new StringBuilder(input);
        for (String line : lines) {
            Matcher m1 = p1.matcher(line);
            Matcher m2 = p2.matcher(line);
            Matcher m3 = p3.matcher(line);
            Matcher m4 = p4.matcher(line);
            Matcher m5 = p5.matcher(line);
            Matcher m6 = p6.matcher(line);
            if (m1.matches()) {
                int p1 = Integer.parseInt(m1.group(1));
                int p2 = Integer.parseInt(m1.group(2));
                swapPwithP(sb, p1, p2);
            } else if (m2.matches()) {
                char c1 = m2.group(1).charAt(0);
                char c2 = m2.group(2).charAt(0);
                swapLwithL(sb, c1, c2);
            } else if (m3.matches()) {
                boolean isLeft = m3.group(1).equals("left");
                int steps = Integer.parseInt(m3.group(2));
                rotate(sb, isLeft, steps);
            } else if (m4.matches()) {
                char c = m4.group(1).charAt(0);
                rotateLetter(sb, c);
            } else if (m5.matches()) {
                int p1 = Integer.parseInt(m5.group(1));
                int p2 = Integer.parseInt(m5.group(2));
                reverse(sb, p1, p2);
            } else if (m6.matches()) {
                int p1 = Integer.parseInt(m6.group(1));
                int p2 = Integer.parseInt(m6.group(2));
                movePtoP(sb, p1, p2);
            }
        }
        System.out.println(sb);
    }

    public static void swapPwithP(StringBuilder sb, int p1, int p2) {
        char c1 = sb.charAt(p1);
        char c2 = sb.charAt(p2);
        sb.setCharAt(p1, c2);
        sb.setCharAt(p2, c1);
    }

    public static void swapLwithL(StringBuilder sb, char c1, char c2) {
        int p1 = sb.indexOf(String.valueOf(c1));
        int p2 = sb.indexOf(String.valueOf(c2));
        sb.setCharAt(p1, c2);
        sb.setCharAt(p2, c1);
    }

    public static void reverse(StringBuilder sb, int p1, int p2) {
        StringBuilder sb2 = new StringBuilder(sb.subSequence(p1, p2 + 1));
        sb2.reverse();
        sb.replace(p1, p2 + 1, sb2.toString());
    }

    public static void rotate(StringBuilder sb, boolean isLeft, int steps) {
        steps %= sb.length();
        for (int i = 0; i < steps; i++) {
            if (isLeft) {
                char c = sb.charAt(0);
                sb.deleteCharAt(0);
                sb.append(c);
            } else {
                char c = sb.charAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
                sb.insert(0, c);
            }
        }
    }

    public static void rotateLetter(StringBuilder sb, char c) {
        int idx = sb.indexOf(String.valueOf(c));
        if (idx >= 4) {
            idx += 1;
        }
        rotate(sb, false, 1 + idx);
    }

    public static void movePtoP(StringBuilder sb, int p1, int p2) {
        char c = sb.charAt(p1);
        sb.deleteCharAt(p1);
        sb.insert(p2, c);
    }
}