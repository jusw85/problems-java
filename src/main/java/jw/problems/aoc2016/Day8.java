package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2016/day/8
 *
 * --- Day 8: Two-Factor Authentication ---
 *
 * You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three somewhat peculiar operations:
 *
 * rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
 * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
 * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
 *
 * For example, here is a simple sequence on a smaller screen:
 *
 * rect 3x2 creates a small rectangle in the top-left corner:
 *
 * ###....
 * ###....
 * .......
 *
 * rotate column x=1 by 1 rotates the second column down by one pixel:
 *
 * #.#....
 * ###....
 * .#.....
 *
 * rotate row y=0 by 4 rotates the top row right by four pixels:
 *
 * ....#.#
 * ###....
 * .#.....
 *
 * rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:
 *
 * .#..#.#
 * #.#....
 * .#.....
 *
 * As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.
 *
 * There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?
 *
 * Your puzzle answer was 116.
 * --- Part Two ---
 *
 * You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter is 5 pixels wide and 6 tall.
 *
 * After you swipe your card, what code is the screen trying to display?
 *
 * Your puzzle answer was UPOJFLBCEZ.
 */
public class Day8 {

    public static void main(String[] args) throws FileNotFoundException {
        int[][] screen = new int[6][50];

        Pattern p1 = Pattern.compile("rect (\\d+)x(\\d+)");
        Pattern p2 = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
        Pattern p3 = Pattern.compile("rotate column x=(\\d+) by (\\d+)");

        Scanner sc = new Scanner(new File("./etc/aoc2016/in8"));
        while (sc.hasNextLine()) {
            String str = sc.nextLine();

            Matcher m1 = p1.matcher(str);
            Matcher m2 = p2.matcher(str);
            Matcher m3 = p3.matcher(str);
            if (m1.matches()) {
                doRect(screen, Integer.parseInt(m1.group(1)), Integer.parseInt(m1.group(2)));
            } else if (m2.matches()) {
                doRow(screen, Integer.parseInt(m2.group(1)), Integer.parseInt(m2.group(2)));
            } else if (m3.matches()) {
                doCol(screen, Integer.parseInt(m3.group(1)), Integer.parseInt(m3.group(2)));
            }
        }
        sc.close();
        printScreenLetters(screen);
        System.out.println(countScreen(screen));
    }

    public static void doRect(int[][] screen, int x, int y) {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                screen[i][j] = 1;
            }
        }
    }

    public static void doRow(int[][] screen, int y, int count) {
        for (int c = 0; c < count; c++) {
            for (int j = screen[y].length - 1; j > 0; j--) {
                int tmp = screen[y][j];
                screen[y][j] = screen[y][j - 1];
                screen[y][j - 1] = tmp;
            }
        }
    }

    public static void doCol(int[][] screen, int x, int count) {
        for (int c = 0; c < count; c++) {
            for (int i = screen.length - 1; i > 0; i--) {
                int tmp = screen[i][x];
                screen[i][x] = screen[i - 1][x];
                screen[i - 1][x] = tmp;
            }
        }
    }

    public static int countScreen(int[][] screen) {
        int count = 0;
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                if (screen[i][j] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void printScreenLetters(int[][] screen) {
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                if (screen[i][j] > 0) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

}