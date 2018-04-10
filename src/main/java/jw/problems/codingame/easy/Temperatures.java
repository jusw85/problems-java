package jw.problems.codingame.easy;

import java.util.Scanner;

/**
 * https://www.codingame.com/training/easy/temperatures
 *
 * The Goal
 *
 * In this exercise, you have to analyze records of temperature to find the closest to zero.
 * Sample temperatures
 * Here, -1 is the closest to 0.
 * Rules
 * Write a program that prints the temperature closest to 0 among input data. If two numbers are equally close to zero, positive integer has to be considered closest to zero (for instance, if the temperatures are -5 and 5, then display 5).
 * Game Input
 * Your program must read the data from the standard input and write the result on the standard output.
 * Input
 *
 * Line 1: N, the number of temperatures to analyze
 *
 * Line 2: A string with the N temperatures expressed as integers ranging from -273 to 5526
 * Output
 * Display 0 (zero) if no temperatures are provided. Otherwise, display the temperature closest to 0.
 * Constraints
 * 0 ≤ N < 10000
 */
public class Temperatures {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of temperatures to analyse

        int closest = n == 0 ? 0 : Integer.MAX_VALUE;
        int absclosest = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int temp = in.nextInt();
            int abstemp = Math.abs(temp);
            if (abstemp < absclosest) {
                closest = temp;
                absclosest = abstemp;
            } else if (abstemp == absclosest && temp > 0) {
                closest = temp;
            }
        }
        System.out.println(closest);
    }

}
