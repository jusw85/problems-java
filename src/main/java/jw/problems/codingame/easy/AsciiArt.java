package jw.problems.codingame.easy;

import java.util.Scanner;

/**
 * https://www.codingame.com/training/easy/ascii-art
 *
 * The Goal
 * In stations and airports you often see this type of screen:
 * Have you ever asked yourself how it might be possible to simulate this display on a good old terminal? We have: with ASCII art!
 * Rules
 *
 * ASCII art allows you to represent forms by using characters. To be precise, in our case, these forms are words. For example, the word "MANHATTAN" could be displayed as follows in ASCII art:
 *
 * # #  #  ### # #  #  ### ###  #  ###
 * ### # # # # # # # #  #   #  # # # #
 * ### ### # # ### ###  #   #  ### # #
 * # # # # # # # # # #  #   #  # # # #
 * # # # # # # # # # #  #   #  # # # #
 *
 *
 * ​Your mission is to write a program that can display a line of text in ASCII art in a style you are given as input.
 * Game Input
 * Input
 *
 * Line 1: the width L of a letter represented in ASCII art. All letters are the same width.
 *
 * Line 2: the height H of a letter represented in ASCII art. All letters are the same height.
 *
 * Line 3: The line of text T, composed of N ASCII characters.
 *
 * Following lines: the string of characters ABCDEFGHIJKLMNOPQRSTUVWXYZ? Represented in ASCII art.
 * Output
 * The text T in ASCII art.
 * The characters a to z are shown in ASCII art by their equivalent in upper case.
 * The characters that are not in the intervals [a-z] or [A-Z] will be shown as a question mark in ASCII art.
 * Constraints
 * 0 < L < 30
 * 0 < H < 30
 * 0 < N < 200
 */
public class AsciiArt {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();
        int H = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        String T = in.nextLine().toLowerCase();
        String[] rows = new String[H];
        for (int i = 0; i < H; i++) {
            rows[i] = in.nextLine();
        }
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < T.length(); j++) {
                char c = T.charAt(j);
                int charIdx = c >= 'a' && c <= 'z' ? (c - 'a') : 26;
                int startIdx = charIdx * L;
                for (int k = 0; k < L; k++) {
                    System.out.print(rows[i].charAt(startIdx + k));
                }
            }
            System.out.println();
        }
    }

}
