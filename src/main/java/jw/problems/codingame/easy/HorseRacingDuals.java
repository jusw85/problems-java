package jw.problems.codingame.easy;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * https://www.codingame.com/ide/puzzle/horse-racing-duals
 *
 * The Goal
 * Casablanca’s hippodrome is organizing a new type of horse racing: duals. During a dual, only two horses will participate in the race. In order for the race to be interesting, it is necessary to try to select two horses with similar strength.
 *
 * Write a program which, using a given number of strengths, identifies the two closest strengths and shows their difference with an integer (≥ 0).
 * Game Input
 * Input
 *
 * Line 1: Number N of horses
 *
 * The N following lines: the strength Pi of each horse. Pi is an integer.
 * Output
 * The difference D between the two closest strengths. D is an integer greater than or equal to 0.
 * Constraints
 * 1 < N  < 100000
 * 0 < Pi ≤ 10000000
 */
public class HorseRacingDuals {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < N; i++) {
            int in = sc.nextInt();
            boolean inserted = set.add(in);
            if (!inserted) {
                System.out.println(0);
                return;
            }
        }

        int mindiff = Integer.MAX_VALUE;
        Iterator<Integer> it = set.iterator();
        int prev = it.next();
        while (it.hasNext()) {
            int curr = it.next();
            mindiff = Math.min(mindiff, curr - prev);
            prev = curr;
        }
        System.out.println(mindiff);
    }

}
