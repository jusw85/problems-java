package jw.problems.aoc2015;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * http://adventofcode.com/2015/day/1
 *
 * --- Day 1: Not Quite Lisp ---
 *
 * Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars, and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.
 *
 * Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * Here's an easy puzzle to warm you up.
 *
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right floor - the directions he got are a little confusing. He starts on the ground floor (floor 0) and then follows the instructions one character at a time.
 *
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ), means he should go down one floor.
 *
 * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
 *
 * For example:
 *
 * (()) and ()() both result in floor 0.
 * ((( and (()(()( both result in floor 3.
 * ))((((( also results in floor 3.
 * ()) and ))( both result in floor -1 (the first basement level).
 * ))) and )())()) both result in floor -3.
 *
 * To what floor do the instructions take Santa?
 *
 * Your puzzle answer was 138.
 * --- Part Two ---
 *
 * Now, given the same instructions, find the position of the first character that causes him to enter the basement (floor -1). The first character in the instructions has position 1, the second character has position 2, and so on.
 *
 * For example:
 *
 * ) causes him to enter the basement at character position 1.
 * ()()) causes him to enter the basement at character position 5.
 *
 * What is the position of the character that causes Santa to first enter the basement?
 *
 * Your puzzle answer was 1771.
 */
public class Day1 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2015/in1"));
        String str = sc.nextLine();
        sc.close();

        int floor = 0;
        char[] chars = str.toCharArray();
        boolean isFound = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '(') {
                floor++;
            } else if (c == ')') {
                floor--;
            }

            if (!isFound && floor == -1) {
                isFound = true;
                System.out.println(i + 1);
            }
        }
        System.out.println(floor);
    }

}